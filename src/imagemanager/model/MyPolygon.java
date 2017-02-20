package imagemanager.model;

import java.awt.Point;
import java.util.LinkedHashSet;

public class MyPolygon extends LinkedHashSet<Point>{
	
	public MyPolygon(Point[] points){
		for (Point point : points) {
			this.add(new Point(point));
		}
	}
	
	public java.awt.Polygon toAWTPolygon(){
		
		java.awt.Polygon poly = new java.awt.Polygon();
		for (Point point : this) {
			poly.addPoint(point.x, point.y);
			
		}
		
		return poly;
	}
	
	public Point[] getPoints(){
		return this.toArray(new Point[] {});
		
	}
	
	@Override
	public String toString() {
		String pointString = "[";
		for (Point point : this) {
			pointString = pointString + "("+point.x+","+point.y+")";
		}
		pointString+="]";
		
		return pointString;
	}
}
