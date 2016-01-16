package imagemanager.gui.imagelookup;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.Shape;

public class DrawableShape extends DrawableObject {
	
	protected Shape shape;
	protected Color color = Color.BLUE;
	
	public DrawableShape(Shape shape) {
		this.shape = shape;
	}
	
	@Override
	public void drawObject(Graphics2D g2d) {
		Color c = g2d.getColor();
		g2d.setColor(color);
		g2d.draw(shape);
		g2d.setColor(c);
	}

}
