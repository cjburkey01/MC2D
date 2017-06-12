package com.cjburkey.mc2d.block;

public final class BlockBasic extends ABlock {
	
	private String name;
	
	public BlockBasic(String name) {
		this.name = name;
	}
	
	public String getUnlocalizedName() {
		return name;
	}
	
}