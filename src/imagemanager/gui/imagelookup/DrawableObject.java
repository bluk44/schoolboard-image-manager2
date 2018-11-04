package imagemanager.gui.imagelookup;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public abstract class DrawableObject {
	
	protected boolean visible = true;
	
	public void draw(Graphics2D g2d){

		drawObject(g2d);
		
	}
	
	protected abstract void drawObject(Graphics2D g2d);

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
