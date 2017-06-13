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
	
	private final Map<Integer, Boolean> toAddKey = new ConcurrentHashMap<>();
	private final Map<Integer, Boolean> currentKey = new ConcurrentHashMap<>();
	private final Collection<Integer> pressedKey = new ConcurrentLinkedQueue<>();

	private final Map<Integer, Boolean> toAddCur = new ConcurrentHashMap<>();
	private final Map<Integer, Boolean> currentCur = new ConcurrentHashMap<>();
	private final Collection<Integer> pressedCur = new ConcurrentLinkedQueue<>();
	
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
		
		GLFW.glfwSetMouseButtonCallback(window.getWindow(), (win, button, action, mods) -> {
			if(action == GLFW.GLFW_PRESS) {
				onMousePress(button);
			} else if(action == GLFW.GLFW_RELEASE) {
				onMouseRelease(button);
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
		currentKey.clear();
		currentKey.putAll(toAddKey);
		toAddKey.clear();

		currentCur.clear();
		currentCur.putAll(toAddCur);
		toAddCur.clear();
	}
	
	public boolean isMouseDown(int button) {
		Integer buttonIn = (Integer) button;
		if(currentCur.containsKey(buttonIn)) {
			return currentCur.get(buttonIn);
		}
		return false;
	}
	
	public boolean isMouseUp(int button) {
		Integer buttonIn = (Integer) button;
		if(currentCur.containsKey(buttonIn)) {
			return !currentCur.get(buttonIn);
		}
		return false;
	}
	
	public boolean isMouseHeld(int button) {
		for(Integer buttonIn : pressedCur) {
			if((int) buttonIn == button) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isKeyDown(int key) {
		Integer keyIn = (Integer) key;
		if(currentKey.containsKey(keyIn)) {
			return currentKey.get(keyIn);
		}
		return false;
	}
	
	public boolean isKeyUp(int key) {
		Integer keyIn = (Integer) key;
		if(currentKey.containsKey(keyIn)) {
			return !currentKey.get(keyIn);
		}
		return false;
	}
	
	public boolean isKeyHeld(int key) {
		for(Integer in : pressedKey) {
			if((int) in == key) {
				return true;
			}
		}
		return false;
	}
	
	private void onMousePress(int button) {
		toAddCur.put(button, true);
		pressedCur.add(button);
	}
	
	private void onMouseRelease(int button) {
		toAddCur.put(button, false);
		pressedCur.remove(button);
	}
	
	private void onKeyPress(int key) {
		toAddKey.put(key, true);
		pressedKey.add(key);
	}
	
	private void onKeyRelease(int key) {
		toAddKey.put(key, false);
		pressedKey.remove(key);
	}
	
	public Vector2f getCursorPosition() {
		return cursorPos;
	}
	
}