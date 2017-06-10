package com.cjburkey.mc2d.render;

import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.Utils;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.window.GLFWWindow;

public class Renderer {
	
	private static final float FOV = (float) Math.toRadians(90.0d);
	private static final float NEAR_CLIP = 0.01f;
	private static final float FAR_CLIP = 1000.0f;

	private ShaderProgram blockShader;
	private final Transformation transform;
	
	public Renderer() {
		transform = new Transformation();
	}
	
	public void init() {
		blockShader = new ShaderProgram();
		blockShader.createVertexShader(Utils.readResourceAsString("mc2d:shader/block/block.vs"));
		blockShader.createFragmentShader(Utils.readResourceAsString("mc2d:shader/block/block.fs"));
		blockShader.link();
		
		blockShader.createUniform("projectionMatrix");
		blockShader.createUniform("worldMatrix");
	}
	
	public void render(GameObject[] objs) {
		clear();
		GLFWWindow window = MC2D.INSTANCE.getWindow();
		blockShader.bind();
		
		blockShader.setUniform("projectionMatrix", transform.getProjectionMatrix(FOV, window.getWindowSize().x, window.getWindowSize().y, NEAR_CLIP, FAR_CLIP));
		for(GameObject obj : objs) {
			blockShader.setUniform("worldMatrix", transform.getWorldMatrix(obj.getPosition(), obj.getRotation(), obj.getScale()));
			obj.render();
		}
		
		ShaderProgram.unbind();
	}
	
	public void cleanup() {
		blockShader.cleanup();
	}
	
	private void clear() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
}