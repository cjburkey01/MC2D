package com.cjburkey.mc2d.module.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.block.BlockState;
import com.cjburkey.mc2d.chunk.ChunkData;
import com.cjburkey.mc2d.core.SemVer;
import com.cjburkey.mc2d.gui.GameObjectSelector;
import com.cjburkey.mc2d.input.Input;
import com.cjburkey.mc2d.input.KeyBinds;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.render.CameraController;
import com.cjburkey.mc2d.render.Renderer;
import com.cjburkey.mc2d.render.TextureAtlas;
import com.cjburkey.mc2d.world.World;

public final class CoreModule extends ICoreModule {
	
	public static CoreModule instance;
	public static final int chunkRange = 5;
	
	private final Input input;
	private final Renderer renderer;
	private final Queue<GameObject> gameObjs;
	private final TextureAtlas atlas;
	private final CameraController camControl;
	private final World world;
	
	private final GameObjectSelector crosshair;
	
	public CoreModule() {
		instance = this;
		input = new Input();
		renderer = new Renderer();
		gameObjs = new ConcurrentLinkedQueue<>();
		atlas = new TextureAtlas();
		camControl = new CameraController(5.0f, renderer.getCamera());
		world = new World();
		
		crosshair = new GameObjectSelector();
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	public SemVer getRequiredGameVersion() {
		return MC2D.VERSION.clone();
	}
	
	public boolean getPatchAllowDifference() {
		return false;
	}
	
	public boolean getMinorAllowDifference() {
		return false;
	}
	
	public boolean getMajorAllowDifference() {
		return false;
	}
	
	public void onLogicInit() {
		KeyBinds.addKeyBind("cancel", GLFW.GLFW_KEY_ESCAPE);
		KeyBinds.addKeyBind("left", GLFW.GLFW_KEY_A);
		KeyBinds.addKeyBind("right", GLFW.GLFW_KEY_D);
		KeyBinds.addKeyBind("up", GLFW.GLFW_KEY_W);
		KeyBinds.addKeyBind("down", GLFW.GLFW_KEY_S);
		KeyBinds.addKeyBind("in", GLFW.GLFW_KEY_UP);
		KeyBinds.addKeyBind("out", GLFW.GLFW_KEY_DOWN);
		
		world.startGenerating(renderer.getCamera(), chunkRange);
		addGameObject(crosshair);
	}
	
	public void onLogicTick() {
		if(KeyBinds.keyPressed(input, "cancel")) {
			MC2D.INSTANCE.stopGame();
		}
		
		if(KeyBinds.keyHeld(input, "left")) {
			camControl.left();
		}
		
		if(KeyBinds.keyHeld(input, "right")) {
			camControl.right();
		}
		
		if(KeyBinds.keyHeld(input, "up")) {
			camControl.up();
		}
		
		if(KeyBinds.keyHeld(input, "down")) {
			camControl.down();
		}
		
		if(KeyBinds.keyHeld(input, "in")) {
			Transformation.scale -= 2.5f;
		}
		
		if(KeyBinds.keyHeld(input, "out")) {
			Transformation.scale += 2.5f;
		}
		
		// TODO: FIX CHUNK CORNER BLOCK DESTRUCTION
		if(input.isMouseHeld(KeyBinds.LEFT)) {
			BlockState cursor = world.getBlockAtWorldPos(crosshair.getPosition());
			if(cursor != null && cursor.getBlock() != null) {
				Vector2i pos = cursor.getPosition();
				Vector2i chunk = cursor.getChunk().getChunkCoords();
				pos.sub(cursor.getChunk().getChunkCoords().mul(ChunkData.chunkSize));
				cursor.getChunk().removeBlock(pos.x, pos.y);
				renderer.runLater(() -> world.reRenderChunk(chunk.x, chunk.y));
			}
		}
		
		input.tick();
	}
	
	public void onLogicCleanup() {
		world.cleanup();
	}
	
	public void onRenderInit() {
		input.renderInit();
		renderer.init();
		GLFW.glfwFocusWindow(MC2D.INSTANCE.getWindow().getWindow());
	}
	
	public void onRenderUpdate() {
		renderer.render(gameObjs.toArray(new GameObject[gameObjs.size()]));
	}
	
	public void onRenderCleanup() {
		for(GameObject obj : gameObjs) {
			if(obj != null) {
				obj.cleanup();
			}
		}
		renderer.cleanup();
	}
	
	public void addGameObject(GameObject obj) {
		gameObjs.add(obj);
	}
	
	public void removeGameObject(GameObject obj) {
		gameObjs.remove(obj);
		renderer.runLater(() -> obj.cleanup());
	}
	
	public TextureAtlas getTextures() {
		return atlas;
	}
	
	public Input getInput() {
		return input;
	}
	
	public World getWorld() {
		return world;
	}
	
}