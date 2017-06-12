package com.cjburkey.mc2d.block;

import org.joml.Vector2i;
import com.cjburkey.mc2d.physic.AABB;

public abstract class ABlock {
	
	public abstract String getUnlocalizedName();
	
	public AABB getAABBCollisionBox(BlockState blockState) {
		Vector2i world = blockState.getPosition();
		return new AABB(world.x, world.y, world.x + 1, world.y + 1);
	}
	
}