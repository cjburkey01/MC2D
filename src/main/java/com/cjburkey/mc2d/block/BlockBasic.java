package com.cjburkey.mc2d.block;

public final class BlockBasic implements IBlock {
	
	private String name;
	
	public BlockBasic(String name) {
		this.name = name;
	}
	
	public String getUnlocalizedName() {
		return name;
	}
	
}