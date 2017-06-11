package com.cjburkey.mc2d.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	
	private static final float FOV = (float) Math.toRadians(90.0d);
	private static final float NEAR_CLIP = 0.01f;
	private static final float FAR_CLIP = 1000.0f;
	
	private final Matrix4f projectionMatrix;
	private final Matrix4f worldMatrix;
	
	public Transformation() {
		worldMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
	}

	public final Matrix4f getProjectionMatrix(float width, float height) {
		float aspectRatio = width / height;
		projectionMatrix.identity().perspective(FOV, aspectRatio, NEAR_CLIP, FAR_CLIP);
		return projectionMatrix;
	}
	
	public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
		worldMatrix.identity().translate(offset).rotateX((float)Math.toRadians(rotation.x)).
			rotateY((float)Math.toRadians(rotation.y)).rotateZ((float)Math.toRadians(rotation.z)).
			scale(scale);
		return worldMatrix;
	}

	
}