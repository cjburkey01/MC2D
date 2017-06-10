package com.cjburkey.mc2d.loop;

import com.cjburkey.mc2d.MC2D;

public class RenderLoop implements ILoop {
	
	private Runnable onInit;
	private Runnable onTick;
	private Runnable onCleanup;
	
	private boolean running = false;
	private final Loops parent;
	private int frames;
	private long lastSecond;
	private String original;
	
	public RenderLoop(Loops parent) {
		this.parent = parent;
	}
	
	public void init(Runnable onInit, Runnable onTick, Runnable onCleanup) {
		this.onInit = onInit;
		this.onTick = onTick;
		this.onCleanup = onCleanup;
	}
	
	public void start() {
		running = true;
		onInit.run();
		while(running) {
			onTick.run();
			long now = System.nanoTime();
			if(original == null) {
				original = MC2D.INSTANCE.getWindow().getTitle();
			}
			if(now - lastSecond >= 1000000000.0d) {
				lastSecond = now;
				parent.FPS = frames;
				frames = 0;
				MC2D.INSTANCE.getWindow().setTitle(original + " (FPS: " + parent.FPS + ", UPS: " + parent.UPS + ")");
			} else {
				frames++;
			}
		}
		onCleanup.run();
	}
	
	public void stop() {
		running = false;
	}
	
	public Loops getHandler() {
		return parent;
	}
	
}