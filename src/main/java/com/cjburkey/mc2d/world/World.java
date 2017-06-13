package com.cjburkey.mc2d.world;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.mc2d.block.BlockState;
import com.cjburkey.mc2d.chunk.ChunkData;
import com.cjburkey.mc2d.chunk.GameObjectChunk;
import com.cjburkey.mc2d.gui.GameObjectChunkShow;
import com.cjburkey.mc2d.module.core.CoreModule;
import com.cjburkey.mc2d.render.Camera;
import com.cjburkey.mc2d.render.Renderer;

public final class World {
	
	/*
	 * COORDS:
	 *   3D WORLD:				The OpenGL coords of an object.
	 *   CHUNK COORDS:			The position of an object in the grid of chunks.
	 *   BLOCK COORDS:			The position of a block relative to its parent chunk.
	 *   BLOCK WORLD COORDS:	The position of a block relative to 0,0 of the world.
	 * 
	 */
	
	private final Queue<ChunkData> generated = new ConcurrentLinkedQueue<>();
	private final Queue<GameObjectChunkShow> outlines = new ConcurrentLinkedQueue<>();
	private final Queue<GameObjectChunk> rendered = new ConcurrentLinkedQueue<>();
	private final double actionRate = 500000000.0d;	// 0.5 second
	private long lastAction;
	
	private boolean running = false;
	
	public void startGenerating(Camera cam, int radius) {
		Thread thread = new Thread(() -> {
			running = true;
			while(running) {
				long now = System.nanoTime();
				if(now - lastAction >= actionRate) {
					lastAction = now;
					doChunksAround(cam.getPosition(), radius);
				}
			}
		});
		thread.start();
	}
	
	public void cleanup() {
		stopGenerating();
		for(ChunkData chunk : generated) {
			remove(chunk);
		}
		for(GameObjectChunk chunk : rendered) {
			deRenderChunk(chunk);
		}
	}
	
	private void stopGenerating() {
		running = false;
	}
	
	private void doChunksAround(Vector3f pos, int radius) {
		generateChunksAround(pos, radius);
		reRenderChunksAround();
		renderChunksAround(pos, radius);
		deRenderChunksAround(pos, radius);
	}
	
	// -- GENERATE -- //
	
	private void generateChunksAround(Vector3f pos, int radius) {
		Vector2i chunk = worldCoordsToChunk(pos);
		for(int x = chunk.x - radius; x <= chunk.x + radius; x ++) {
			for(int y = chunk.y - radius; y <= chunk.y + radius; y ++) {
				generateChunk(x, y);
			}
		}
	}
	
	private ChunkData getGeneratedChunkAt(int x, int y) {
		for(ChunkData chunk : generated) {
			Vector2i pos = chunk.getChunkCoords();
			if(pos.x == x && pos.y == y) {
				return chunk;
			}
		}
		return null;
	}
	
	private void generateChunk(int x, int y) {
		if(getGeneratedChunkAt(x, y) == null) {
			ChunkData chunk = new ChunkData(x, y);
			Generation.generateChunk(chunk);
			generated.add(chunk);
		}
	}
	
	public ChunkData getChunkAtWorldPos(Vector3f world) {
		Vector2i chunk = worldCoordsToChunk(world);
		return getGeneratedChunkAt(chunk.x, chunk.y);
	}
	
	public BlockState getBlockAtWorldPos(Vector3f world) {
		ChunkData at = getChunkAtWorldPos(world);
		if(at != null) {
			Vector2i block = getInChunkBlockCoordsFromWorld(world);
			return at.getBlockState(block.x, block.y);
		}
		return null;
	}
	
	public Vector2i getInChunkBlockCoordsFromWorld(Vector3f world) {
		ChunkData at = getChunkAtWorldPos(world);
		Vector2i worldBlock = worldCoordsToWorldBlock(world);
		Vector2i worldChunk = at.getChunkWorldCoords();
		Vector2i blockPos = worldBlock.sub(worldChunk);
		return blockPos;
	}
	
