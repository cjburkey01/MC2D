package com.cjburkey.mc2d.chunk;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.mc2d.block.IBlock;
import com.cjburkey.mc2d.core.Utils;
import com.cjburkey.mc2d.object.Mesh;
import com.cjburkey.mc2d.render.TextureAtlas;

public final class MeshChunk {
	
	public static final float blockSize = 1.0f;
	
	public static Mesh generateChunkMesh(ChunkData chunk, TextureAtlas atlas) {
		return buildMesh(chunk, atlas);
	}
	
	private static Mesh buildMesh(ChunkData chunk, TextureAtlas atlas) {
		List<Vector3f> verts = new ArrayList<>();
		List<Vector2f> uvs = new ArrayList<>();
		List<Integer> tris = new ArrayList<>();
		for(int x = 0; x < ChunkData.chunkSize; x ++) {
			for(int y = 0; y < ChunkData.chunkSize; y ++) {
				if(chunk.getBlock(x, y) != null) {
					addBlock(chunk.getBlock(x, y), x, y, verts, uvs, tris, atlas);
				}
			}
		}
		return new Mesh(Utils.vector3fToFloats(verts), Utils.vector2fToFloats(uvs), Utils.intToInts(tris), atlas.getTexture());
	}
	
	private static void addBlock(IBlock block, int x, int y, List<Vector3f> verts, List<Vector2f> uvs, List<Integer> tris, TextureAtlas atlas) {
		final boolean rev = false;
		final int index = verts.size();
		Vector3f corner = new Vector3f(x, y, 0.0f);
		
		Vector3f i0 = new Vector3f();
		Vector3f i1 = new Vector3f();
		Vector3f i2 = new Vector3f();
		Vector3f i3 = new Vector3f();
		
		i0.add(corner);
		i1.add(corner).add(new Vector3f(0.0f, 1.0f, 0.0f));
		i2.add(corner).add(new Vector3f(1.0f, 1.0f, 0.0f));
		i3.add(corner).add(new Vector3f(1.0f, 0.0f, 0.0f));
		
		verts.add(i0);
		verts.add(i1);
		verts.add(i2);
		verts.add(i3);
		
		if(rev) {
			tris.add(index + 0);
			tris.add(index + 1);
			tris.add(index + 2);
			tris.add(index + 2);
			tris.add(index + 3);
			tris.add(index + 0);
		} else {
			tris.add(index + 1);
			tris.add(index + 0);
			tris.add(index + 2);
			tris.add(index + 3);
			tris.add(index + 2);
			tris.add(index + 0);
		}
		
		final Vector2i forBlock = atlas.getBlock(block);
		final float atlasStep = 1.0f / (float) atlas.getSize();
		
		uvs.add(new Vector2f(atlasStep * forBlock.x, atlasStep * forBlock.y));
		uvs.add(new Vector2f(atlasStep * forBlock.x, atlasStep + atlasStep * forBlock.y));
		uvs.add(new Vector2f(atlasStep + atlasStep * forBlock.x, atlasStep + atlasStep * forBlock.y));
		uvs.add(new Vector2f(atlasStep + atlasStep * forBlock.x, atlasStep * forBlock.y));
	}
	
}