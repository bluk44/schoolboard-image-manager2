package imagemanager.model;

import imageprocessing.Util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class BoardRegion {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Lob
	private MyQuadrangle perimeter;
	
	@Lob
	private byte[] pixels;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private SourceImage sourceImage;
	
	public BoardRegion(){}
	
	public BoardRegion(File file){
		BufferedImage pixels = Util.readImageFromFile(file);
		this.pixels = Util.getByteArray(pixels);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public MyQuadrangle getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(MyQuadrangle perimeter) {
		this.perimeter = perimeter;
	}
	
	public SourceImage getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(SourceImage sourceImage) {
		this.sourceImage = sourceImage;
	}

	public byte[] getPixels() {
		return pixels;
	}

	public void setPixels(byte[] pixels) {
		this.pixels = pixels;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((perimeter == null) ? 0 : perimeter
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardRegion other = (BoardRegion) obj;
		if (perimeter == null) {
			if (other.perimeter != null)
				return false;
		} else if (!perimeter.equals(other.perimeter))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BoardRegion [id=" + id + ", perimeter=" + perimeter + "]";
	}

}
