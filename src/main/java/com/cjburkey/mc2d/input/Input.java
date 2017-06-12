package com.cjburkey.mc2d.input;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.window.GLFWWindow;

public final class Input {

	private final Map<Integer, Boolean> toAdd = new ConcurrentHashMap<>();
	private final Map<Integer, Boolean> current = new ConcurrentHashMap<>();
	private final Collection<Integer> pressed = new ConcurrentLinkedQueue<>();
	private final Vector2f cursorPos = new Vector2f();
	
	public void renderInit() {
		final GLFWWindow window = MC2D.INSTANCE.getWindow();
		GLFW.glfwSetKeyCallback(window.getWindow(), (win, key, code, action, mods) -> {
			if(action == GLFW.GLFW_PRESS) {
				onKeyPress(key);
			} else if(action == GLFW.GLFW_RELEASE) {
				onKeyRelease(key);
			}
		});
		GLFW.glfwSetCursorPosCallback(window.getWindow(), (win, x, y) -> {
			boolean inX = x >= 0.0d && x < window.getWindowSize().x;
			boolean inY = y >= 0.0d && y < window.getWindowSize().y;
			if(inX && inY) {
				cursorPos.x = (float) x;
				cursorPos.y = (float) y;
			}
		});
	}
	
	public void tick() {
		current.clear();
		current.putAll(toAdd);
		toAdd.clear();
	}
	
	public boolean keyPressed(int key) {
		Integer keyIn = (Integer) key;
		if(current.containsKey(keyIn)) {
			return current.get(keyIn);
		}
		return false;
	}
	
	public boolean keyReleased(int key) {
		Integer keyIn = (Integer) key;
		if(current.containsKey(keyIn)) {
			return !current.get(keyIn);
		}
		return false;
	}
	
	public boolean keyHeld(int key) {
		for(Integer in : pressed) {
			if((int) in == key) {
				return true;
			}
		}
		return false;
	}
	
	public Vector2f getCursorPosition() {
		return cursorPos;
	}
	
	private void onKeyPress(int key) {
		toAdd.put(key, true);
		pressed.add(key);
	}
	
	private void onKeyRelease(int key) {
		toAdd.put(key, false);
		pressed.remove(key);
	}
	
}