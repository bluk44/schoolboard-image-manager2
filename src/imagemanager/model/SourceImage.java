package imagemanager.model;

import imageprocessing.Util;

import java.awt.Point;
import java.awt.image.BufferedImage;
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

import test.Test;

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
	private byte[] icon;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] pixels;

	@ManyToMany(mappedBy = "images")
	Set<Category> categories = new HashSet<Category>();

	@OneToMany(cascade={CascadeType.ALL}, mappedBy = "sourceImage")
	Set<BoardRegion> boardImages = new HashSet<BoardRegion>();

	public SourceImage() {
	};

	public SourceImage(File file) {
		String[] tab = file.getName().split("/");
		this.name = tab[tab.length - 1];
		this.date = file.lastModified();
		BufferedImage pixels = Util.readImageFromFile(file);
		float imageSize = pixels.getWidth() * pixels.getHeight();
		float scale = ICON_SIZE / imageSize;
		System.out.println("scale "+scale);
		BufferedImage icon = Util.resize(pixels, scale, scale);
		this.pixels = Util.getByteArray(pixels);
		this.icon = Util.getByteArray(icon);

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public byte[] getPixels() {
		return pixels;
	}

	public void setPixels(byte[] pixels) {
		this.pixels = pixels;
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

//	public void extractBoardRegion(Point[] quadrangle) {
//		
//		BufferedImage image = Util.getBufferedImage(this.pixels);
//		
//		
//		ImageProcessor ip1 = Util.convertToImageProcessor(image);
//		ImageProcessor ip2 = ip1.duplicate();
//
//		int x1 = (quadrangle[0].x + quadrangle[3].x) / 2;
//		int y1 = (quadrangle[0].y + quadrangle[1].y) / 2;
//		int x2 = (quadrangle[1].x + quadrangle[2].x) / 2;
//		int y2 = (quadrangle[2].y + quadrangle[3].y) / 2;
//		
//		Point[] fixed = new Point[4];
//
//		fixed[0] = new Point(x1, y1);
//		fixed[1] = new Point(x2, y1);
//		fixed[2] = new Point(x2, y2);
//		fixed[3] = new Point(x1, y2);
//
//		ArrayList<PointMatch> m = new ArrayList<PointMatch>();
//
//		for (int i = 0; i < 4; i++) {
//			m.add(new PointMatch(new mpicbg.models.Point(new float[] {
//					quadrangle[i].x, quadrangle[i].y }),
//					new mpicbg.models.Point(new float[] { fixed[i].x,
//							fixed[i].y })));
//		}
//		
//		HomographyModel2D model = new HomographyModel2D();
//		InverseTransformMapping<HomographyModel2D> mapping = new InverseTransformMapping<HomographyModel2D>(
//				model);
//		
//		try {
//			model.fit(m);
//			mapping.mapInterpolated(ip1, ip2);
//
//		} catch (NotEnoughDataPointsException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllDefinedDataPointsException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		BufferedImage clipped = Util.subImage(ip2.getBufferedImage(), x1, y1, x2, y2);
//		
//		// czyszczenie tla
//		Color[] colors = new Color[2];
//		BufferedImage binarized = BackgroundCleaner.run(clipped, colors);
//		
//		BoardRegion boardRegion = new BoardRegion();

//		boardRegion.setPerimeter(new MyQuadrangle(quadrangle));
//		boardImages.add(boardRegion);
//		boardRegion.setSourceImage(this);
//		Test.showImage(clipped, "clipped");
//		
//	}

	public void extractBoardRegion(Point[] quadrangle){
		
		BufferedImage image = Util.getBufferedImage(this.pixels);
		Mat imageMat = Util.image2Mat(image);
		
		int x1 = (quadrangle[0].x + quadrangle[3].x) / 2;
		int y1 = (quadrangle[0].y + quadrangle[1].y) / 2;
		int x2 = (quadrangle[1].x + quadrangle[2].x) / 2;
		int y2 = (quadrangle[2].y + quadrangle[3].y) / 2;
		
		Point[] fixed = new Point[4];

		fixed[0] = new Point(x1, y1);
		fixed[1] = new Point(x2, y1);
		fixed[2] = new Point(x2, y2);
		fixed[3] = new Point(x1, y2);
		
		Mat dstImage = new Mat(x2 - x1 - 1, y2 - y1 - 1, imageMat.type());
		
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
		Imgproc.warpPerspective(imageMat, dstImage, trans, new Size(x2 - x1 - 1, y2 - y1 - 1));
		Test.showImage(Util.mat2Img(dstImage), "dstimage");

	}
	
	
	@Override
	public String toString() {
		return "SourceImage [id=" + id + ", name=" + name + ", date=" + date
				+ "]";
	}

}
