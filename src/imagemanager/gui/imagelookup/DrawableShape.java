package imagemanager.gui.imagelookup;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class DrawableShape extends DrawableObject {
	
	protected Shape shape;
	protected Shape tShape;
	protected Color color = Color.BLUE;
	
	public DrawableShape(Shape shape) {
		this.shape = shape;
		this.tShape = shape;
	}
	
	public void transform(AffineTransform at){
		GeneralPath p = new GeneralPath(shape);
		tShape = p.createTransformedShape(at);
	}
	
	@Override
	public void drawObject(Graphics2D g2d) {
		Color c = g2d.getColor();
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(3));
		g2d.draw(tShape);
		g2d.setColor(c);
		
		
	}

}
