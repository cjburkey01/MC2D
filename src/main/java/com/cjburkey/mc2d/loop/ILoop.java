package com.cjburkey.mc2d.loop;

public interface ILoop {
	
	void init(Runnable onInit, Runnable onTick, Runnable onCleanup);
	void start();
	void stop();

	Loops getHandler();
	
}