	/*private void deGenerateChunk(int x, int y) {
		ChunkData chunk = getGeneratedChunkAt(x, y);
		if(chunk != null) {
			remove(chunk);
		}
	}*/
	
	private void remove(ChunkData chunk) {
		generated.remove(chunk);
	}
	
	// -- RENDER -- //
	
	private void reRenderChunksAround() {
		for(GameObjectChunk obj : rendered) {
			obj.getChunk().refresh(this);
		}
	}
	
	private void renderChunksAround(Vector3f around, int radius) {
		around = new Vector3f(around);
		around.z = ChunkData.chunkZ;
		for(ChunkData chunk : generated) {
			Vector3f pos = chunk.getWorldCoords().add(ChunkData.scaleAndSize / 2, ChunkData.scaleAndSize / 2, 0.0f);
			if(pos.distance(around) <= radius * ChunkData.scaleAndSize) {
				if(getRenderedChunkAt(chunk.getChunkCoords().x, chunk.getChunkCoords().y) == null) {
					renderChunkAt(chunk.getChunkCoords());
				}
			}
		}
	}
	
	private void deRenderChunksAround(Vector3f around, int radius) {
		around = new Vector3f(around);
		around.z = ChunkData.chunkZ;
		for(GameObjectChunk object : rendered) {
			Vector3f chunkPos = new Vector3f(object.getPosition());
			chunkPos.add(ChunkData.scaleAndSize / 2, ChunkData.scaleAndSize / 2, 0.0f);
			if(chunkPos.distance(around) > radius * ChunkData.scaleAndSize) {
				deRenderChunk(object);
			}
		}
	}
	
	private GameObjectChunk getRenderedChunkAt(int x, int y) {
		for(GameObjectChunk chunk : rendered) {
			Vector2i pos = chunk.getChunk().getChunkCoords();
			if(pos.x == x && pos.y == y) {
				return chunk;
			}
		}
		return null;
	}
	
	public void reRenderChunk(int x, int y) {
		Renderer.instance.runLater(() -> {
			deRenderChunk(getRenderedChunkAt(x, y));
			renderChunkAt(x, y);
		});
	}
	
	private void renderChunkAt(Vector2i vec) {
		renderChunkAt(vec.x, vec.y);
	}
	
	private void renderChunkAt(int x, int y) {
		ChunkData at = getGeneratedChunkAt(x, y);
		if(at != null) {
			GameObjectChunk object = new GameObjectChunk(at);
			rendered.add(object);
			CoreModule.instance.addGameObject(object);
			
			GameObjectChunkShow chunkShow = new GameObjectChunkShow(at);
			outlines.add(chunkShow);
			CoreModule.instance.addGameObject(chunkShow);
		}
	}
	
	private void deRenderChunk(GameObjectChunk chunk) {
		if(chunk != null) {
			ChunkData chunkData = chunk.getChunk();
			for(GameObjectChunkShow gocs : outlines) {
				if(gocs.getChunk().equals(chunkData)) {
					CoreModule.instance.removeGameObject(gocs);
					outlines.remove(gocs);
				}
			}
			
			rendered.remove(chunk);
			chunk.destroy();
		}
	}
	
	public static Vector2i worldCoordsToChunk(Vector3f world) {
		int x = (int) Math.floor(world.x / ChunkData.scaleAndSize);
		int y = (int) Math.floor(world.y / ChunkData.scaleAndSize);
		return new Vector2i(x, y);
	}
	
	public static Vector2i worldCoordsToWorldBlock(Vector3f world) {
		int x = (int) Math.floor(world.x / ChunkData.scale);
		int y = (int) Math.floor(world.y / ChunkData.scale);
		return new Vector2i(x, y);
	}
	
	public static Vector3f getBlockWorldPos(Vector3f pos) {
		float scale = ChunkData.scale;
		float blockX = (float) Math.floor(pos.x / scale) * scale + scale / 2.0f;
		float blockY = (float) Math.floor(pos.y / scale) * scale + scale / 2.0f;
		return new Vector3f(blockX, blockY, ChunkData.chunkZ);
	}
	
}