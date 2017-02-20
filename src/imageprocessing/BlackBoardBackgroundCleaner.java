package imageprocessing;

import imagemanager.model.BoardRegion.BoardType;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import test.Test;

public class BlackBoardBackgroundCleaner extends BackgroundCleaner {

	{
		this.blurMaskSize = 11;
		this.thresholdBlockSize = 21;
		this.thresholdConstant = -7;
		this.backgroundColor = new double[] {68, 61, 58};
	}
	
	@Override
	protected Mat createMask(Mat image) {
		Mat mask = image.clone();
		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(mask, mask, new Size(blurMaskSize, blurMaskSize), 0, 0);
		Imgproc.adaptiveThreshold(mask, mask, 255.0,
				Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,
				thresholdBlockSize, thresholdConstant);
		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_GRAY2BGR);
		
		Test.showImage(Util.mat2Img(mask), "mask");
	
		return mask;
	}

	@Override
	public BoardType getBoardType() {
		return BoardType.BLACKBOARD;
	}

}
