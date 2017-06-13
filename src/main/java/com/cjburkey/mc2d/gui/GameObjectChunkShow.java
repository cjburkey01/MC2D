package com.cjburkey.mc2d.gui;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.chunk.ChunkData;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Mesh;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.render.Texture;

public class GameObjectChunkShow extends GameObject {
	
	private final ChunkData chunk;
	
	public GameObjectChunkShow(ChunkData chunk) {
		this.chunk = chunk;
		final float cs = (float) ChunkData.chunkSize / 2.0f;
		final float[] verts = new float[] {
				-cs, -cs, 0.0f,
				-cs, cs, 0.0f,
				cs, cs, 0.0f,
				cs, -cs, 0.0f
		};
		final float[] uvs = new float[] {
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f
		};
		final int[] tris = new int[] {
				0, 1, 2, 2, 3, 0
		};
		
		Vector3f pos = chunk.getWorldCoords();
		pos.z = -Transformation.NEAR;
		pos.x += ChunkData.scaleAndSize / 2;
		pos.y += ChunkData.scaleAndSize / 2;
		setMesh(new Mesh(true, verts, uvs, tris, Texture.getDefaultTexture()));
		setScale(ChunkData.scale);
		setPosition(pos);
	}
	
	public void render() {
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		super.render();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	public ChunkData getChunk() {
		return chunk;
	}
	
}