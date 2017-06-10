package com.cjburkey.mc2d.object;

import org.joml.Vector3f;

public class GameObject {
	
	private final Vector3f position;
	private final Vector3f rotation;
	private final Mesh mesh;
	private float scale;
	
	public GameObject(Mesh mesh) {
		this.mesh = mesh;
		position = new Vector3f();
		rotation = new Vector3f();
		scale = 1.0f;
	}
	
	public void init() {
		mesh.buildMesh();
	}
	
	public void render() {
		mesh.render();
	}
	
	public void cleanup() {
		mesh.cleanup();
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void setRotation(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public float getScale() {
		return scale;
	}
	
}