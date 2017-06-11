package com.cjburkey.mc2d.module.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.SemVer;

public final class ModuleHandler<T extends ICoreModule> {
	
	private final List<T> modules = new ArrayList<T>();
	private final EventHandler events = new EventHandler();
	
	public void loadModules(Class<T> c) {
		System.out.println("Loading modules...");
		Reflections r = new Reflections();
		Set<Class<? extends T>> classes = r.getSubTypesOf(c);
		for(Class<? extends T> clazz : classes) {
			ICoreModule m = addModule(clazz);
			if(m != null) {
				System.out.println("Loaded module: " + m.getName());
			}
		}
		System.out.println("Loaded modules.");
	}
	
	private ICoreModule addModule(Class<? extends T> clazz) {
		T inst = createInstance(clazz);
		SemVer requiredVersion = inst.getRequiredGameVersion();
		if(requiredVersion.getMajor() != MC2D.VERSION.getMajor() && !inst.getMajorAllowDifference()) {
			return null;
		}
		
		if(requiredVersion.getMinor() != MC2D.VERSION.getMinor() && !inst.getMinorAllowDifference()) {
			return null;
		}
		
		if(requiredVersion.getPatch() != MC2D.VERSION.getPatch() && !inst.getPatchAllowDifference()) {
			return null;
		}
		
		for(T t : modules) {
			if(t.equals(inst)) {
				return null;
			}
		}
		modules.add(inst);
		return inst;
	}
	
	public EventHandler getEventSystem() {
		return events;
	}
	
	private T createInstance(Class<? extends T> clazz) {
		try {
			T instance = clazz.newInstance();
			return instance;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public class EventHandler {
		public void logicInit() {
			modules.forEach(e -> e.onLogicInit());
		}
		
		public void logicTick() {
			modules.forEach(e -> e.onLogicTick());
		}
		
		public void logicCleanup() {
			modules.forEach(e -> e.onLogicCleanup());
		}
		
		public void renderInit() {
			modules.forEach(e -> e.onRenderInit());
		}
		
		public void renderUpdate() {
			modules.forEach(e -> e.onRenderUpdate());
		}
		
		public void renderCleanup() {
			modules.forEach(e -> e.onRenderCleanup());
		}
	}
	
}