package imageprocessing;

import imagemanager.model.BoardRegion.BoardType;
import imagemanager.model.BoardRegionParams;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import test.Test;

public abstract class BackgroundCleaner {

	protected int blurMaskSize;
	protected int thresholdBlockSize;
	protected int thresholdConstant;
	protected double[] backgroundColor = new double[3];
			
	public int getBlurMaskSize() {
		return blurMaskSize;
	}
	public void setBlurMaskSize(int blurMaskSize) {
		this.blurMaskSize = blurMaskSize;
	}
	public int getThresholdBlockSize() {
		return thresholdBlockSize;
	}
	public void setThresholdBlockSize(int thresholdBlockSize) {
		this.thresholdBlockSize = thresholdBlockSize;
	}
	public int getThresholdConstant() {
		return thresholdConstant;
	}
	public void setThresholdConstant(int thresholdConstant) {
		this.thresholdConstant = thresholdConstant;
	}
	public double[] getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(double[] backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public void setParameters(BoardRegionParams params){
		this.blurMaskSize = params.getBlurMaskSize();
		this.thresholdBlockSize = params.getThreshholdBlockSize();
		this.thresholdConstant = params.getThresholdConstant();
	}
	public void run(Mat image){
		
		// tworzenie maski
		Mat mask = createMask(image);
		
		// napisy na pierwszym planie
		Mat fg = image.clone();
		Core.bitwise_and(fg, mask, fg); 
		
		// tlo
		Mat bg = new Mat(image.size(), image.type(), new Scalar(backgroundColor));
		Core.bitwise_not(mask, mask);
		//Test.showImage(Util.mat2Img(mask), "mask");
		Core.bitwise_and(mask, bg, bg);
		Core.bitwise_or(bg, fg, image);
		Test.showImage(Util.mat2Img(image), "image");

		//Test.showImage(Util.mat2Img(bg), "bg");
		
		
		//Core.bitwise_and(image, mask, image);
		//tlo
		//Test.showImage(Util.mat2Img(image), "image");	
		
		
//		Core.bitwise_not(mask, mask);
//		Core.bitwise_or(image, mask, image);
//		Core.bitwise_not(mask, mask);
//
//		double[] green = {0, 256, 0};
//		Mat background = new Mat(image.size(), image.type(), new Scalar(green));
//		Core.bitwise_and(background, mask, background);
//		Test.showImage(Util.mat2Img(background), "background");
//		Core.bitwise_not(background, background);
//		Test.showImage(Util.mat2Img(background), "inv background");
//		
//		Test.showImage(Util.mat2Img(image), "image");
		
	}
	
	protected abstract Mat createMask(Mat image);
	public abstract BoardType getBoardType();
}
