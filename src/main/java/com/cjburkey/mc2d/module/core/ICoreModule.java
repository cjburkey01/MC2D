package com.cjburkey.mc2d.module.core;

import com.cjburkey.mc2d.core.SemVer;

public abstract class ICoreModule {
	
	public abstract String getName();
	public abstract SemVer getRequiredGameVersion();
	public abstract boolean getPatchAllowDifference();
	public abstract boolean getMinorAllowDifference();
	public abstract boolean getMajorAllowDifference();
	
	public abstract void onLogicInit();
	public abstract void onLogicTick();
	public abstract void onLogicCleanup();
	
	public abstract void onRenderInit();
	public abstract void onRenderUpdate();
	public abstract void onRenderCleanup();
	
	public boolean equals(Object obj) {
		if(obj == null || !obj.getClass().equals(getClass())) return false;
		return true;
	}
	
}