package com.cjburkey.mc2d.render;

import org.joml.Vector3f;

public class Camera {
	
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
	
	// TODO: TRANSLATIONS
	public void translate() {
		
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
}