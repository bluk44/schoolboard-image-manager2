package imageprocessing;

import java.awt.Point;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class BoardRegionExtractor {
	
	BackgroundCleaner bgCleaner;
	
	//private Mat 
	
	public Mat run(Mat image, Point[] quadrangle){
			
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
		//Test.showImage(Util.mat2Img(dstImage), "dstimage");
		
		return dstImage;
	}

	public BackgroundCleaner getBgCleaner() {
		return bgCleaner;
	}

	public void setBgCleaner(BackgroundCleaner bgCleaner) {
		this.bgCleaner = bgCleaner;
	}
}
