package com.cjburkey.mc2d.window;

import org.joml.Vector2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.mc2d.MC2D;

public class GLFWWindow {
	
	private long window;
	private String title;
	private final Vector2i size = new Vector2i();;
	
	public void create() {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!GLFW.glfwInit()) {
			System.err.println("Couldn't initialize GLFW.");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		
		window = GLFW.glfwCreateWindow(10, 10, "MC2D", MemoryUtil.NULL, MemoryUtil.NULL);
		if(window == MemoryUtil.NULL) {
			System.err.println("Couldn't create GLFW window.");
		}
		
		GLFW.glfwSetWindowSizeCallback(window, (w, width, height) -> setWindowSize(width, height));
		
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GLFW.glfwSwapInterval(1);
		GLFW.glfwSwapBuffers(window);
	}
	
	public void centerOnScreen() {
		Vector2i monitor = getMonitorSize();
		GLFW.glfwSetWindowPos(window, (monitor.x - size.x) / 2, (monitor.y - size.y) / 2);
	}
	
	public void setWindowSize(int width, int height) {
		size.x = width;
		size.y = height;
		GLFW.glfwSetWindowSize(window, size.x, size.y);
		GL11.glViewport(0, 0, size.x, size.y);
	}
	
	public void show() {
		setTitle("MC2D v" + MC2D.VERSION);
		setWindowSize(getMonitorSize().x / 2, getMonitorSize().y / 2);
		centerOnScreen();
		GLFW.glfwShowWindow(window);
	}
	
	public void render() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}
	
	public void cleanup() {
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
	
	public Vector2i getWindowSize() {
		return new Vector2i(size);
	}
	
	public Vector2i getMonitorSize() {
		GLFWVidMode vid = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		return new Vector2i(vid.width(), vid.height());
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		GLFW.glfwSetWindowTitle(window, title);
	}
	
	public boolean shouldWindowClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public long getWindow() {
		return window;
	}
	
}