package com.cjburkey.mc2d.core;

import java.io.InputStream;

public class Resource {
	
	public static InputStream getStream(String loc) {
		String path = getPath(loc);
		if(path != null) {
			return Resource.class.getResourceAsStream(path);
		}
		return null;
	}
	
	public static String getPath(String loc) {
		String[] split = loc.split(":");
		if(split.length == 2) {
			return "/" + split[0] + "/" + split[1];
		}
		return null;
	}
	
}