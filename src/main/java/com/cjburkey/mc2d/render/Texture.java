package com.cjburkey.mc2d.render;

import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.core.Resource;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public final class Texture {
	
	private static Texture missingTexture;
	
	private PNGDecoder png;
	private boolean isLoaded = false;
	private int textureId;
	
	public static Texture getTexture(String loc) {
		return getTexture(Resource.getStream(loc));
	}
	
	public static Texture getTexture(InputStream stream) {
		Texture texture = new Texture();
		if(stream != null) {
			try {
				texture.png = new PNGDecoder(stream);
				return texture;
			} catch(Exception e) {
				MC2D.getLogger().log("Couldn't load texture.");
			}
		}
		return getDefaultTexture();
	}
	
	public static Texture getDefaultTexture() {
		if(missingTexture == null) {
			missingTexture = getTexture("mc2d:texture/basic/missing.png");
		}
		return missingTexture;
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