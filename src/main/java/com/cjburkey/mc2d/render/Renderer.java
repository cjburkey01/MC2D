package com.cjburkey.mc2d.render;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.Utils;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.window.GLFWWindow;

public final class Renderer {
	
	public static Renderer instance;
	
	private final Queue<Runnable> doLater;
	private final Transformation transform;
	private final Camera camera;
	
	private ShaderProgram blockShader;
	private ShaderProgram basicShader;
	
	public Renderer() {
		instance = this;
		doLater = new ConcurrentLinkedQueue<>();
		transform = new Transformation();
		camera = new Camera();
	}
	
	public void init() {
		blockShader = new ShaderProgram();
		blockShader.createVertexShader(Utils.readResourceAsString("mc2d:shader/block/block.vs"));
		blockShader.createFragmentShader(Utils.readResourceAsString("mc2d:shader/block/block.fs"));
		blockShader.link();
		blockShader.createUniform("projectionMatrix");
		blockShader.createUniform("modelViewMatrix");
		blockShader.createUniform("texture_sampler");
		blockShader.createUniform("opacity");

		basicShader = new ShaderProgram();
		basicShader.createVertexShader(Utils.readResourceAsString("mc2d:shader/basic/basic.vs"));
		basicShader.createFragmentShader(Utils.readResourceAsString("mc2d:shader/basic/basic.fs"));
		basicShader.link();
		basicShader.createUniform("projectionMatrix");
		basicShader.createUniform("modelViewMatrix");
		basicShader.createUniform("color");
	}
	
	public void render(GameObject[] objs) {
		clear();
		GLFWWindow window = MC2D.INSTANCE.getWindow();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
		
		blockShader.bind();
		Matrix4f viewMatrix = transform.getViewMatrix(camera);
		Matrix4f projectionMatrix = transform.getProjectionMatrix(window.getWindowSize().x, window.getWindowSize().y);
		blockShader.setUniform("projectionMatrix", projectionMatrix);
		blockShader.setUniform("texture_sampler", 0);
		ShaderProgram.unbind();
		
		basicShader.bind();
		basicShader.setUniform("projectionMatrix", projectionMatrix);
		ShaderProgram.unbind();
		
		for(GameObject obj : objs) {
			if(obj.getMesh() != null) {
				if(obj.getMesh().useBasicShader()) {
					basicShader.bind();
					if(!obj.isMeshBuilt()) {
						obj.generateMesh();
					}
					Matrix4f modelViewMatrix = transform.getModelViewMatrix(obj, viewMatrix);
					basicShader.setUniform("modelViewMatrix", modelViewMatrix);
					basicShader.setUniform("color", new Vector3f(1.0f, 1.0f, 1.0f));
					obj.render();
				} else {
					blockShader.bind();
					if(!obj.isMeshBuilt()) {
						obj.generateMesh();
					}
					Matrix4f modelViewMatrix = transform.getModelViewMatrix(obj, viewMatrix);
					blockShader.setUniform("modelViewMatrix", modelViewMatrix);
					blockShader.setUniform("opacity", obj.getMesh().getOpacity());
					obj.render();
				}
				ShaderProgram.unbind();
			}
		}
		ShaderProgram.unbind();
		
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
	
	public Camera getCamera() {
		return camera;
	}
	
	public Transformation getTransform() {
		return transform;
	}
	
	public ShaderProgram getBlockShader() {
		return blockShader;
	}
	
	private void clear() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
}