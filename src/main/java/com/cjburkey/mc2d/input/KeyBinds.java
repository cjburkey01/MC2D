package com.cjburkey.mc2d.input;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.glfw.GLFW;

public final class KeyBinds {
	
	public static final int LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	public static final int RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	
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
		return i.isKeyDown(getKeyBound(key));
	}
	
	public static boolean keyReleased(Input i, String key) {
		return i.isKeyUp(getKeyBound(key));
	}
	
	public static boolean keyHeld(Input i, String key) {
		return i.isKeyHeld(getKeyBound(key));
	}
	
}