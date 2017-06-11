package com.cjburkey.mc2d.object;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.mc2d.render.Texture;

public final class Mesh {
	
	private final int vertCount;
	private final float[] verts;
	private final float[] uvs;
	private final int[] tris;
	
	private boolean built = false;
	private int vao;
	private int vbo;
	private int triVbo;
	private int uvVbo;
	private Texture texture;
	
	public Mesh(float[] verts, float[] uvs, int[] tris, Texture texture) {
		vertCount = tris.length;
		this.verts = verts;
		this.uvs = uvs;
		this.tris = tris;
		this.texture = texture;
	}
	
	public void buildMesh() {
		built = true;
		FloatBuffer vertBuff = null;
		FloatBuffer uvBuff = null;
		IntBuffer triBuff = null;
		try {
			vertBuff = MemoryUtil.memAllocFloat(verts.length);
			vertBuff.put(verts).flip();
			uvBuff = MemoryUtil.memAllocFloat(uvs.length);
			uvBuff.put(uvs).flip();
			triBuff = MemoryUtil.memAllocInt(tris.length);
			triBuff.put(tris).flip();
			
			vao = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vao);
			
			vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuff, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			uvVbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvVbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, uvBuff, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			triVbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, triVbo);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, triBuff, GL15.GL_STATIC_DRAW);
			
			GL30.glBindVertexArray(0);
			
			texture.load();
		} finally {
			if(vertBuff != null) {
				MemoryUtil.memFree(vertBuff);
			}
			
			if(triBuff != null) {
				MemoryUtil.memFree(triBuff);
			}
			
			if(uvBuff != null) {
				MemoryUtil.memFree(uvBuff);
			}
		}
	}
	
	public void render() {
		if(texture != null) {
			texture.activate();
		}
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
		GL15.glDeleteBuffers(uvVbo);
		GL15.glDeleteBuffers(triVbo);
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vao);
		if(texture != null) {
			texture.cleanup();
		}
	}
	
	public boolean isMeshBuilt() {
		return built;
	}
	
}