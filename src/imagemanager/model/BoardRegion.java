package imagemanager.model;

import imageprocessing.TextLocating;
import imageprocessing.Util;

import java.awt.Point;
import java.awt.Polygon;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
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
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

@Entity
public class BoardRegion {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private MyQuadrangle perimeter;
	
	private int imageWidth;
	private int imageHeight;
	
	private int openCVType;
	
	public enum BoardType {
		WHITEBOARD, BLACKBOARD
	}
	
	@Enumerated(EnumType.STRING)
	private BoardType boardType;
	
	@Embedded
	BoardRegionParams params;
	
	
	// Zdjęcie początkowe do którego zawsze można wrócić
	// bez nałożonej maski
	@Lob
	private byte[] rawPicture;
	
	// Macierz spójnych składowych 
	// 0 - tło 
	// inne numery to ID spójnych składowych
	@Lob
	private int[] labeledText;
	
	// Oczyszczone zdjęcie z nałożoną maską
	@Lob
	private byte[] result;
	
	// Maska którą nakłada się na surowe zdjęcie
	@Lob
	private byte[] mask;
	
	// Aktualnie zaznaczone spójne składowe
	Map<Integer, Boolean> marking;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private SourceImage sourceImage;
	
	@OneToMany(cascade={CascadeType.ALL}, mappedBy = "boardRegion")
	private Collection<TextRegion> textRegions = new ArrayList<TextRegion>();
	
	public BoardRegion() {
		
	}
	
	public BoardRegion(Long id, MyQuadrangle perimeter, int imageWidth,
			int imageHeight, int openCVType, byte[] pixels, BoardType boardType) {
		super();
		this.id = id;
		this.perimeter = perimeter;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.openCVType = openCVType;
		this.rawPicture = pixels;
		this.boardType = boardType;
	}
	
	public BoardRegion(Mat image, Point[] quadrangle, BoardType boardType, BoardRegionParams params){
		this.imageWidth = image.width();
		this.imageHeight = image.height();
		this.openCVType = image.type();
		this.rawPicture = Util.mat2Byte(image);
		this.perimeter = new MyQuadrangle(quadrangle);
		this.boardType = boardType;
		this.params = params;
	}
	
	public void clearBackground(){
		Mat image = getUnprocessedImage();
		Mat mask = image.clone();
		Mat bg = null;
		switch (boardType) {
		case BLACKBOARD:
		// create mask
			Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			Imgproc.GaussianBlur(mask, mask, new Size(params.getBlurMaskSize(), params.getBlurMaskSize()), 0, 0);
			Imgproc.adaptiveThreshold(mask, mask, 255.0,
					Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,
					params.getThreshholdBlockSize(), params.getThresholdConstant());
			Imgproc.cvtColor(mask, mask, Imgproc.COLOR_GRAY2BGR);	
			bg = new Mat(image.size(), image.type(), new Scalar(new double[] {68, 61, 58}));

			break;

		case WHITEBOARD:
		// create mask
			Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			Imgproc.GaussianBlur(mask, mask, new Size(params.getBlurMaskSize(), params.getBlurMaskSize()), 0, 0);
			Imgproc.adaptiveThreshold(mask, mask, 255.0,
					Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV,
					params.getThreshholdBlockSize(), params.getThresholdConstant());
			Imgproc.cvtColor(mask, mask, Imgproc.COLOR_GRAY2BGR);		
			bg = new Mat(image.size(), image.type(), new Scalar(new double[] {255, 255, 255}));
			
			break;
			
		}
		
		
		
		// subtract mask
		// napisy na pierwszym planie
		Mat fg = image.clone();
		Core.bitwise_and(fg, mask, fg);
		
		// tlo
		Core.bitwise_not(mask, mask);
		//Test.showImage(Util.mat2Img(mask), "mask");
		Core.bitwise_and(mask, bg, bg);
		Core.bitwise_or(bg, fg, image);
		
		result = Util.mat2Byte(image);
		
		List<Polygon> textPolygons = TextLocating.findTextPolygons(Util.mat2Img(mask));
		for (Polygon polygon : textPolygons) {
			TextRegion textRegion = new TextRegion();
			textRegion.setPerimeter(polygon);
			textRegion.setBoardRegion(this);
			textRegions.add(textRegion);
		}
	}
	
	public void extractTextRegions() {
		
		Mat image = getResultImage();
		List<Polygon> textPolygons = TextLocating.findTextPolygons(Util.mat2Img(image));
		for (Polygon polygon : textPolygons) {
			TextRegion textRegion = new TextRegion();
			textRegion.setPerimeter(polygon);
			textRegion.setBoardRegion(this);
			textRegions.add(textRegion);
		}
	}
	
	public Long getId(){
		return id;
	}
	
	public MyQuadrangle getPerimeter() {
		return perimeter;
	}

	
	public Mat getUnprocessedImage(){
		Mat image = new Mat(new Size(imageWidth, imageHeight), openCVType);
		image.put(0, 0, getUnprocessed());
		return image;
	}
	
	public SourceImage getSourceImage() {
		return sourceImage;
	} 

	public void setSourceImage(SourceImage sourceImage) {
		this.sourceImage = sourceImage;
	}
	
	public Collection<TextRegion> getTextRegions() {
		return textRegions;
	}

	public void setTextRegions(Collection<TextRegion> textRegions) {
		this.textRegions = textRegions;
	}
	
	private byte[] getUnprocessed(){
		return rawPicture;
	}

	public BoardType getBoardType() {
		return boardType;
	}
	
	public BoardRegionParams getParams(){
		return params;
	}
	
	public void setParams(BoardRegionParams params){
		this.params = params;
	}
	public byte[] getResult() {
		return result;
	}
	
	public Mat getResultImage(){
		Mat image = new Mat(new Size(imageWidth, imageHeight), openCVType);
		image.put(0, 0, getResult());
		return image;
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
