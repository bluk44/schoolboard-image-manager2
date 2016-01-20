package imageprocessing;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

public class AWTUtil {
	
	public static Polygon copyPolygon(Polygon poly, AffineTransform at){
		
		PathIterator iter = poly.getPathIterator(at);
		
		Polygon copy = new Polygon();
		
		float[] coords = new float[2];
		do{
			if(! (iter.currentSegment(coords) == PathIterator.SEG_CLOSE)){
				copy.addPoint((int)coords[0], (int)coords[1]);
			} 
			iter.next();
		} while(! iter.isDone());
		
		return copy;
	}
	
	public static String polygonToString(Polygon poly){
		
		String str = "";
		PathIterator iter = poly.getPathIterator(null);

		float[] coords = new float[2];
		do{
			if(! (iter.currentSegment(coords) == PathIterator.SEG_CLOSE)){
				str += "("+coords[0]+" "+coords[1]+")";
			} 
			iter.next();
		} while(! iter.isDone());

		return str;
	}
}
