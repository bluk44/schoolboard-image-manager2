package imageprocessing;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageWindow;
import ij.process.ImageConverter;
import ij.process.StackConverter;

public class CSConverter {

	public enum Conversion {
		GRAY_8, GRAY_16, GRAY_32, COLOR_RGB, STACK_RGB, STACK_HSB
	}
	
	public static class UnsupportedConversionException extends Exception{
		
	}
	
	public static void run(Conversion conv, ImagePlus imp) throws UnsupportedConversionException {
		int type = imp.getType();
		ImageStack stack = null;
		if (imp.getStackSize() > 1)
			stack = imp.getStack();
		ImageWindow win = imp.getWindow();

		if (stack != null) {
			// do stack converions
			if (stack.isRGB() && conv == Conversion.COLOR_RGB) {
				new ImageConverter(imp).convertRGBStackToRGB();
				if (win != null)
					new ImageWindow(imp, imp.getCanvas());
			} else if (stack.isHSB() && conv == Conversion.COLOR_RGB) {
				new ImageConverter(imp).convertHSBToRGB();
				if (win != null)
					new ImageWindow(imp, imp.getCanvas());
			} else if (conv == Conversion.GRAY_8)
				new StackConverter(imp).convertToGray8();
			else if (conv == Conversion.GRAY_16)
				new StackConverter(imp).convertToGray16();
			else if (conv == Conversion.GRAY_32)
				new StackConverter(imp).convertToGray32();
			else if (conv == Conversion.COLOR_RGB)
				new StackConverter(imp).convertToRGB();
			else if (conv == Conversion.STACK_RGB)
				new StackConverter(imp).convertToRGBHyperstack();
			else if (conv == Conversion.STACK_HSB)
				new StackConverter(imp).convertToHSBHyperstack();
			else
				throw new UnsupportedConversionException();

		} else {
			// do single image conversions
			ImageConverter ic = new ImageConverter(imp);
			if (conv == Conversion.GRAY_8)
				ic.convertToGray8();
			else if (conv == Conversion.GRAY_16)
				ic.convertToGray16();
			else if (conv == Conversion.GRAY_32)
				ic.convertToGray32();
			else if (conv == Conversion.STACK_RGB)
				ic.convertToRGBStack();
			else if (conv == Conversion.STACK_HSB)
				ic.convertToHSB();
			else if (conv == Conversion.COLOR_RGB)
				ic.convertToRGB();
			else
				throw new UnsupportedConversionException();
		}
		
	}

}
