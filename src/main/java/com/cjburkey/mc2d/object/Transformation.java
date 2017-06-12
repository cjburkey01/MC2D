package com.cjburkey.mc2d.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.cjburkey.mc2d.render.Camera;
import com.cjburkey.mc2d.window.GLFWWindow;

public final class Transformation {
	
	private static final float NEAR = 0.01f;
	private static final float FAR = 1000.0f;
	public static float scale = 50.0f;
	
	private final Matrix4f projectionMatrix;
	private final Matrix4f viewMatrix;
	private final Matrix4f modelViewMatrix;
	
	public Transformation() {
		modelViewMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
	}

	public final Matrix4f getProjectionMatrix(float width, float height) {
		if(scale < 1.0f) scale = 1.0f;
		projectionMatrix.identity();
		projectionMatrix.ortho(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, NEAR, FAR);
		projectionMatrix.scale(width * (1.0f / scale), width * (1.0f / scale), 1.0f);
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
	
	public Vector3f screenCoordsToWorldCoords(GLFWWindow window, Camera cam, double xP, double yP) {
		float x = 2.0f * (float) xP / (float) window.getWindowSize().x - 1.0f;
		float y = 2.0f * (float) yP / (float) window.getWindowSize().y - 1.0f;
		
		Matrix4f projection = getProjectionMatrix(window.getWindowSize().x, window.getWindowSize().y);
		Matrix4f view = getViewMatrix(cam);
		Matrix4f projectionView = new Matrix4f(projection).mul(new Matrix4f(view));
		projectionView.invert();
		
		Vector3f point = new Vector3f(x, y, 0);
		return point.mulPosition(projectionView);
	}
	
}