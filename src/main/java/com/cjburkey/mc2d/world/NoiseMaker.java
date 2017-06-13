package com.cjburkey.mc2d.world;

import java.util.concurrent.ThreadLocalRandom;
import com.cjburkey.mc2d.core.Utils;

public final class NoiseMaker {
	
	private static final int ARB1 = 824;
	private static final int ARB2 = 317;
	
	private static final double scale1 = 100.0d;
	private static final double yBase1 = 5.0d;
	private static final double amplitude1 = 20.0d;
	
	private static final double scale2 = 40.0d;
	private static final double yBase2 = 5.0d;
	private static final double amplitude2 = 15.0d;
	private final int seed;
	
	public NoiseMaker() {
		this(ThreadLocalRandom.current().nextInt(-999, 999 + 1));
	}
	
	public NoiseMaker(int seed) {
		this.seed = Utils.clamp(seed, -999, 999);
	}
	
	public double noise(double x, double y) {
		double noise = 0.0d;
		double n1 = noise(x, y, scale1, yBase1, amplitude1);
		double n2 = noise(x, y, scale2, yBase2, amplitude2);
		noise = n1 + n2;
		return noise;
	}
	
	private double noise(double x, double y, double scale, double base, double amp) {
		double val = SimplexNoise.noise(ARB1 * seed + x / scale, ARB2 * seed + y / scale);
		val += (base - y) / amp;
		return val;
	}
	
	public int getSeed() {
		return seed;
	}
	
}