package com.cjburkey.mc2d.block;

import org.joml.Vector2i;

public final class BlockState {
	
	private final ABlock block;
	private final Vector2i worldPos;
	
	public BlockState(ABlock inBlock, Vector2i worldPos) {
		block = inBlock;
		this.worldPos = worldPos;
	}
	
	public ABlock getBlock() {
		return block;
	}
	
	public Vector2i getPosition() {
		return new Vector2i(worldPos);
	}
	
}