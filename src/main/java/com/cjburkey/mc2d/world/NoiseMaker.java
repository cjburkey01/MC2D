package com.cjburkey.mc2d.world;

import java.util.concurrent.ThreadLocalRandom;
import com.cjburkey.mc2d.core.Utils;

public final class NoiseMaker {
	
	private static final int ARB1 = 824;
	private static final int ARB2 = 317;
	private final int seed;
	
	public NoiseMaker() {
		this(ThreadLocalRandom.current().nextInt(-999, 999 + 1));
	}
	
	public NoiseMaker(int seed) {
		this.seed = Utils.clamp(seed, -999, 999);
	}
	
	public double noise(double x, double y, double scale, double yBase, double amp) {
		double val = SimplexNoise.noise(ARB1 * seed + x / scale, ARB2 * seed + y / scale);
		val += (yBase - y) / amp;
		return val;
	}
	
	public int getSeed() {
		return seed;
	}
	
}