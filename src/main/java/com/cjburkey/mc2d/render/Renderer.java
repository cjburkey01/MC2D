package com.cjburkey.mc2d.render;

import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.core.Utils;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Mesh;

public class Renderer {

	private final GameObject triangle;
	private ShaderProgram blockShader;
	
	public Renderer() {
		triangle = new GameObject(new Mesh(new float[] {
				-0.5f, 0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.5f, 0.5f, 0.0f
		}, new float[] {
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f
		}, new int[] {
				0, 1, 3, 3, 1, 2
		}));
	}
	
	public void init() {
		blockShader = new ShaderProgram();
		blockShader.createVertexShader(Utils.readResourceAsString("mc2d:shader/block/block.vs"));
		blockShader.createFragmentShader(Utils.readResourceAsString("mc2d:shader/block/block.fs"));
		blockShader.link();
		triangle.init();
	}
	
	public void render() {
		clear();
		blockShader.bind();
		
		triangle.render();
		
		ShaderProgram.unbind();
	}
	
	public void cleanup() {
		triangle.cleanup();
		blockShader.cleanup();
	}
	
	private void clear() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
}