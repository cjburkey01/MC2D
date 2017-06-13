package com.cjburkey.mc2d.chunk;

import com.cjburkey.mc2d.module.core.CoreModule;
import com.cjburkey.mc2d.object.GameObject;

public final class GameObjectChunk extends GameObject {
	
	private static final boolean doFading = true;
	private static final int fadeTicks = 60;
	
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
			if(doFading) {
				if(fadeIn) {
					fadeOut = false;
					if(mesh.getOpacity() < 1.0f) {
						mesh.setOpacity(mesh.getOpacity() + (1.0f / (float) fadeTicks));
					} else {
						mesh.setOpacity(1.0f);
						fadeIn = false;
					}
				} else if(fadeOut) {
					fadeIn = false;
					if(mesh.getOpacity() > 0.0f) {
						mesh.setOpacity(mesh.getOpacity() - (1.0f / (float) fadeTicks));
					} else {
						mesh.setOpacity(0.0f);
						fadeOut = false;
						remove();
					}
				}
			} else {
				if(fadeIn) {
					fadeIn = false;
				} else if(fadeOut) {
					fadeOut = false;
					remove();
				}
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