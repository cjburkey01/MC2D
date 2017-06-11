package com.cjburkey.mc2d.input;

import java.util.HashMap;
import java.util.Map;

public final class KeyBinds {
	
	private static final Map<String, Integer> keys = new HashMap<>();
	
	public static void addKeyBind(String name, int key) {
		keys.put(name, key);
	}
	
	public static int getKeyBound(String name) {
		Integer val = keys.get(name);
		if(val == null) {
			return -1;
		}
		return val;
	}
	
	public static boolean keyPressed(Input i, String key) {
		return i.keyPressed(getKeyBound(key));
	}
	
	public static boolean keyReleased(Input i, String key) {
		return i.keyReleased(getKeyBound(key));
	}
	
	public static boolean keyHeld(Input i, String key) {
		return i.keyHeld(getKeyBound(key));
	}
	
}