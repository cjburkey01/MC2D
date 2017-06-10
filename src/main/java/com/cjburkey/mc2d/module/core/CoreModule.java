package com.cjburkey.mc2d.module.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.SemVer;
import com.cjburkey.mc2d.input.Input;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Mesh;
import com.cjburkey.mc2d.render.Renderer;

public class CoreModule extends ICoreModule {
	
	private final Input input;
	private final Renderer renderer;
	private final Queue<GameObject> gameObjs;
	
	public CoreModule() {
		input = new Input();
		renderer = new Renderer();
		gameObjs = new ConcurrentLinkedQueue<>();
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
		GameObject tri;
		gameObjs.add(tri = new GameObject(new Mesh(new float[] {
				-0.5f, 0.5f, 0.5f,		// 0
				-0.5f, -0.5f, 0.5f,		// 1
				0.5f, -0.5f, 0.5f,		// 2
				0.5f, 0.5f, 0.5f,		// 3
				-0.5f, 0.5f, -0.5f,		// 4
				0.5f, 0.5f, -0.5f,		// 5
				-0.5f, -0.5f, -0.5f,	// 6
				0.5f, -0.5f, -0.5f,		// 7
		}, new float[] {
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f
		}, new int[] {
				0, 1, 3, 3, 1, 2,
				4, 0, 3, 5, 4, 3,
				3, 2, 7, 5, 3, 7,
				6, 1, 0, 6, 0, 4,
				2, 1, 6, 2, 6, 7,
				7, 6, 4, 7, 4, 5
		})));
		
		tri.getMesh().buildMesh();
		tri.setPosition(0.0f, 0.0f, -2.0f);
		
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
	
}