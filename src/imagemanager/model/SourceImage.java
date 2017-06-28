package imagemanager.model;

import imagemanager.model.BoardRegion.BoardType;
import imageprocessing.Util;

import java.awt.Point;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import org.opencv.highgui.Highgui;
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

	@AttributeOverrides({
		@AttributeOverride(name="width", column=@Column(name="imageWidth")),
		@AttributeOverride(name="height", column=@Column(name="imageHeight")),
		@AttributeOverride(name="type", column=@Column(name="imageType")),
		@AttributeOverride(name="pixels", column=@Column(name="imagePixels"))
	})
	@Embedded
	private Image icon;
	
	@AttributeOverrides({
		@AttributeOverride(name="width", column=@Column(name="iconWidth")),
		@AttributeOverride(name="height", column=@Column(name="iconHeight")),
		@AttributeOverride(name="type", column=@Column(name="iconType")),
		@AttributeOverride(name="pixels", column=@Column(name="iconPixels"))
	})
	@Embedded
	private Image image;
	
	@ManyToMany(mappedBy = "images")
	Set<Category> categories = new HashSet<Category>();

	@OneToMany(cascade={CascadeType.ALL}, mappedBy = "sourceImage")
	Set<BoardRegion> boardImages = new HashSet<BoardRegion>();

	public SourceImage() {
	};

	public SourceImage(Long id, String name, Long date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}
	
	public SourceImage(File file) {
		String[] tab = file.getName().split("/");
		this.name = tab[tab.length - 1];
		this.date = file.lastModified();
		Mat image = Highgui.imread(file.getPath());
		Mat icon = new Mat();
				
		int imageHeight = (int) image.size().height;
		int imageWidth = (int) image.size().width;
				
		double scale = ICON_SIZE / (imageWidth * imageHeight);
		
		Imgproc.resize(image, icon, new Size(image.size().width * scale, image.size().height * scale));
				
		this.image = new Image(image);
		this.icon = new Image(icon);
	
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
	
	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
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

	public BoardRegion createBoardRegion(Point[] quadrangle, BoardType boardType, BoardRegionParams params){
		System.out.println("create board region called");
		Mat image = this.image.getMat();
		
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
				
		boardImages.add(region);
		region.setSourceImage(this);
		
		return region;
	}
	
}
