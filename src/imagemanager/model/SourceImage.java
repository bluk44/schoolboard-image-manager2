package imagemanager.model;

import ij.process.ImageProcessor;
import imageprocessing.Util;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import test.Test;
import mpicbg.ij.InverseTransformMapping;
import mpicbg.models.HomographyModel2D;
import mpicbg.models.IllDefinedDataPointsException;
import mpicbg.models.NotEnoughDataPointsException;
import mpicbg.models.PointMatch;

@Entity
@Table(name = "SOURCEIMAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
public class SourceImage {
	@Transient
	static float ICON_SCALE = 0.1f;

	@Id
	@GeneratedValue
	private Long id;
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

	@OneToMany(mappedBy = "sourceImage")
	Set<BoardRegion> boardImages = new HashSet<BoardRegion>();

	public SourceImage() {
	};

	public SourceImage(File file) {
		String[] tab = file.getName().split("/");
		this.name = tab[tab.length - 1];
		this.date = file.lastModified();
		BufferedImage pixels = Util.readImageFromFile(file);
		BufferedImage icon = Util.resize(pixels, ICON_SCALE);
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

	public void extractBoardRegion(Point[] quadrangle) {
		
		BufferedImage image = Util.getBufferedImage(this.pixels);

		ImageProcessor ip1 = Util.convertToImageProcessor(image);
		ImageProcessor ip2 = ip1.duplicate();

		int x1 = (quadrangle[0].x + quadrangle[3].x) / 2;
		int y1 = (quadrangle[0].y + quadrangle[1].y) / 2;
		int x2 = (quadrangle[1].x + quadrangle[2].x) / 2;
		int y2 = (quadrangle[2].y + quadrangle[3].y) / 2;
		
		Point[] fixed = new Point[4];

		fixed[0] = new Point(x1, y1);
		fixed[1] = new Point(x2, y1);
		fixed[2] = new Point(x2, y2);
		fixed[3] = new Point(x1, y2);

		ArrayList<PointMatch> m = new ArrayList<PointMatch>();

		for (int i = 0; i < 4; i++) {
			m.add(new PointMatch(new mpicbg.models.Point(new float[] {
					quadrangle[i].x, quadrangle[i].y }),
					new mpicbg.models.Point(new float[] { fixed[i].x,
							fixed[i].y })));
		}
		
		HomographyModel2D model = new HomographyModel2D();
		InverseTransformMapping<HomographyModel2D> mapping = new InverseTransformMapping<HomographyModel2D>(
				model);
		
		try {
			model.fit(m);
			mapping.mapInterpolated(ip1, ip2);

		} catch (NotEnoughDataPointsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllDefinedDataPointsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage clipped = Util.subImage(ip2.getBufferedImage(), x1, y1, x2, y2);
		BoardRegion boardRegion = new BoardRegion();
		boardRegion.setPixels(Util.getByteArray(clipped));
		boardRegion.setPerimeter(new MyQuadrangle(quadrangle));
		boardRegion.setId(12345l);
		this.getBoardImages().add(boardRegion);
		
		Test.showImage(clipped, "clipped");
		
	}

	// public BoardRegion extractBoardRegion(Polygon quadrangle){
	//
	// // extract board region from source image
	//
	// double x1 = (quadrangle.getPoint(0).x + quadrangle.getPoint(3).x) / 2;
	// double x2 = (quadrangle.getPoint(1).x + quadrangle.getPoint(2).x) / 2;
	//
	// double y1 = (quadrangle.getPoint(0).y + quadrangle.getPoint(1).y) / 2;
	// double y2 = (quadrangle.getPoint(2).y + quadrangle.getPoint(3).y) / 2;
	//
	// Polygon fixed = new Polygon(new Point(x1, y1), new Point(x2, y1), new
	// Point(x2, y2), new Point(x1, y2));
	//
	// BufferedImage pixels = Util.getBufferedImage(this.pixels);
	// BufferedImage trans = QuadrangleTransformation.transform(pixels,
	// quadrangle, fixed);
	// BufferedImage bRegion = Util.subImage(trans, (int) fixed.getPoint(0).x,
	// (int) fixed.getPoint(0).y, (int) fixed.getPoint(2).x,
	// (int) fixed.getPoint(2).y);
	// Color[] colors = new Color[2];
	//
	// BufferedImage binarized = BackgroundCleaner.run(bRegion, colors);
	//
	// // create new board image
	//
	// BoardRegion bimg = new BoardRegion();
	//
	// bimg.setIcon(Util.resize(binarized, ICON_SCALE));
	// bimg.setPixels(binarized);
	// bimg.setSource(this);
	// bimg.setSrcQuadrangle(quadrangle);
	//
	// if(colors[1] == Color.BLACK){
	// bimg.setType(BoardRegion.BoardType.BLACK);
	// } else if(colors[2] == Color.WHITE){
	// bimg.setType(BoardRegion.BoardType.WHITE);
	// }
	//
	// return bimg;
	// }

	@Override
	public String toString() {
		return "SourceImage [id=" + id + ", name=" + name + ", date=" + date
				+ "]";
	}

}
