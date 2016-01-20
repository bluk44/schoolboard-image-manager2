package imageprocessing;

import fiji.threshold.Auto_Threshold;
import ij.CompositeImage;
import ij.ImagePlus;
import ij.ImageStack;
import ij.LookUpTable;
import ij.Prefs;
import ij.process.AutoThresholder.Method;
import ij.process.ImageProcessor;

public class Thresholding {

	public static void maxEntropy(ImagePlus imp) {
		int nSlices = imp.getStackSize();

		if ((imp.getBitDepth() != 8)) {
			ImageStack stack1 = imp.getStack();
			ImageStack stack2 = new ImageStack(imp.getWidth(), imp.getHeight());
			for (int i = 1; i <= nSlices; i++) {
				String label = stack1.getSliceLabel(i);
				ImageProcessor ip = stack1.getProcessor(i);
				ip.resetMinAndMax();
				stack2.addSlice(label, ip.convertToByte(true));
			}
			imp.setStack(null, stack2);
		}

		ImageStack stack = imp.getStack();
		for (int i = 1; i <= nSlices; i++) {
			ImageProcessor ip = stack.getProcessor(i);
			ip.setAutoThreshold(Method.MaxEntropy, false, ImageProcessor.NO_LUT_UPDATE);
			double minThreshold = ip.getMinThreshold();
			double maxThreshold = ip.getMaxThreshold();
			int[] lut = new int[256];
			for (int j = 0; j < 256; j++) {
				if (j >= minThreshold && j <= maxThreshold)
					lut[j] = (byte) 255;
				else
					lut[j] = 0;
			}
			ip.applyTable(lut);
		}
		stack.setColorModel(LookUpTable.createGrayscaleColorModel(!Prefs.blackBackground));
		imp.setStack(null, stack);
		imp.getProcessor().setThreshold(255, 255, ImageProcessor.NO_LUT_UPDATE);
		if (imp.isComposite()) {
			CompositeImage ci = (CompositeImage) imp;
			ci.setMode(CompositeImage.GRAYSCALE);
			ci.resetDisplayRanges();
			ci.updateAndDraw();
		}
			
	}
	
	public static void IJisodata(int[] pixels){
		int[] histogram = new int[256];
		for(int i = 0; i < pixels.length; i++)
			++histogram[pixels[i]];
		int t = Auto_Threshold.IJDefault(histogram);
		
		for(int i = 0; i < pixels.length; i++){
			if(pixels[i] < t) pixels[i] = 0;
			else pixels[i] = 255;
		}
		
	}
		

}
