package com.cjburkey.mc2d.core;

import java.io.OutputStream;
import java.io.PrintStream;

public final class Logger {
	
	private final PrintStream out;
	private final PrintStream err;
	
	private final PrintStream hijackedOut;
	private final PrintStream hijackedErr;
	
	public Logger() {
		out = System.out;
		err = System.err;
		
		hijackedOut = new HijackedStream(out);
		hijackedErr = new HijackedStream(err);
	}
	
	public void hijackOutput() {
		System.setOut(hijackedOut);
		System.setErr(hijackedErr);
	}
	
	public void log(Object out) {
		System.out.println((out == null) ? "null" : out.toString());
	}
	
	public void err(Object out) {
		System.err.println((out == null) ? "null" : out.toString());
	}
	
	private class HijackedStream extends PrintStream {
		public HijackedStream(OutputStream out) {
			super(out);
		}
		
		public void println(String in) {
			String format = "[MC2D - %s] %s";
			super.println(String.format(format, Thread.currentThread().getName(), in));
		}
	}
	
}