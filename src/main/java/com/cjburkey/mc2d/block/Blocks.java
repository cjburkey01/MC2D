package com.cjburkey.mc2d.block;

import java.util.ArrayList;
import java.util.List;

public final class Blocks {
	
	private static final List<ABlock> blocks = new ArrayList<ABlock>();
	
	public static ABlock blockStone;
	public static ABlock blockDirt;
	public static ABlock blockGrass;
	
	public static void loadBlocks() {
		blockStone = new BlockBasic("block_stone");
		blockDirt = new BlockBasic("block_dirt");
		blockGrass = new BlockBasic("block_grass");
		
		blocks.add(blockStone);
		blocks.add(blockDirt);
		blocks.add(blockGrass);
	}
	
	public static ABlock[] getBlocks() {
		return blocks.toArray(new ABlock[blocks.size()]);
	}
	
}