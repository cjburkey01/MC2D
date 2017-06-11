package com.cjburkey.mc2d.module.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.chunk.ChunkData;
import com.cjburkey.mc2d.chunk.MeshChunk;
import com.cjburkey.mc2d.core.SemVer;
import com.cjburkey.mc2d.input.Input;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.render.Renderer;
import com.cjburkey.mc2d.render.TextureAtlas;

public class CoreModule extends ICoreModule {
	
	public static CoreModule instance;
	
	private final Input input;
	private final Renderer renderer;
	private final Queue<GameObject> gameObjs;
	private final TextureAtlas atlas;
	
	public CoreModule() {
		instance = this;
		input = new Input();
		renderer = new Renderer();
		gameObjs = new ConcurrentLinkedQueue<>();
		atlas = new TextureAtlas();
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
		
	}
	
	public void onLogicTick() {
		if(input.keyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			MC2D.INSTANCE.stopGame();
		}
		
		input.tick();
	}
	
	public void onLogicCleanup() {
		
	}
	
	public void onRenderInit() {
		GameObject chunk = new GameObject(MeshChunk.generateChunkMesh(new ChunkData(), atlas));
		chunk.setPosition(0.0f, 0.0f, -2.0f);
		gameObjs.add(chunk);
		
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
				obj.getMesh().cleanup();
			}
		}
		renderer.cleanup();
	}
	
	public TextureAtlas getTextures() {
		return atlas;
	}
	
}