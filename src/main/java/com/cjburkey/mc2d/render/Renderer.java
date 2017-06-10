package com.cjburkey.mc2d.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.Utils;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.window.GLFWWindow;

public class Renderer {

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
	
	private int x = 0;
	public void render(GameObject[] objs) {
		x ++;
		if(x >= 360) {
			x = 0;
		}
		clear();
		GLFWWindow window = MC2D.INSTANCE.getWindow();
		blockShader.bind();
		
		Matrix4f projectionMatrix = transform.getProjectionMatrix(window.getWindowSize().x, window.getWindowSize().y);
		blockShader.setUniform("projectionMatrix", projectionMatrix);
		for(GameObject obj : objs) {
			obj.setRotation(x, x, x);
			System.out.println(x);
			Matrix4f worldMatrix = transform.getWorldMatrix(obj.getPosition(), obj.getRotation(), obj.getScale());
			blockShader.setUniform("worldMatrix", worldMatrix);
			obj.getMesh().render();
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