package com.cjburkey.mc2d.render;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.Utils;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.window.GLFWWindow;

public class Renderer {
	
	public static Renderer instance;
	
	private final Queue<Runnable> doLater;
	private ShaderProgram blockShader;
	private final Transformation transform;
	
	public Renderer() {
		instance = this;
		doLater = new ConcurrentLinkedQueue<>();
		transform = new Transformation();
	}
	
	public void init() {
		blockShader = new ShaderProgram();
		blockShader.createVertexShader(Utils.readResourceAsString("mc2d:shader/block/block.vs"));
		blockShader.createFragmentShader(Utils.readResourceAsString("mc2d:shader/block/block.fs"));
		blockShader.link();
		
		blockShader.createUniform("projectionMatrix");
		blockShader.createUniform("worldMatrix");
		blockShader.createUniform("texture_sampler");
	}
	
	public void render(GameObject[] objs) {
		clear();
		GLFWWindow window = MC2D.INSTANCE.getWindow();
		blockShader.bind();
		
		Matrix4f projectionMatrix = transform.getProjectionMatrix(window.getWindowSize().x, window.getWindowSize().y);
		blockShader.setUniform("projectionMatrix", projectionMatrix);
		blockShader.setUniform("texture_sampler", 0);
		for(GameObject obj : objs) {
			if(!obj.getMesh().isMeshBuilt()) {
				obj.getMesh().buildMesh();
			}
			Matrix4f worldMatrix = transform.getWorldMatrix(obj.getPosition(), obj.getRotation(), obj.getScale());
			blockShader.setUniform("worldMatrix", worldMatrix);
			obj.getMesh().render();
		}
		
		for(Runnable r : doLater) {
			r.run();
		}
		doLater.clear();
		
		ShaderProgram.unbind();
	}
	
	public void cleanup() {
		blockShader.cleanup();
	}
	
	public void runLater(Runnable call) {
		doLater.add(call);
	}
	
	private void clear() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
}