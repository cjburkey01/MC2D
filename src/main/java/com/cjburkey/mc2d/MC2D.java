package com.cjburkey.mc2d;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.core.Logger;
import com.cjburkey.mc2d.core.SemVer;
import com.cjburkey.mc2d.loop.Loops;
import com.cjburkey.mc2d.module.core.ICoreModule;
import com.cjburkey.mc2d.module.core.ModuleHandler;
import com.cjburkey.mc2d.window.GLFWWindow;

public class MC2D {
	
	public static final SemVer VERSION = new SemVer(0, 0, 1);
	public static final MC2D INSTANCE = new MC2D();
	
	private Logger logger;
	private final ModuleHandler<ICoreModule> modules = new ModuleHandler<>();
	private final GLFWWindow window = new GLFWWindow();
	private final Loops loops = new Loops();
	
	public static void main(String[] args) {
		System.out.println("Game opened.");
		INSTANCE.init();
		INSTANCE.logger.log("Game closed.");
	}
	
	private void init() {
		logger = new Logger();
		logger.hijackOutput();
		logger.log("Loading game...");
		window.create();
		
		logger.log("Info dump:");
		logger.log(" -  LWJGL:\t" + Version.getVersion());
		logger.log(" -  OpenGL:\t" + GL11.glGetString(GL11.GL_VERSION));
		logger.log(" -  MC2D:\t\t" + VERSION);
		modules.loadModules(ICoreModule.class);
		initLoops();
		startLoops();
		window.cleanup();
	}
	
	public void stopGame() {
		loops.stop();
	}
	
	private void initLoops() {
		Runnable logicInit = () -> modules.getEventSystem().logicInit();
		Runnable logicTick = () -> modules.getEventSystem().logicTick();
		Runnable logicClean = () -> modules.getEventSystem().logicCleanup();
		Runnable renderInit = () -> {
			window.show();
			modules.getEventSystem().renderInit();
		};
		Runnable renderUpdate = () -> {
			if(window.shouldWindowClose()) {
				stopGame();
			}
			window.render();
			modules.getEventSystem().renderUpdate();
		};
		Runnable renderClean = () -> modules.getEventSystem().renderCleanup();
		loops.init(logicInit, logicTick, logicClean, renderInit, renderUpdate, renderClean);
	}
	
	public GLFWWindow getWindow() {
		return window;
	}
	
	private void startLoops() {
		loops.start();
	}
	
	public static Logger getLogger() {
		return INSTANCE.logger;
	}
	
}