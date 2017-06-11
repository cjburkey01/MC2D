package com.cjburkey.mc2d.render;

import org.joml.Vector3f;

public final class Camera {
	
	private final Vector3f position;
	private final Vector3f rotation;
	
	public Camera() {
		position = new Vector3f();
		rotation = new Vector3f();
	}
	
	public Camera(Vector3f pos, Vector3f rot) {
		position = new Vector3f(pos);
		rotation = new Vector3f(rot);
	}
	
	public void move(float offsetX, float offsetY, float offsetZ) {
		if(offsetZ != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
			position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
		}
		if(offsetX != 0) {
			position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
			position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
		}
		position.y += offsetY;
	}
	
	public void rotate(float offsetX, float offsetY, float offsetZ) {
		rotation.x += offsetX;
		rotation.y += offsetY;
		rotation.z += offsetZ;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
}