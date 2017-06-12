package com.cjburkey.mc2d.chunk;

import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.mc2d.block.IBlock;

public final class ChunkData {
	
	public static final float scale = 5.0f;
	public static final int chunkSize = 16;
	public static final float scaleAndSize = scale * chunkSize;
	public static final float chunkZ = -0.05f;
	
	private IBlock blocks[][];
	private final Vector2i chunkPos;
	
	public ChunkData(int cx, int cy) {
		blocks = new IBlock[chunkSize][chunkSize];
		chunkPos = new Vector2i(cx, cy);
	}
	
	public void setBlock(int x, int y, IBlock block) {
		if(inChunk(x, y)) {
			blocks[x][y] = block;
		}
	}
	
	public void removeBlock(int x, int y) {
		setBlock(x, y, null);
	}
	
	public boolean inChunk(int x, int y) {
		return ((x >= 0) && (x < chunkSize) && (y >= 0) && (y < chunkSize));
	}
	
	public IBlock getBlock(int x, int y) {
		if(inChunk(x, y)) {
			return blocks[x][y];
		}
		return null;
	}
	
	public Vector2i getChunkCoords() {
		return new Vector2i(chunkPos);
	}
	
	public Vector3f getWorldCoords() {
		return new Vector3f(chunkPos.x * scaleAndSize, chunkPos.y * scaleAndSize, chunkZ);
	}
	
	public Vector2i getWorldCoordsForBlock(int x, int y) {
		return new Vector2i(chunkPos.x * chunkSize + x, chunkPos.y * chunkSize + y);
	}
	
}