package com.cjburkey.mc2d.chunk;

import org.joml.Vector2i;
import com.cjburkey.mc2d.block.Blocks;
import com.cjburkey.mc2d.block.IBlock;
import com.cjburkey.mc2d.world.SimplexNoise;

public final class Generation {
	
	private static final double scale = 20.0d;
	private static final double yBase = 5.0d;
	private static final double amplitude = 7.5d;
	
	public static void generateChunk(ChunkData chunk) {
		for(int x = 0; x < ChunkData.chunkSize; x ++) {
			for(int y = 0; y < ChunkData.chunkSize; y ++) {
				Vector2i block = chunk.getWorldCoordsForBlock(x, y);
				chunk.setBlock(x, y, getBlockForWorldPos(block.x, block.y));
			}
		}
	}
	
	
	
	private static IBlock getBlockForWorldPos(int x, int y) {
		IBlock block = getNoiseBlockForWorldPos(x, y);
		if(block == null) {
			for(int i = 1; i <= 5; i ++) {
				IBlock at = getNoiseBlockForWorldPos(x, y - i);
				if(at != null && i < 5) {
					return Blocks.blockDirt;
				} else if(at != null && i == 5) {
					return Blocks.blockGrass;
				}
			}
		}
		return block;
	}
	
	private static IBlock getNoiseBlockForWorldPos(int x, int y) {
		double noise = noise(x, y);
		if(noise >= 0.2d) {
			return Blocks.blockStone;
		}
		return null;
	}
	
	private static double noise(double x, double y) {
		double val = SimplexNoise.noise(x / scale, y / scale);
		val += (yBase - y) / amplitude;
		return val;
	}
	
}