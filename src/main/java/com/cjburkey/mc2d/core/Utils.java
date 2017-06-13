package com.cjburkey.mc2d.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.chunk.ChunkData;

public final class Utils {
	
	private static final char NEWLINE = '\n';
	
	public static String readResourceAsString(String loc) {
		StringBuilder out = new StringBuilder();
		Queue<String> lines = readResourceAsLines(loc);
		for(String line : lines) {
			out.append(line);
			out.append(NEWLINE);
		}
		return out.toString();
	}
	
	public static Queue<String> readResourceAsLines(String loc) {
		Queue<String> out = new ConcurrentLinkedQueue<>();
		InputStream stream = Resource.getStream(loc);
		if(stream != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String line = null;
				while((line = reader.readLine()) != null) {
					out.add(line);
				}
				reader.close();
			} catch(Exception e) {
				MC2D.getLogger().err("Couldn't read resource: " + loc);
				e.printStackTrace();
				MC2D.INSTANCE.stopGame();
			}
		} else {
			System.err.println("Couldn't load resource: " + loc);
		}
		return out;
	}
	
	public static int clamp(int val, int min, int max) {
		if(val < min) return min;
		if(val > max) return max;
		return val;
	}
	
	public static Vector3f getBlockPos(Vector3f pos) {
		float scale = ChunkData.scale;
		float blockX = (float) Math.floor(pos.x / scale) * scale + scale / 2.0f;
		float blockY = (float) Math.floor(pos.y / scale) * scale + scale / 2.0f;
		return new Vector3f(blockX, blockY, pos.z);
	}
	
	public static float[] vector3fToFloats(List<Vector3f> v3s) {
		float[] out = new float[v3s.size() * 3];
		int i = 0;
		for(Vector3f v3 : v3s) {
			out[i * 3] = v3.x;
			out[i * 3 + 1] = v3.y;
			out[i * 3 + 2] = v3.z;
			i ++;
		}
		return out;
	}
	
	public static float[] vector2fToFloats(List<Vector2f> v2s) {
		float[] out = new float[v2s.size() * 2];
		int i = 0;
		for(Vector2f v2 : v2s) {
			out[i * 2] = v2.x;
			out[i * 2 + 1] = v2.y;
			i ++;
		}
		return out;
	}
	
	public static int[] intToInts(List<Integer> ints) {
		int[] out = new int[ints.size()];
		int i = 0;
		for(Integer in : ints) {
			out[i] = in;
			i ++;
		}
		return out;
	}
	
}