package com.cjburkey.mc2d.object;

public class GameObject {
	
	private final Mesh mesh;
	
	public GameObject(Mesh mesh) {
		this.mesh = mesh;
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
	
}