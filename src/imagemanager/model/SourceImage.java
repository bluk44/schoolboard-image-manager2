package imagemanager.model;

import imageprocessing.Util;

import java.awt.image.BufferedImage;
import java.io.File;
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

@Entity
@Table(name="SOURCEIMAGE", uniqueConstraints={@UniqueConstraint(columnNames={"NAME"})})
public class SourceImage{
	@Transient
	static float ICON_SCALE = 0.1f;
	
	@Id
	@GeneratedValue	
	private Long id;
	private String name;
	private Long date;
	
	@Lob
	@Basic(fetch=FetchType.EAGER)
	private byte[] icon;
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] pixels;
	
	@ManyToMany(mappedBy="images")
	Set<Category> categories = new HashSet<Category>();
	
	@OneToMany(mappedBy="sourceImage")
	Set<BoardRegion> boardImages = new HashSet<BoardRegion>();
	
	public SourceImage(){};
	
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
		
//	public BoardRegion extractBoardRegion(Polygon quadrangle){
//		
//		// extract board region from source image
//		
//		double x1 = (quadrangle.getPoint(0).x + quadrangle.getPoint(3).x) / 2;
//		double x2 = (quadrangle.getPoint(1).x + quadrangle.getPoint(2).x) / 2;
//
//		double y1 = (quadrangle.getPoint(0).y + quadrangle.getPoint(1).y) / 2;
//		double y2 = (quadrangle.getPoint(2).y + quadrangle.getPoint(3).y) / 2;
//		
//		Polygon fixed = new Polygon(new Point(x1, y1), new Point(x2, y1), new Point(x2, y2), new Point(x1, y2));
//		
//		BufferedImage pixels = Util.getBufferedImage(this.pixels);
//     	BufferedImage trans = QuadrangleTransformation.transform(pixels, quadrangle, fixed);
//		BufferedImage bRegion = Util.subImage(trans, (int) fixed.getPoint(0).x,
//				(int) fixed.getPoint(0).y, (int) fixed.getPoint(2).x,
//				(int) fixed.getPoint(2).y);
//		Color[] colors = new Color[2];
//		
//		BufferedImage binarized = BackgroundCleaner.run(bRegion, colors);
//		
//		// create new board image
//		
//		BoardRegion bimg = new BoardRegion();
//		
//		bimg.setIcon(Util.resize(binarized, ICON_SCALE));
//		bimg.setPixels(binarized);
//		bimg.setSource(this);
//		bimg.setSrcQuadrangle(quadrangle);
//		
//		if(colors[1] == Color.BLACK){
//			bimg.setType(BoardRegion.BoardType.BLACK);
//		} else if(colors[2] == Color.WHITE){
//			bimg.setType(BoardRegion.BoardType.WHITE);
//		}
//				
//		return bimg;
//	}
	
	@Override
	public String toString() {
		return "SourceImage [id=" + id + ", name=" + name + ", date=" + date
				+ "]";
	}

}
