package com.cjburkey.mc2d.chunk;

import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.module.core.CoreModule;
import com.cjburkey.mc2d.object.GameObject;

public class GameObjectChunk extends GameObject {
	
	private static final boolean doFading = true;
	private static final int fadeTicks = 60;
	
	private ChunkData chunk;
	
	private boolean fadeIn = false;
	private boolean fadeOut = false;
	
	public GameObjectChunk(ChunkData chunk) {
		super(ChunkMeshGenerator.generateChunkMesh(chunk, CoreModule.instance.getTextures()));
		this.chunk = chunk;
		setScale(ChunkData.scale);
		setPosition(chunk.getWorldCoords());
	}
	
	public void render() {
		if(doFading) {
			if(fadeIn) {
				if(mesh.getOpacity() < 1.0f) {
					mesh.setOpacity(mesh.getOpacity() + (1.0f / (float) fadeTicks));
				} else {
					mesh.setOpacity(1.0f);
					fadeIn = false;
				}
			} else if(fadeOut) {
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
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		super.render();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void generateMesh() {
		super.generateMesh();
		fadeIn = true;
		mesh.setOpacity(0.0f);
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