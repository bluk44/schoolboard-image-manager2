package imagemanager.gui.imagelookup;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

public class DrawableBoardQuadrangle extends DrawableShape {

	String id;
	Point center;
	
	public DrawableBoardQuadrangle(Shape shape, Long id){
		super(shape);
		this.id = id.toString();
		PathIterator iter = shape.getPathIterator(new AffineTransform());
		float[] coords = new float[2];
		center = new Point(0,0);
		for (int i = 0; i < 4; i++) {
			iter.currentSegment(coords);
			center.x += coords[0];
			center.y += coords[1];
			iter.next();
		}
		
		center.x /= 4;
		center.y /= 4;
	}
	
	@Override
	public void drawObject(Graphics2D g2d) {
		System.out.println("DrawableBoardQuadrangle draw object called");
		Color c = g2d.getColor();
		g2d.setColor(color);
		g2d.draw(shape);
		g2d.drawString(id, center.x, center.y);
		g2d.setColor(c);	}
}
