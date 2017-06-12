package com.cjburkey.mc2d.render;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.joml.Vector2i;
import com.cjburkey.mc2d.MC2D;
import com.cjburkey.mc2d.block.ABlock;
import com.cjburkey.mc2d.core.Resource;
import com.cjburkey.mc2d.io.IO;

public final class TextureAtlas {
	
	private static final int imgSize = 16;
	private ABlock[][] textures;
	private final File tmpFile;
	private Texture texture;
	private int width;
	
	public TextureAtlas() {
		tmpFile = new File(IO.getTmpDir(), "/atlas.png");
	}
	
	public void build(ABlock[] blocks) {
		calcWidth(blocks.length);
		MC2D.getLogger().log("Building texture atlas: " + width + "x" + width);
		textures = new ABlock[width][width];
		MC2D.getLogger().log("Create BufferedImage");
		BufferedImage atlas = new BufferedImage(width * imgSize, width * imgSize, BufferedImage.TYPE_INT_ARGB);
		MC2D.getLogger().log("Created BufferedImage");
		int x = 0;
		int y = 0;
		for(ABlock block : blocks) {
			addBlockToAtlas(x, y, block, atlas);
			x ++;
			if(x >= width) {
				x = 0;
				y ++;
			}
		}
		saveAndLoadTexture(atlas);
		MC2D.getLogger().log("Built texture atlas.");
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public int getSize() {
		return width;
	}
	
	public Vector2i getBlock(ABlock block) {
		for(int x = 0; x < width; x ++ ){
			for(int y = 0; y < width; y ++) {
				if(textures[x][y].equals(block)) {
					return new Vector2i(x, y);
				}
			}
		}
		return null;
	}
	
	private void calcWidth(int blocks) {
		width = 0;
		while(Math.pow(width, 2) < blocks) {
			width ++;
		}
	}
	
	private void addBlockToAtlas(int x, int y, ABlock block, BufferedImage atlas) {
		BufferedImage blockImg = loadImageForBlock(block);
		if(blockImg != null && blockImg.getWidth() == imgSize && blockImg.getHeight() == imgSize) {
			Graphics2D g = atlas.createGraphics();
			g.drawImage(blockImg, x * imgSize, y * imgSize, null);
			textures[x][y] = block;
		}
	}
	
	private void saveAndLoadTexture(BufferedImage img) {
		try {
			ImageIO.write(img, "png", tmpFile);
			texture = Texture.getTexture(tmpFile.toURI().toURL().openStream());
			Renderer.instance.runLater(() -> {
				texture.load();
				tmpFile.delete();
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage loadImageForBlock(ABlock block) {
		String loc = "mc2d:texture/block/" + block.getUnlocalizedName() + ".png";
		try {
			InputStream stream = Resource.getStream(loc);
			if(stream == null) return null;
			BufferedImage img = ImageIO.read(stream);
			return img;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}