package com.cjburkey.mc2d.loop;

public final class Loops {
	
	public int FPS, UPS;
	
	private GameLoop gameLoop;
	private RenderLoop renderLoop;
	
	public Loops() {
		gameLoop = new GameLoop(this);
		renderLoop = new RenderLoop(this);
	}
	
	public void init(Runnable logicInit, Runnable logicTick, Runnable logicCleanup, Runnable renderInit, Runnable renderUpdate, Runnable renderCleanup) {
		gameLoop.init(logicInit, logicTick, logicCleanup);
		renderLoop.init(renderInit, renderUpdate, renderCleanup);
	}
	
	public void start() {
		gameLoop.start();
		System.out.println("Loaded game.");
		renderLoop.start();
	}
	
	public void stop() {
		renderLoop.stop();
		gameLoop.stop();
	}
	
}