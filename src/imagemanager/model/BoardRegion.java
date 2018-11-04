package imagemanager.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.opencv.core.Core;
import org.opencv.core.Mat;

@Entity
public class BoardRegion {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private MyQuadrangle perimeter;

	public enum BoardType {
		WHITEBOARD, BLACKBOARD
	}

	@Enumerated(EnumType.STRING)
	private BoardType boardType;

	@Embedded
	BoardRegionParams params;

	// kolorowe zdjęcie początkowe bez nałożonej maski
	@AttributeOverrides({
		@AttributeOverride(name="width", column=@Column(name="pictureWidth")),
		@AttributeOverride(name="height", column=@Column(name="pictureHeight")),
		@AttributeOverride(name="type", column=@Column(name="pictureType")),
		@AttributeOverride(name="pixels", column=@Column(name="picturePixels"))
	})
	@Embedded
	private Image rawPicture;

	// Maska którą nakłada się na zdjęcie w celu oddzielenia tekstu od tłą
	@AttributeOverrides({
		@AttributeOverride(name="width", column=@Column(name="maskWidth")),
		@AttributeOverride(name="height", column=@Column(name="maskHeight")),
		@AttributeOverride(name="type", column=@Column(name="maskType")),
		@AttributeOverride(name="pixels", column=@Column(name="maskPixels"))
	})
	@Embedded
	private Mask mask;
		
	@ManyToOne(fetch = FetchType.LAZY)
	private SourceImage sourceImage;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "boardRegion")
	private Collection<TextRegion> textRegions = new ArrayList<TextRegion>();

	public BoardRegion() {
	}

	// konstruktor używany przy wyciaganiu z bazy danych
	public BoardRegion(Long id, MyQuadrangle perimeter, BoardType boardType) {
		super();
		this.id = id;
		this.perimeter = perimeter;
		this.boardType = boardType;
	}

	// konstruktor używany przez program do stworzenia nowego obiektu w pamieci
	public BoardRegion(Mat image, Point[] quadrangle, BoardType boardType,
			BoardRegionParams params) {
		this.rawPicture = new Image(image);
		this.mask = new Mask(image, boardType, params);
		this.perimeter = new MyQuadrangle(quadrangle);
		this.boardType = boardType;
		this.params = params;
	}
	
	public Mat getClearedImage() {
		Mat image = rawPicture.getMat();
		Mat mask = this.mask.getMat();
		
		Core.bitwise_and(image, mask, image);
		Core.bitwise_not(mask, mask);
		Core.bitwise_or(mask, image, image);		

		return image;
	}
	
	public void updateMask(){
		this.mask = new Mask(rawPicture.getMat(), boardType, params);		
	}
		
	public void extractTextRegions() {

		// Mat image = getResultImage();
		// List<Polygon> textPolygons =
		// TextLocating.findTextPolygons(Util.mat2Img(image));
		// for (Polygon polygon : textPolygons) {
		// TextRegion textRegion = new TextRegion();
		// textRegion.setPerimeter(polygon);
		// textRegion.setBoardRegion(this);
		// textRegions.add(textRegion);
		// }
	}

	public Long getId() {
		return id;
	}

	public MyQuadrangle getPerimeter() {
		return perimeter;
	}

	public SourceImage getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(SourceImage sourceImage) {
		this.sourceImage = sourceImage;
	}
	
	public Image getRawPicture() {
		return rawPicture;
	}

	public void setRawPicture(Image rawPicture) {
		this.rawPicture = rawPicture;
	}

	public Mask getMask() {
		return mask;
	}

	public void setMask(Mask mask) {
		this.mask = mask;
	}
	
	public BoardType getBoardType() {
		return boardType;
	}

	public BoardRegionParams getParams() {
		return params;
	}

	public void setParams(BoardRegionParams params) {
		this.params = params;
	}
	
	public Collection<TextRegion> getTextRegions() {
		return textRegions;
	}

	public void setTextRegions(Collection<TextRegion> textRegions) {
		this.textRegions = textRegions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((perimeter == null) ? 0 : perimeter.hashCode());
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
