package com.cjburkey.mc2d.world;

import org.joml.Vector2i;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.block.ABlock;
import com.cjburkey.mc2d.block.Blocks;
import com.cjburkey.mc2d.chunk.ChunkData;

public final class Generation {
	
	private static final int grassHeight = 4;
	private static NoiseMaker noise;
	
	private static void init() {
		noise = new NoiseMaker(0);
		MC2D.getLogger().log("Seed: " + noise.getSeed());
	}
	
	public static void generateChunk(ChunkData chunk) {
		for(int x = 0; x < ChunkData.chunkSize; x ++) {
			for(int y = 0; y < ChunkData.chunkSize; y ++) {
				Vector2i block = chunk.getWorldCoordsForBlock(x, y);
				chunk.setBlock(x, y, getBlockForWorldPos(block.x, block.y));
			}
		}
	}
	
	private static ABlock getBlockForWorldPos(int x, int y) {
		ABlock block = getNoiseBlockForWorldPos(x, y);
		if(block == null) {
			for(int i = 1; i <= grassHeight; i ++) {
				ABlock at = getNoiseBlockForWorldPos(x, y - i);
				ABlock above = getNoiseBlockForWorldPos(x, y + 1);
				if(at != null && i < grassHeight) {
					return Blocks.blockDirt;
				} else if(at != null && i == grassHeight) {
					return ((above == null) ? Blocks.blockGrass : Blocks.blockDirt);
				}
			}
		}
		return block;
	}
	
	private static ABlock getNoiseBlockForWorldPos(int x, int y) {
		if(noise == null) {
			init();
		}
		double gened = noise.noise(x, y);
		if(gened >= 0.2d) {
			return Blocks.blockStone;
		}
		return null;
	}
	
}