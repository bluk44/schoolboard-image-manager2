package imageprocessing;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class AWTImageWrapper {
	
	public static ImageProcessor toImageProcessor(BufferedImage source){
		int numBands = source.getData().getNumBands();
		ImageProcessor ip = null;
		switch(numBands){
		case 1:
			ip = new ByteProcessor(source);
			break;
		case 3:
			ip = new ColorProcessor(source);
			break;
		case 4:
			ip = new ColorProcessor(source);
			break;			
		}
		ip.setRoi(new Rectangle(source.getWidth(), source.getHeight()));
		return ip;
	}
	
	public static ImagePlus toImagePlus(BufferedImage source){
		return new ImagePlus("", toImageProcessor(source));
	}
	
}
