package imageprocessing;

import ij.plugin.filter.GaussianBlur;
import ij.process.ImageProcessor;

public class Filters {

	public static void gauss(ImageProcessor ip, double sigma){
		GaussianBlur gb = new GaussianBlur();
		gb.blurGaussian(ip, sigma, sigma, 0.2);
	}
	
}
