package com.cjburkey.mc2d.block;

import java.util.ArrayList;
import java.util.List;

public final class Blocks {
	
	private static final List<IBlock> blocks = new ArrayList<IBlock>();
	
	public static IBlock blockStone;
	public static IBlock blockDirt;
	public static IBlock blockGrass;
	
	public static void loadBlocks() {
		blockStone = new BlockBasic("block_stone");
		blockDirt = new BlockBasic("block_dirt");
		blockGrass = new BlockBasic("block_grass");
		
		blocks.add(blockStone);
		blocks.add(blockDirt);
		blocks.add(blockGrass);
	}
	
	public static IBlock[] getBlocks() {
		return blocks.toArray(new IBlock[blocks.size()]);
	}
	
}