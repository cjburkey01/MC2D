package com.cjburkey.mc2d.loop;

public final class GameLoop implements ILoop {
	
	private final double BETWEEN_TICKS;
	private final Thread thread;
	
	private Runnable onInit;
	private Runnable onTick;
	private Runnable onCleanup;
	
	private long lastTick;
	private long lastSecond;
	private int ticks;
	private boolean running = false;
	private final Loops parent;
	
	public GameLoop(Loops parent) {
		this.parent = parent;
		BETWEEN_TICKS = 1000000000.0d / 60.0d;
		thread = new Thread(() -> {
			running = true;
			onInit.run();
			while(running) {
				long now = System.nanoTime();
				if(now - lastTick >= BETWEEN_TICKS) {
					lastTick = now;
					onTick.run();
					if(now - lastSecond >= 1000000000.0d) {
						lastSecond = now;
						parent.UPS = ticks;
						ticks = 0;
					}
					ticks ++;
				}
			}
			onCleanup.run();
		});
	}
	
	public void init(Runnable onInit, Runnable onTick, Runnable onCleanup) {
		this.onInit = onInit;
		this.onTick = onTick;
		this.onCleanup = onCleanup;
	}
	
	public void start() {
		thread.start();
	}
	
	public void stop() {
		running = false;
	}
	
	public Loops getHandler() {
		return parent;
	}
	
}