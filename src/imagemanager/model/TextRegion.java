package imagemanager.model;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TextRegion implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	
	// lewy gorny rog prostokatneo obwodu regionu tekstoweo
	private Point origin;
	
	// wymiary obwodu regionu tekstoweo
	private Dimension dim;
	
	// Zbior elementow tekstu nalezacych do teoo regionu tekstowego 
	// kazdy taki element ma swoj unikatowy numer
	private Set<Long> blobs = new HashSet<Long>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	private BoardRegion boardRegion;

	public TextRegion() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Dimension getDim() {
		return dim;
	}

	public void setDim(Dimension dim) {
		this.dim = dim;
	}
	
	public Set<Long> getBlobs() {
		return blobs;
	}

	public void setBlobs(Set<Long> blobs) {
		this.blobs = blobs;
	}
	
	public BoardRegion getBoardRegion() {
		return boardRegion;
	}

	public void setBoardRegion(BoardRegion boardRegion) {
		this.boardRegion = boardRegion;
	}

	@Override
	public String toString() {
		return "TextRegion [id=" + id + ", origin=" + origin + ", dim=" + dim
				+ "]";
	}

}
