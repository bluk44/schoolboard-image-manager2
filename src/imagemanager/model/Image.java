package imagemanager.model;

import imageprocessing.Util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import org.opencv.core.Mat;
import org.opencv.core.Size;

@Embeddable
public class Image {
	@Basic
	public int width;
	@Basic
	public int height;
	
	@Enumerated(EnumType.STRING)
	@Basic
	public ImageType type;
	
	@Lob
	@Basic
	public byte[] pixels;
	
	public Image(){
		
	}
	
	public Image(int width, int height, byte[] pixels, ImageType type){
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		this.type = type;
	}
	
	public Image(Mat mat){
		this.width = mat.width();
		this.height = mat.height();
		
		int openCVType = mat.type();
		
		switch(openCVType){
		
		case 16:
			this.type = ImageType.CV_8UC3;
		}
		
		
		this.pixels = Util.mat2Byte(mat);
	}
	
	// kopiuje pixele
	public Mat getMat(){
		Mat mat = new Mat(new Size(width, height), type.val);
		mat.put(0, 0, pixels);

		return mat;
	}
		
	public void update(Mat mat){
		this.width = mat.width();
		this.height = mat.height();
		
		int openCVType = mat.type();
		
		switch(openCVType){
		
		case 16:
			this.type = ImageType.CV_8UC3;
		}
		
		this.pixels = Util.mat2Byte(mat);
	}
	
	public void update(BufferedImage bi){
		this.pixels = ((DataBufferByte) bi.getRaster().getDataBuffer())
				.getData();
	}
	
	public enum ImageType {
		 
		CV_8UC3(16);
		
		private int val;
		
		private ImageType(int val){
			this.val = val;
		}
		
		public int getVal(){
			return val;
		}
	}
}
