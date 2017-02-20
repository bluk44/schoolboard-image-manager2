package imageprocessing;

import java.awt.Polygon;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class TextLocating2 {
	
	private static int fgColor = 255;
	private static int bgColor = 0;
	
	private enum dir {
		N, S, W, E;
		
		@Override
		public String toString(){
			if(this == N) return "N";
			else if(this == S) return "S";
			else if(this == W) return "W";
			else if(this == E) return "E";
			return "";
		}
	}
	
	private static int squareSize = 10;
	
	public static List<Polygon> findTextPolygons(Mat image){
		
		//int nh = (int) Math.round(image.size().height / (double) squareSize);
		//int nw = (int) Math.round(image.size().width / (double) squareSize);

		double nh = image.size().height / (double) squareSize;
		double nw = image.size().width / (double) squareSize;
		
		double xScale = (double) (nw * squareSize) / image.size().width;
		double yScale = (double) (nh * squareSize) / image.size().height;
		
		Mat resized = new Mat(image.size(), image.type());
		Size nSize = new Size(image.size().width*xScale, image.size().height*yScale);
		System.out.println(nSize);
		Imgproc.resize(image, resized, nSize);
		
		return null;
	}
	
	public static int getSquareSize() {
		return squareSize;
	}

	public static void setSquareSize(int squareSize) {
		TextLocating2.squareSize = squareSize;
	}
	

}
