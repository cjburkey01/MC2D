package com.cjburkey.mc2d.render;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import com.cjburkey.mc2d.core.Resource;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public final class Texture {
	
	public static Texture DEFAULT = new Texture("mc2d:texture/basic/missing.png");
	public static Texture BLANK = new Texture("mc2d:texture/basic/blank.png");
	
	private PNGDecoder png;
	private boolean isLoaded = false;
	private int textureId;
	
	public Texture(InputStream stream) {
		try {
			png = new PNGDecoder(stream);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Texture(String loc) {
		this(Resource.getStream(loc));
	}
	
	public Texture(File file) throws MalformedURLException, IOException {
		this(file.toURI().toURL().openStream());
	}
	
	public void load() {
		if(!isLoaded) {
			isLoaded = true;
			doLoad();
		}
	}
	
	public void activate() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	
	public void cleanup() {
		deactivate();
	}
	
	private void doLoad() {
		try {
			ByteBuffer buf = ByteBuffer.allocateDirect(4 * png.getWidth() * png.getHeight());
			png.decode(buf, png.getWidth() * 4, Format.RGBA);
			buf.flip();
			
			textureId = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			//GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, png.getWidth(), png.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_INT, buf);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, png.getWidth(), png.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deactivate() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
}