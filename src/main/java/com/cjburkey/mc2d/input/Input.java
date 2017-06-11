package com.cjburkey.mc2d.input;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.mc2d.MC2D;

public final class Input {

	private final Map<Integer, Boolean> toAdd = new ConcurrentHashMap<>();
	private final Map<Integer, Boolean> current = new ConcurrentHashMap<>();
	private final Collection<Integer> pressed = new ConcurrentLinkedQueue<>();
	
	public void renderInit() {
		GLFW.glfwSetKeyCallback(MC2D.INSTANCE.getWindow().getWindow(), (win, key, code, action, mods) -> {
			if(action == GLFW.GLFW_PRESS) {
				onKeyPress(key);
			} else if(action == GLFW.GLFW_RELEASE) {
				onKeyRelease(key);
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
	
	private void onKeyPress(int key) {
		toAdd.put(key, true);
		pressed.add(key);
	}
	
	private void onKeyRelease(int key) {
		toAdd.put(key, false);
		pressed.remove(key);
	}
	
}