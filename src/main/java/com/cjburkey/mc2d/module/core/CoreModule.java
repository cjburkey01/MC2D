package com.cjburkey.mc2d.module.core;

import org.lwjgl.glfw.GLFW;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.SemVer;
import com.cjburkey.mc2d.input.Input;
import com.cjburkey.mc2d.render.Renderer;

public class CoreModule extends ICoreModule {
	
	private final Input input;
	private final Renderer renderer;
	
	public CoreModule() {
		input = new Input();
		renderer = new Renderer();
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
		input.renderInit();
		renderer.init();
		GLFW.glfwFocusWindow(MC2D.INSTANCE.getWindow().getWindow());
	}
	
	public void onRenderUpdate() {
		renderer.render();
	}
	
	public void onRenderCleanup() {
		renderer.cleanup();
	}
	
}