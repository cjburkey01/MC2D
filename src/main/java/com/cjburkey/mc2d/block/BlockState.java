package com.cjburkey.mc2d.block;

import org.joml.Vector2i;
import com.cjburkey.mc2d.chunk.ChunkData;

public final class BlockState {

	private final ChunkData chunk;
	private final ABlock block;
	private final Vector2i worldPos;
	
	public BlockState(ChunkData chunk, ABlock block, Vector2i worldPos) {
		this.chunk = chunk;
		this.block = block;
		this.worldPos = worldPos;
	}
	
	public ChunkData getChunk() {
		return chunk;
	}
	
	public ABlock getBlock() {
		return block;
	}
	
	public Vector2i getPosition() {
		return new Vector2i(worldPos);
	}
	
	public String toString() {
		return "State for: " + block;
	}
	
}