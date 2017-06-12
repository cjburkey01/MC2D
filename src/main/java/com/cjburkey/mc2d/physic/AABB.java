package com.cjburkey.mc2d.physic;

import org.joml.Vector2f;

public final class AABB {
	
	private final Vector2f position;
	private final Vector2f size;
	
	public AABB(float x, float y, float w, float h) {
		position = new Vector2f(x, y);
		size = new Vector2f(w, h);
	}
	
	public AABB() {
		position = new Vector2f();
		size = new Vector2f();
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public void setSize(float w, float h) {
		size.x = w;
		size.y = h;
	}
	
	public Vector2f getPosition() {
		return new Vector2f(position);
	}
	
	public Vector2f getSize() {
		return new Vector2f(size);
	}
	
	public boolean intersecting(AABB other) {
		Vector2f otherPos = other.getPosition();
		Vector2f otherSize = other.getSize();
		boolean b1 = (position.x < otherPos.x + otherSize.x);
		boolean b2 = (position.x + size.x > otherPos.x);
		boolean b3 = (position.y < otherPos.y + otherSize.y);
		boolean b4 = (size.y + position.y > otherPos.y);
		return (b1 && b2 && b3 && b4);
	}
	
}