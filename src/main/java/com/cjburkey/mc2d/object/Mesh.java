package com.cjburkey.mc2d.object;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	
	private final int vertCount;
	private final float[] verts;
	private final float[] colors;
	private final int[] tris;
	
	private int vao;
	private int vbo;
	private int triVbo;
	private int colVbo;
	
	public Mesh(float[] verts, float[] colors, int[] tris) {
		vertCount = tris.length;
		this.verts = verts;
		this.colors = colors;
		this.tris = tris;
	}
	
	public void buildMesh() {
		FloatBuffer vertBuff = null;
		FloatBuffer colorBuff = null;
		IntBuffer triBuff = null;
		try {
			vertBuff = MemoryUtil.memAllocFloat(verts.length);
			vertBuff.put(verts).flip();
			colorBuff = MemoryUtil.memAllocFloat(colors.length);
			colorBuff.put(colors).flip();
			triBuff = MemoryUtil.memAllocInt(tris.length);
			triBuff.put(tris).flip();
			
			vao = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vao);
			
			vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuff, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			colVbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colVbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuff, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			triVbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, triVbo);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, triBuff, GL15.GL_STATIC_DRAW);
			
			GL30.glBindVertexArray(0);
		} finally {
			if(vertBuff != null) {
				MemoryUtil.memFree(vertBuff);
			}
			
			if(triBuff != null) {
				MemoryUtil.memFree(triBuff);
			}
			
			if(colorBuff != null) {
				MemoryUtil.memFree(colorBuff);
			}
		}
	}
	
	public void render() {
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawElements(GL11.GL_TRIANGLES, vertCount, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	public void cleanup() {
		GL20.glDisableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(colVbo);
		GL15.glDeleteBuffers(triVbo);
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vao);
	}
	
}