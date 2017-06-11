package com.cjburkey.mc2d.object;

import com.cjburkey.mc2d.chunk.ChunkData;
import com.cjburkey.mc2d.chunk.MeshChunk;
import com.cjburkey.mc2d.module.core.CoreModule;

public class GameObjectChunk extends GameObject {
	
	private ChunkData chunk;
	
	public GameObjectChunk(ChunkData chunk) {
		super(MeshChunk.generateChunkMesh(chunk, CoreModule.instance.getTextures()));
		this.chunk = chunk;
		setScale(ChunkData.scale);
		setPosition(chunk.getWorldCoords());
	}
	
	public ChunkData getChunk() {
		return chunk;
	}
	
	public void render() {
		super.render();
		
	}
	
}