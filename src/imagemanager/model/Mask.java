package imagemanager.model;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import imagemanager.model.BoardRegion.BoardType;
import imagemanager.model.Image.ImageType;
import imageprocessing.Util;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

@Embeddable
public class Mask {
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
	
	public Mask(){}
	
	public Mask(int width, int height, byte[] pixels, ImageType type){
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		this.type = type;
	}
	
	public Mask(Mat image, BoardType bType, BoardRegionParams params){
		this.width = image.width();
		this.height = image.height();
		
		int openCVType = image.type();
		
		switch(openCVType){
		
		case 16:
			this.type = ImageType.CV_8UC3;
		}
		
		Mat mask = image.clone();
		switch (bType) {
		case BLACKBOARD:
		// create mask
			Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			Imgproc.GaussianBlur(mask, mask, new Size(params.getBlurMaskSize(), params.getBlurMaskSize()), 0, 0);
			Imgproc.adaptiveThreshold(mask, mask, 255.0,
					Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,
					params.getThreshholdBlockSize(), params.getThresholdConstant());
			Imgproc.cvtColor(mask, mask, Imgproc.COLOR_GRAY2BGR);	

			break;

		case WHITEBOARD:
		// create mask
			Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			Imgproc.GaussianBlur(mask, mask, new Size(params.getBlurMaskSize(), params.getBlurMaskSize()), 0, 0);
			Imgproc.adaptiveThreshold(mask, mask, 255.0,
					Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV,
					params.getThreshholdBlockSize(), params.getThresholdConstant());
			Imgproc.cvtColor(mask, mask, Imgproc.COLOR_GRAY2BGR);		
			
			break;
			
		}
		
		// save mask
		this.pixels = Util.mat2Byte(mask);

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
