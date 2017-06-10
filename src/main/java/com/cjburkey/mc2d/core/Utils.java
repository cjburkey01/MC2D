package com.cjburkey.mc2d.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.cjburkey.mc2d.MC2D;

public class Utils {
	
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
	
}