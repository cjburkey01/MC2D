package com.cjburkey.mc2d.chunk;

import java.util.concurrent.ThreadLocalRandom;
import com.cjburkey.mc2d.block.Blocks;
import com.cjburkey.mc2d.block.IBlock;

public final class ChunkData {
	
	public static final int chunkSize = 16;
	
	private IBlock blocks[][];
	
	public ChunkData() {
		blocks = new IBlock[chunkSize][chunkSize];
		for(int x = 0; x < chunkSize; x ++) {
			for(int y = 0; y < chunkSize; y ++) {
				int rand = ThreadLocalRandom.current().nextInt(0, 3);
				if(rand == 0) {
					setBlock(x, y, Blocks.blockStone);
				} else if(rand == 1 || rand == 2) {
					if(y == chunkSize - 1) {
						setBlock(x, y, Blocks.blockGrass);
					} else {
						setBlock(x, y, Blocks.blockDirt);
					}
				}
			}
		}
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
	
}