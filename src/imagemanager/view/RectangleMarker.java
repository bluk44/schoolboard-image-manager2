package imagemanager.view;

import java.awt.Graphics;
import java.awt.Point;


public class RectangleMarker {
	
	
	private Point ps = null;
	private Point pe = null;
	
	public void start(int x, int y){
		ps = new Point(x, y);
	}
	
	public void update(int x, int y){
		pe = new Point(x, y);
	}
	
	public void stop(){
		
	}
	
	public void reset(){
		ps = null;
		pe = null;
	}
	
	public void draw(Graphics g){
		
		if(ps == null || pe == null) return;
		
		Point p1 = new Point(Math.min(ps.x, pe.x), Math.min(ps.y, pe.y));
		Point p2 = new Point(Math.max(ps.x, pe.x), Math.max(ps.y, pe.y));
		
		int w = p2.x - p1.x;
		int h = p2.y - p1.y;
		
		g.drawRect(p1.x, p1.y, w, h);
	}
}
