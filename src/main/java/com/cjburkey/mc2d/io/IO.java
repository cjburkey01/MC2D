package com.cjburkey.mc2d.io;

import java.io.File;

public final class IO {
	
	public static File getGameDir() {
		File dir = new File(System.getProperty("user.home"), "/MC2D/");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	public static File getTmpDir() {
		File dir = new File(getGameDir(), "/tmp/");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
}