package com.cjburkey.mc2d.render;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.Utils;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Mesh;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.window.GLFWWindow;

public final class Renderer {
	
	public static Renderer instance;
	
	private final Queue<Runnable> doLater;
	private final Transformation transform;
	private final Camera camera;

	private ShaderProgram blockShader;
	
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
	}
	
	public void render(GameObject[] objs) {
		clear();
		GLFWWindow window = MC2D.INSTANCE.getWindow();

		blockShader.bind();
		Matrix4f viewMatrix = transform.getViewMatrix(camera);
		Matrix4f projectionMatrix = transform.getProjectionMatrix(window.getWindowSize().x, window.getWindowSize().y);
		blockShader.setUniform("projectionMatrix", projectionMatrix);
		blockShader.setUniform("texture_sampler", 0);
		for(GameObject obj : objs) {
			Mesh[] meshes = obj.getMesh();
			for(Mesh mesh : meshes) {
				if(!mesh.isOutline()) {
					if(!obj.isMeshBuilt()) {
						obj.generateMesh();
					}
					Matrix4f modelViewMatrix = transform.getModelViewMatrix(obj, viewMatrix);
					blockShader.setUniform("modelViewMatrix", modelViewMatrix);
					blockShader.setUniform("opacity", mesh.getOpacity());
					obj.render();
				}
			}
		}
		
		for(Runnable r : doLater) {
			r.run();
		}
		doLater.clear();
		
		ShaderProgram.unbind();
	}
	
	public void cleanup() {
		blockShader.cleanup();
		//solidShader.cleanup();
	}
	
	public void runLater(Runnable call) {
		doLater.add(call);
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	private void clear() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
}