package com.cjburkey.mc2d.object;

import com.cjburkey.mc2d.render.Camera;

public final class CameraController {
	
	private final Camera camera;
	private final float speed = 0.1f;
	
	public CameraController(Camera camera) {
		this.camera = camera;
	}
	
	public void left() {
		camera.getPosition().x -= speed;
	}
	
	public void right() {
		camera.getPosition().x += speed;
	}
	
	public void up() {
		camera.getPosition().y += speed;
	}
	
	public void down() {
		camera.getPosition().y -= speed;
	}
	
}