package imageprocessing;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class WhiteBoardImageProcessor extends BoardImageProcessor {
	
	{
		this.blurMaskSize = 7;
		this.thresholdBlockSize = 11;
		this.thresholdConstant = 3;
		this.backgroundColor = new double[] {255, 255, 255};
	}
	
	
	
	@Override
	protected Mat createMask(Mat image) {
		Mat mask = image.clone();
		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(mask, mask, new Size(blurMaskSize, blurMaskSize), 0, 0);
		Imgproc.adaptiveThreshold(mask, mask, 255.0,
				Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV,
				thresholdBlockSize, thresholdConstant);
		//Imgproc.cvtColor(mask, mask, Imgproc.COLOR_GRAY2BGR);
			
		return mask;
	}
	
	
}
