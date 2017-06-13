package com.cjburkey.mc2d.chunk;

import com.cjburkey.mc2d.module.core.CoreModule;
import com.cjburkey.mc2d.object.GameObject;

public final class GameObjectChunk extends GameObject {
	
	private ChunkData chunk;
	
	private boolean fadeIn = false;
	private boolean fadeOut = false;
	
	public GameObjectChunk(ChunkData chunk) {
		super();
		this.chunk = chunk;
		createMesh();
		setScale(ChunkData.scale);
		setPosition(chunk.getWorldCoords());
	}
	
	public void createMesh() {
		setMesh(ChunkMeshCreator.generateChunkMesh(chunk, CoreModule.instance.getTextures()));
	}
	
	public void render() {
		if(mesh != null) {
			mesh.setOpacity(1.0f);
			if(fadeIn) {
				fadeIn = false;
			} else if(fadeOut) {
				fadeOut = false;
				remove();
			}
			super.render();
		}
	}
	
	public void generateMesh(boolean fadeIn) {
		super.generateMesh();
		this.fadeIn = fadeIn;
		if(fadeIn) {
			mesh.setOpacity(0.0f);
		}
	}
	
	public void generateMesh() {
		generateMesh(true);
	}
	
	public ChunkData getChunk() {
		return chunk;
	}
	
	public void destroy() {
		fadeIn = false;
		fadeOut = true;
	}
	
	private void remove() {
		CoreModule.instance.removeGameObject(this);
	}
	
}