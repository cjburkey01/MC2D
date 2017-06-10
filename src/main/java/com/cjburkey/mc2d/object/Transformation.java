package com.cjburkey.mc2d.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	
	private final Matrix4f projectionMatrix;
	private final Matrix4f worldMatrix;
	
	public Transformation() {
		projectionMatrix = new Matrix4f();
		worldMatrix = new Matrix4f();
	}
	
	public Matrix4f getProjectionMatrix(float fov, int width, int height, float near, float far) {
		float aspect = (float) width / (float) height;
		Matrix4f out = new Matrix4f(projectionMatrix);
		out.identity();
		out.perspective(fov, aspect, near, far);
		return out;
	}
	
	public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
		Matrix4f out = new Matrix4f(worldMatrix);
		out.identity();
		out.translate(offset);
		out.rotateX((float) Math.toRadians(rotation.x));
		out.rotateY((float) Math.toRadians(rotation.y));
		out.rotateZ((float) Math.toRadians(rotation.z));
		out.scale(scale);
		return out;
	}
	
}