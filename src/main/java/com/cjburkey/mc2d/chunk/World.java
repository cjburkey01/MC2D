package com.cjburkey.mc2d.chunk;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.mc2d.module.core.CoreModule;

public class World {
	
	private final Queue<ChunkData> generated = new ConcurrentLinkedQueue<>();
	private final Queue<GameObjectChunk> rendered = new ConcurrentLinkedQueue<>();
	
	// -- GENERATE -- //
	
	public void generateChunksAround(Vector3f pos, int radius) {
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
	
	// -- RENDER -- //
	
	public void renderChunksAround(Vector3f around, int radius) {
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
	
	public void deRenderChunksAround(Vector3f around, int radius) {
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
	
	private void renderChunkAt(Vector2i vec) {
		renderChunkAt(vec.x, vec.y);
	}
	
	private void renderChunkAt(int x, int y) {
		ChunkData at = getGeneratedChunkAt(x, y);
		if(at != null) {
			GameObjectChunk object = new GameObjectChunk(at);
			rendered.add(object);
			CoreModule.instance.addGameObject(object);
		}
	}
	
	private void deRenderChunk(GameObjectChunk chunk) {
		rendered.remove(chunk);
		chunk.destroy();
	}
	
	public static Vector2i worldCoordsToChunk(Vector3f world) {
		int x = (int) Math.floor(world.x / ChunkData.scaleAndSize);
		int y = (int) Math.floor(world.y / ChunkData.scaleAndSize);
		return new Vector2i(x, y);
	}
	
}