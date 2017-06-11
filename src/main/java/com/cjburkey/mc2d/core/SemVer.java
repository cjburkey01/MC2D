package com.cjburkey.mc2d.core;

import org.joml.Vector3i;

public final class SemVer {
	
	private final Vector3i version;
	
	private SemVer(Vector3i v) {
		version = new Vector3i(v);
	}
	
	public SemVer(int major, int minor, int patch) {
		version = new Vector3i(major, minor, patch);
	}
	
	public int getMajor() {
		return version.x;
	}
	
	public int getMinor() {
		return version.y;
	}
	
	public int getPatch() {
		return version.z;
	}
	
	public String toString() {
		final String sep = ".";
		return version.x + sep + version.y + sep + version.z;
	}
	
	public SemVer clone() {
		return new SemVer(version);
	}
	
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		
		if(!(other instanceof SemVer)) {
			return false;
		}
		
		SemVer s = (SemVer) other;
		if(!s.version.equals(version)) {
			return false;
		}
		return true;
	}
	
}