package imagemanager.gui.imagelookup;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

public class DrawableBoardQuadrangle extends DrawableShape {

	String id;
	private final Point center;
	private final Stroke stroke = new BasicStroke(3);
	private final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 32);
	
	public DrawableBoardQuadrangle(Polygon shape, Long id){
		super(shape);
		this.id = id.toString();
		
		// szukanie srodka geometrycznego by wyswietlic id
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
//		int x2Points[] = {10, 55, 15, 50};
//		int y2Points[] = {10, 15, 40, 45};		
//		tShape = new Polygon(x2Points, y2Points, 4);
		
		Color c = g2d.getColor();
		Stroke s = g2d.getStroke();
		Font f = g2d.getFont();
		
		g2d.setColor(color);
		g2d.setStroke(stroke);
		g2d.draw(tShape);
		
		//g2d.setFont(font);
		//g2d.drawString(id, center.x, center.y);
		g2d.setColor(c);	
		g2d.setStroke(s);
	}
}
