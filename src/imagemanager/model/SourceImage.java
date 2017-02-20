package imagemanager.model;

import imagemanager.model.BoardRegion.BoardType;
import imageprocessing.Util;

import java.awt.Point;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;




@Entity
public class SourceImage {
	
	@Transient
	// rozmiar ikony w pikselach
	static float ICON_SIZE = 160000; 
	
	@Id
	@GeneratedValue
	private Long id;
	@Column(unique=true)
	private String name;
	private Long date;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	private byte[] iconPixels;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] imagePixels;
	
	private int imageWidth;
	private int imageHeight;
	
	private int iconWidth;
	private int iconHeight;
	
	private int openCVType;
	
	@ManyToMany(mappedBy = "images")
	Set<Category> categories = new HashSet<Category>();

	@OneToMany(cascade={CascadeType.ALL}, mappedBy = "sourceImage")
	Set<BoardRegion> boardImages = new HashSet<BoardRegion>();

	public SourceImage() {
	};

	public SourceImage(String name, Long date, byte[] iconPixels,
			byte[] imagePixels, int imageWidth, int imageHeight, int iconWidth,
			int iconHeight, int openCVType) {
		super();
		this.name = name;
		this.date = date;
		this.iconPixels = iconPixels;
		this.imagePixels = imagePixels;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
		this.openCVType = openCVType;
	}
	
	public SourceImage(File file) {
		String[] tab = file.getName().split("/");
		this.name = tab[tab.length - 1];
		this.date = file.lastModified();
		//Mat image = imread(file.getPath());
		Mat image = imread
		Mat icon = new Mat();
		
		this.openCVType = image.type();
		
		this.imageHeight = (int) image.size().height;
		this.imageWidth = (int) image.size().width;
				
		double imgSize = imageWidth * imageHeight;
		double scale = ICON_SIZE / (imageWidth * imageHeight);
		Imgproc.resize(image, icon, new Size(image.size().width * scale, image.size().height * scale));
		
		this.iconWidth = (int) icon.size().width;
		this.iconHeight = (int) icon.size().height;
		
		this.imagePixels = Util.mat2Byte(image);
		this.iconPixels = Util.mat2Byte(icon);
	
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getDate() {
		return date;
	}
	
	public Mat getImage(){
		Mat image = new Mat(new Size(imageWidth, imageHeight), openCVType);
		image.put(0, 0, getImagePixels());
		return image;
	}
	
	
	public Mat getIcon(){
		Mat icon = new Mat(new Size(iconWidth, iconHeight), openCVType);
		icon.put(0, 0, getIconPixels());
		return icon;
	}
	
	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<BoardRegion> getBoardImages() {
		return boardImages;
	}

	public void setBoardImages(Set<BoardRegion> boardImages) {
		this.boardImages = boardImages;
	}
	
	@Override
	public String toString() {
		return "SourceImage [id=" + id + ", name=" + name + ", date=" + date
				+ "]";
	}

	private byte[] getIconPixels() {
		return iconPixels;
	}

	private byte[] getImagePixels() {
		return imagePixels;
	}
	
	public void createBoardRegion(Point[] quadrangle, BoardType boardType, BoardRegionParams params){
		
		Mat image = getImage();
		
		int x1 = (quadrangle[0].x + quadrangle[3].x) / 2;
		int y1 = (quadrangle[0].y + quadrangle[1].y) / 2;
		int x2 = (quadrangle[1].x + quadrangle[2].x) / 2;
		int y2 = (quadrangle[2].y + quadrangle[3].y) / 2;
		
		Point[] fixed = new Point[4];

		fixed[0] = new Point(x1, y1);
		fixed[1] = new Point(x2, y1);
		fixed[2] = new Point(x2, y2);
		fixed[3] = new Point(x1, y2);
		
		Mat dstImage = new Mat(x2 - x1 - 1, y2 - y1 - 1, image.type());
		
		Mat dst = new Mat(4, 2, CvType.CV_32F);
		dst.put(0, 0, 0);
		dst.put(0, 1, 0);
		dst.put(1, 0, x2 - x1 - 1);
		dst.put(1, 1, 0);
		dst.put(2, 0, x2 - x1 - 1);
		dst.put(2, 1, y2 - y1 - 1);
		dst.put(3, 0, 0);
		dst.put(3, 1, y2 - y1 - 1);

		Mat src = new Mat(4, 2, CvType.CV_32F);
		src.put(0, 0, quadrangle[0].x);
		src.put(0, 1, quadrangle[0].y);
		src.put(1, 0, quadrangle[1].x);
		src.put(1, 1, quadrangle[1].y);
		src.put(2, 0, quadrangle[2].x);
		src.put(2, 1, quadrangle[2].y);
		src.put(3, 0, quadrangle[3].x);
		src.put(3, 1, quadrangle[3].y);
		
		Mat trans = Imgproc.getPerspectiveTransform(src, dst);
		Imgproc.warpPerspective(image, dstImage, trans, new Size(x2 - x1 - 1, y2 - y1 - 1));
		
		if(params == null && boardType == BoardType.BLACKBOARD){
			params = BoardRegionParams.getDefaultBBParams();
		} else if(params == null && boardType == BoardType.WHITEBOARD){
			params = BoardRegionParams.getDefaultWBParams();
		}
		
		BoardRegion region = new BoardRegion(dstImage, quadrangle, boardType, params);
		region.clearBackground();
		//region.extractTextRegions();
		
		boardImages.add(region);
		region.setSourceImage(this);
		
	}
	
}
