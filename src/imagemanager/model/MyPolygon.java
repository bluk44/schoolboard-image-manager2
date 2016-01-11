package imagemanager.model;

import java.awt.Point;
import java.util.LinkedHashSet;

public class MyPolygon extends LinkedHashSet<Point>{
	
	public java.awt.Polygon toAWTPolygon(){
		
		java.awt.Polygon poly = new java.awt.Polygon();
		for (Point point : this) {
			poly.addPoint(point.x, point.y);
			
		}
		
		return poly;
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
