package imagemanager.gui.imagelookup;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;

public class DrawableTextRegion extends DrawableShape {
	
	String id;
	
	private final Stroke stroke = new BasicStroke(3);
	private final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 32);
	
	public DrawableTextRegion(Shape shape, Long id){
		super(shape);
		this.id = id.toString();
	}
	
	@Override
	public void drawObject(Graphics2D g2d) {
		Color c = g2d.getColor();
		Stroke s = g2d.getStroke();
		Font f = g2d.getFont();
		
		g2d.setColor(color);
		g2d.setStroke(stroke);
		g2d.draw(shape);
		g2d.setFont(font);
		g2d.setColor(c);	
		g2d.setStroke(s);
	}
	
}
