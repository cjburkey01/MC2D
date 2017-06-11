package com.cjburkey.mc2d.block;

import java.util.ArrayList;
import java.util.List;

public class Blocks {
	
	private static final List<IBlock> blocks = new ArrayList<IBlock>();
	
	public static IBlock blockBasic;
	
	public static void loadBlocks() {
		blockBasic = new BlockBasic("block_stone");
		
		blocks.add(blockBasic);
	}
	
	public static IBlock[] getBlocks() {
		return blocks.toArray(new IBlock[blocks.size()]);
	}
	
}