package com.cjburkey.mc2d.object;

import org.joml.Vector3f;

public class GameObject {
	
	protected final Mesh mesh;
	private final Vector3f position;
	private float scale;
	private final Vector3f rotation;

	public GameObject(Mesh mesh) {
		this.mesh = mesh;
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	public void setPosition(Vector3f pos) {
		position.x = pos.x;
		position.y = pos.y;
		position.z = pos.z;
	}
	
	public void render() {
		mesh.render();
	}
	
	public void cleanup() {
		mesh.cleanup();
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}
	
	public void generateMesh() {
		mesh.buildMesh();
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getScale() {
		return scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}
	
	public Mesh[] getMesh() {
		return new Mesh[] { mesh };
	}
	
	public boolean isMeshBuilt() {
		return mesh.isMeshBuilt();
	}
	
}