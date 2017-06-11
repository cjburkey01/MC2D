package com.cjburkey.mc2d.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.cjburkey.mc2d.render.Camera;

public final class Transformation {
	
	private static final float FOV = (float) Math.toRadians(90.0d);
	private static final float NEAR_CLIP = 0.01f;
	private static final float FAR_CLIP = 1000.0f;
	
	private final Matrix4f projectionMatrix;
	private final Matrix4f viewMatrix;
	private final Matrix4f modelViewMatrix;
	
	public Transformation() {
		modelViewMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
	}

	public final Matrix4f getProjectionMatrix(float width, float height) {
		float aspectRatio = width / height;
		projectionMatrix.identity().perspective(FOV, aspectRatio, NEAR_CLIP, FAR_CLIP);
		return projectionMatrix;
	}
	
	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f pos = camera.getPosition();
		Vector3f rot = camera.getRotation();
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0));
		viewMatrix.translate(-pos.x, -pos.y, -pos.z);
		return viewMatrix;
	}
	
	public Matrix4f getModelViewMatrix(GameObject obj, Matrix4f viewMatrix) {
		Vector3f rot = obj.getRotation();
		modelViewMatrix.identity();
		modelViewMatrix.translate(obj.getPosition());
		modelViewMatrix.rotateX(rot.x);
		modelViewMatrix.rotateY(rot.y);
		modelViewMatrix.rotateZ(rot.z);
		modelViewMatrix.scale(obj.getScale());
		Matrix4f view = new Matrix4f(viewMatrix);
		return view.mul(modelViewMatrix);
	}

	
}