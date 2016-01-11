package imagemanager.gui.imagelookup;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.LinkedHashSet;

public class QuadrangleSelecting extends DrawableObject{
	
	private LinkedHashSet<Point> quadrangle = new LinkedHashSet<Point>();
	private Color pointColor = Color.RED;
	
	AffineTransform at = new AffineTransform();
	
	public Polygon getSelectedQuadrangle() throws QuadrangleIncompleteException{
		if(quadrangle.size() < 4) throw new QuadrangleIncompleteException();		
		else {
			// znajdz srodek ciezkosci
			Point b = new Point();
			for (Point point : quadrangle) {
				b.x += point.x;
				b.y += point.y;
			}
			b.x /= 4;
			b.y /= 4;
			
			// nadaj kolejnosc punktom aby tworzyly prostokat
			Point[] pts = new Point[4];
			for (Point point : quadrangle) {
				if(point.x > b.x && point.y > b.y){
					pts[0] = new Point(point.x, point.y);
				} else if(point.x < b.x && point.y < b.y){
					pts[2] = new Point(point.x, point.y);
				} else if(point.x > b.x && point.y < b.y){
					pts[1] = new Point(point.x, point.y);
				} else {
					pts[3] = new Point(point.x, point.y);
				}
			}
			
			Polygon poly = new Polygon();
			for (Point point : pts) {
				poly.addPoint(point.x, point.y);
			}
			
			return poly;
		}
		
	}
	
	public void addPoint(Point p){
		if(quadrangle.size() < 4){
			quadrangle.add(p);
		}
	}
	
	public void dragPoint(Point p){
		for (Point point : quadrangle) {
			if(point.distance(p) <= 2){
				point.x = p.x;
				point.y = p.y;
			}
		}
	}
	
	@Override
	protected void drawObject(Graphics2D g2d) {
		Color c = g2d.getColor();
		g2d.setColor(pointColor);
		AffineTransform saveAT = g2d.getTransform();
		
		System.out.println(saveAT.getScaleX()+" "+saveAT.getScaleY());
		for (Point point : quadrangle) {
			if(saveAT.getScaleX() < 1){
				g2d.fillOval(point.x-2, point.y-2, (int)(4 / saveAT.getScaleX()), (int)(4 /saveAT.getScaleY()));
			} else{
				g2d.fillOval(point.x-2, point.y-2, 4, 4);
			}
			
			//g2d.fillOval(point.x-2, point.y-2, 4, 4);
		}
		g2d.setTransform(saveAT);
		g2d.setColor(c);		
	}
	
}
