package imagemanager.model;

import imageprocessing.AWTUtil;

import java.awt.Polygon;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class TextRegion implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private Polygon perimeter;

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

	public Polygon getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(Polygon perimeter) {
		this.perimeter = perimeter;
	}

	public BoardRegion getBoardRegion() {
		return boardRegion;
	}

	public void setBoardRegion(BoardRegion boardRegion) {
		this.boardRegion = boardRegion;
	}

	@Override
	public String toString() {
		return "TextRegion [id=" + id + ", perimeter="
				+ AWTUtil.polygonToString(perimeter) + "]";
	}
}
