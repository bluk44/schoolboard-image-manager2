package imageprocessing;

import ij.ImagePlus;
import imageprocessing.CSConverter.Conversion;
import imageprocessing.CSConverter.UnsupportedConversionException;
import imagescience.feature.Edges;
import imagescience.image.FloatImage;
import imagescience.image.Image;
import imagescience.segment.Thresholder;

public class EdgeDetection {
	
	public static void canny(ImagePlus ip, double smoothingScale, boolean suppress, double lower, double higher){
		
		int imType = ip.getType();
		
		Image imgScience = Image.wrap(ip);
		Image newImage = new FloatImage(imgScience);
		Edges edges = new Edges();
		newImage = edges.run(newImage, smoothingScale, suppress);
		
		Thresholder thres = new Thresholder();
		thres.hysteresis(newImage, lower, higher);
		
		ip.setImage(newImage.imageplus());
		
		if(imType == ImagePlus.GRAY16){
			try {
				CSConverter.run(Conversion.GRAY_16, ip);
			} catch (UnsupportedConversionException e) {

			}
		} else if(imType == ImagePlus.GRAY8){
			try {
				CSConverter.run(Conversion.GRAY_8, ip);
			} catch (UnsupportedConversionException e) {

			}
		}

	}
}
