package imagemanager.gui.imagelookup;

import imageprocessing.Util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DrawableImage extends DrawableObject {
	
	BufferedImage image = Util.readImageFromFile("icons/noimage.png");
	
	@Override
	public void drawObject(Graphics2D g2d) {
		g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public int getWidth(){
		return image.getWidth();
	}
	
	public int getHeight(){
		return image.getHeight();
	}
}
