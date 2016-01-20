package imageprocessing;

import ij.ImagePlus;
import ij.process.ImageProcessor;


public class ContrastEnhance {
	
	public static void equalizeHistogram(ImagePlus image){
		
		float max = image.getWidth() * image.getHeight();
		ImageProcessor ip = image.getProcessor();
		
		// obliczanie histogramu skumulowanego
		float[] CDF = new float[256];
		for(int i = 0; i < ip.getHeight(); i++){
			for(int j = 0; j < ip.getWidth(); j++){
				++CDF[(int) ip.getPixelValue(j, i)]; 
			}
		}
		
		CDF[0] = CDF[0] * 255 / max;
		for(int i = 1; i < CDF.length; i++){ 
			CDF[i] = CDF[i] * 255 / max;
			CDF[i] += CDF[i-1];
		}
		
		// zmiana jasnosci wedlug histogramu skumulowanego
		for(int i = 0; i < ip.getHeight(); i++){
			for(int j = 0; j < ip.getWidth(); j++){
				int newVal = Math.round(CDF[(int) ip.getPixelValue(j, i)]);
				ip.putPixelValue(j, i, newVal);
			}
		}
		
	}
	
	public static void equalizeHistogram(int[] pixels, boolean classicEqualization){
		
		int max = 255;
		int	range = 255;
		
		int[] histogram = new int[256];
		for(int i = 0; i < pixels.length; i++)
			++histogram[pixels[i]];
		
		double sum;
		sum = getWeightedValue(histogram, 0, false);
		for (int i=1; i<max; i++)
			sum += 2 * getWeightedValue(histogram, i, false);
		sum += getWeightedValue(histogram, max, false);
		double scale = range/sum;
		int[] lut = new int[range+1];
		lut[0] = 0;
		sum = getWeightedValue(histogram, 0, false);
		for (int i=1; i<max; i++) {
			double delta = getWeightedValue(histogram, i, false);
			sum += delta;
			lut[i] = (int)Math.round(sum*scale);
			sum += delta;
		}
		lut[max] = max;
		applyLut(pixels, lut);
	}
	
	private static double getWeightedValue(int[] histogram, int i, boolean classicEqualization) {
		int h = histogram[i];
		if (h<2 || classicEqualization) return h;
		return Math.sqrt((h));
	}
	
	private static void applyLut(int[] pixels, int[] lut){
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = lut[pixels[i]];
		}
	}

}
