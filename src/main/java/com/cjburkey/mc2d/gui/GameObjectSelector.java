package com.cjburkey.mc2d.gui;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.chunk.ChunkData;
import com.cjburkey.mc2d.module.core.CoreModule;
import com.cjburkey.mc2d.object.GameObject;
import com.cjburkey.mc2d.object.Mesh;
import com.cjburkey.mc2d.object.Transformation;
import com.cjburkey.mc2d.render.Renderer;
import com.cjburkey.mc2d.render.Texture;
import com.cjburkey.mc2d.world.World;

public class GameObjectSelector extends GameObject {
	
	public GameObjectSelector() {
		final float[] verts = new float[] {
				-0.5f, -0.5f, 0.0f,
				-0.5f, 0.5f, 0.0f,
				0.5f, 0.5f, 0.0f,
				0.5f, -0.5f, 0.0f
		};
		final float[] uvs = new float[] {
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f
		};
		final int[] tris = new int[] {
				0, 1, 2, 2, 3, 0
		};
		
		setMesh(new Mesh(true, verts, uvs, tris, Texture.getDefaultTexture()));
		setScale(ChunkData.scale);
	}
	
	public void render() {
		Vector2f cursor = CoreModule.instance.getInput().getCursorPosition();
		Vector3f world = Renderer.instance.getTransform().screenCoordsToWorldCoords(MC2D.INSTANCE.getWindow(),
				Renderer.instance.getCamera(), cursor.x, cursor.y);
		world.z = -Transformation.NEAR;
		Vector3f pos = World.getBlockWorldPos(new Vector3f(world));
		setPosition(pos);

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		super.render();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	public Vector2i getAtBlock() {
		return new Vector2i(World.worldCoordsToWorldBlock(getPosition()));
	}
	
}