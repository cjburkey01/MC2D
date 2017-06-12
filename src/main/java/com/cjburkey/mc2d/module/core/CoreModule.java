package com.cjburkey.mc2d.module.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.chunk.World;
import com.cjburkey.mc2d.core.SemVer;
import com.cjburkey.mc2d.input.Input;
import com.cjburkey.mc2d.input.KeyBinds;
import com.cjburkey.mc2d.object.CameraController;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Mesh;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.render.Renderer;
import com.cjburkey.mc2d.render.TextureAtlas;

public final class CoreModule extends ICoreModule {
	
	public static CoreModule instance;
	public static final int chunkRange = 5;
	
	private final Input input;
	private final Renderer renderer;
	private final Queue<GameObject> gameObjs;
	private final TextureAtlas atlas;
	private final CameraController camControl;
	private final World world;
	
	public CoreModule() {
		instance = this;
		input = new Input();
		renderer = new Renderer();
		gameObjs = new ConcurrentLinkedQueue<>();
		atlas = new TextureAtlas();
		camControl = new CameraController(1.0f, renderer.getCamera());
		world = new World();
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
		KeyBinds.addKeyBind("left", GLFW.GLFW_KEY_A);
		KeyBinds.addKeyBind("right", GLFW.GLFW_KEY_D);
		KeyBinds.addKeyBind("up", GLFW.GLFW_KEY_W);
		KeyBinds.addKeyBind("down", GLFW.GLFW_KEY_S);
		KeyBinds.addKeyBind("in", GLFW.GLFW_KEY_UP);
		KeyBinds.addKeyBind("out", GLFW.GLFW_KEY_DOWN);
	}
	
	public void onLogicTick() {
		if(input.keyPressed(GLFW.GLFW_KEY_ESCAPE)) {
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
			Transformation.scale -= 1.5f;
		}
		
		if(KeyBinds.keyHeld(input, "out")) {
			Transformation.scale += 1.5f;
		}
		
		world.generateChunksAround(renderer.getCamera().getPosition(), chunkRange);
		world.renderChunksAround(renderer.getCamera().getPosition(), chunkRange);
		world.deRenderChunksAround(renderer.getCamera().getPosition(), chunkRange);
		
		input.tick();
	}
	
	public void onLogicCleanup() {
		
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
		for(Mesh m : obj.getMesh()) {
			renderer.runLater(() -> m.cleanup());
		}
	}
	
	public TextureAtlas getTextures() {
		return atlas;
	}
	
}