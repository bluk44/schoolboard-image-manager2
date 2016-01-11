package imagemanager.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.Set;


public class TextRegion implements Serializable{
		
	private String text;
	private Set<Point> polygon;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Set<Point> getPolygon() {
		return polygon;
	}
	public void setPolygon(Set<Point> polygon) {
		this.polygon = polygon;
	}
	@Override
	public String toString() {
		return "TextRegion [text=" + text + ", polygon=" + polygon + "]";
	}
	
}

