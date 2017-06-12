package com.cjburkey.mc2d.object;

import org.joml.Vector3f;

public class GameObject {
	
	protected Mesh mesh;
	private final Vector3f position;
	private float scale;
	private final Vector3f rotation;
	
	public GameObject() {
		mesh = null;
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}

	public GameObject(Mesh mesh) {
		this();
		this.mesh = mesh;
	}
	
	public void render() {
		if(mesh != null) {
			mesh.render();
		}
	}
	
	public void cleanup() {
		if(mesh != null) {
			mesh.cleanup();
		}
	}
	
	public void generateMesh() {
		if(mesh != null) {
			mesh.buildMesh();
		}
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

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}
	
	public void setMesh(Mesh mesh) {
		cleanup();
		this.mesh = mesh;
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
		if(mesh != null) {
			return mesh.isMeshBuilt();
		}
		return false;
	}
	
}