package imageprocessing.segmentation;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import imageprocessing.CSConverter;
import imageprocessing.CSConverter.Conversion;
import imageprocessing.CSConverter.UnsupportedConversionException;
import imageprocessing.ConnectedRegionsLabeling.Region;
import imageprocessing.ConnectedRegionsLabeling.Results;
import imageprocessing.Morphology.OpType;
import imageprocessing.Morphology.StructElType;
import imageprocessing.ConnectedRegionsLabeling;
import imageprocessing.ContrastEnhance;
import imageprocessing.EdgeDetection;
import imageprocessing.Filters;
import imageprocessing.Histogram;
import imageprocessing.Morphology;
import imageprocessing.Thresholding;
import imageprocessing.Util;
import imageprocessing.AWTImageWrapper;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Oddziela tło od pisma.
 * 
 * Zdjęcie wejściowe przetwarzane jest w kilku etapach:
 * 
 * - wyrównanie oświetlenia.<br/>
 * Zapobieba powstawaniu ciemnych plam przy progowaniu dużych obiektów
 * pierwszoplanowych. Polega na zamianie przestrzeni kolorów z RGB na HSB oraz
 * odjęciu niskich częstotliwości z kanału B który reprezentuje jasność, na
 * koniec ponowna zamiana na RGB.
 * 
 * - wykrycie krawędzi.<br/>
 * Celem jest zlokalizowanie pisma, czyli zmian oświetlenia o wysokiej
 * częstotliwości, efektem działania jest czarno-biały obraz z dokładnie
 * określonymi krawędziami znaków. Zastosowano metodę Cannyego, dla zdjęcia
 * czarnej tablicy określono wyższe progi intensywności ze względu na smugi po
 * wycieraniu gąbką.
 * 
 * - dylacja kwawędzi.<br/>
 * Polega na pogrubieniu wykrytych wcześniej krawędzi tak aby powstały obszary
 * jak najdokładniej obejmujące pismo. Promień dylacji jest obliczany na
 * podstawie stosunku grubośći pisma do wysokości tablicy, który wyznaczony jest
 * doświadczalnie.
 * 
 * - oznaczanie połączonych obszarów.<br/>
 * Poprawia dokładność progowania, każda litera lub grpua połączonych liter może
 * być progowana osobno.
 * 
 * - poprawa histogramu.<br/>
 * Polega na skupieniu histogramu wokół dwuch wartości średich: jednej dla tła,
 * drugiej dla pierwszego planu, pozwala to odkładniej określić próg tła,
 * wykonywane dla każdej grupy pikseli osobno.
 * 
 * - binaryzacja obrazu.<br/>
 * Szukanie wartości progowej dla każdej grupy pikseli.
 * 
 * 
 * @author Lucas Budkowski
 * 
 */
public class BackgroundCleaner {
	/**
	 * Promień maski wyrówniującej oświetnelie
	 */
	private static double LUM_CORRECTION_BLUR_RADIUS = 30;

	/**
	 * prog czarnej tablicy
	 */
	private static final int BLACKBOARD_THRESHOLD = 130;

	private static final double BB_SMOOTH = 2.0;
	private static final double BB_LOWER = 6.0;
	private static final double BB_HIGHER = 6.0;

	private static final double BB_DILATION = 7.d / 1000.d;

	private static final double WB_SMOOTH = 2.0;
	private static final double WB_LOWER = 1.0;
	private static final double WB_HIGHER = 2.0;

	private static final double WB_DILATION = 4.d / 1000.d;

	/**
	 * Funkcja z poszczególnymi krokami procesu
	 * 
	 * @param image
	 *            Zdjęcie tablicy
	 */
	public static BufferedImage run(BufferedImage image, Color[] colors) {
		ImagePlus imp = AWTImageWrapper.toImagePlus(image);
		//correctIllumination(imp);
		// ImagePlus foreground = imp.duplicate();
		return separateForeground(imp, colors);
		// return assingColors(imp, foreground);
		// Test.showImage(imp.getBufferedImage(), "foreground");
		// return imp.getBufferedImage();
	}

	private static void correctIllumination(ImagePlus image) {
		try {
			CSConverter.run(Conversion.COLOR_RGB, image);
			CSConverter.run(Conversion.STACK_HSB, image);

			ImageProcessor brightness = image.getStack().getProcessor(3);
			ImageProcessor blurred = Util
					.copy(image.getStack().getProcessor(3));

			Filters.gauss(blurred, LUM_CORRECTION_BLUR_RADIUS);
			subBChan(brightness, blurred);

			CSConverter.run(Conversion.COLOR_RGB, image);
		} catch (UnsupportedConversionException e) {
			e.printStackTrace();
		}
	}

	// protected void calculateDilationRadius(int boardHeight) {
	// dilation = (float) (boardHeight * DILATION_RATIO);
	// }

	private static BufferedImage separateForeground(ImagePlus image, Color[] colors) {

		double smoothing = 0, lower = 0, higher = 0, dilationRatio = 0;
		boolean supress = false, whiteBoard = false;

		Histogram h = new Histogram(image.getBufferedImage());
		if (h.getTotalMean() < BLACKBOARD_THRESHOLD) {
			System.out.println("image recognized as blackboard");
			smoothing = BB_SMOOTH;
			supress = true;
			lower = BB_LOWER;
			higher = BB_HIGHER;
			dilationRatio = BB_DILATION;

		} else {
			System.out.println("image recognized as whiteboard");
			whiteBoard = true;

			smoothing = WB_SMOOTH;
			supress = true;
			lower = WB_LOWER;
			higher = WB_HIGHER;
			dilationRatio = WB_DILATION;
		}

		try {
			CSConverter.run(Conversion.GRAY_8, image);
		} catch (UnsupportedConversionException e) {
			System.out.println("conversion to 8 bit gray failed");
			return null;
		}
		// kopiowanie
		ImagePlus mask = image.duplicate();

		// utworzenie maski
		EdgeDetection.canny(mask, smoothing, supress, lower, higher);
		mask.show();
		// System.out.println(mask.getType());
		float dilation = (float) (image.getHeight() * dilationRatio);
		// System.out.println("dilation radius: "+dilation);

		Morphology.run(mask, OpType.DILATE, StructElType.CIRCLE, dilation);

		// oznaczanie polaczonych regionow
		Results r = ConnectedRegionsLabeling.run(mask.getProcessor(), 0);

		// wyrownywanie histogramow
		for (Region region : r.getAllRegions()) {
			if (region.getId() == 0)
				continue;
			region.setImage(image.getProcessor());
			int[] pixels = region.getPixels();
			ContrastEnhance.equalizeHistogram(pixels, false);
			Thresholding.IJisodata(pixels);

			region.setPixels(pixels);
		}

		BufferedImage result = image.getBufferedImage();

		// binaryzacja
		
		colors[0] = whiteBoard ? Color.BLACK : Color.WHITE;
		colors[1] = whiteBoard ? Color.WHITE : Color.BLACK;

		for (int i = 0; i < result.getHeight(); i++) {
			for (int j = 0; j < result.getWidth(); j++) {
				if (result.getRGB(j, i) != colors[0].getRGB()) {
					result.setRGB(j, i, colors[1].getRGB());
				}
			}
		}

		return result;
	}

	/**
	 * Odejmuje niskie częstotliwosci oświetlenia
	 * 
	 * @param ipBR
	 *            kanał jasności oryginalnego zdjęcia
	 * @param ipGA
	 *            kanał jasności rozmytego zdjęcia
	 */
	private static void subBChan(ImageProcessor ipBR, ImageProcessor ipGA) {
		int w = ipBR.getWidth(), h = ipBR.getHeight();

		float[] lum = new float[w * h];
		float[] blur = new float[w * h];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				lum[i * w + j] = ipBR.getPixelValue(j, i);
			}
		}

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				blur[i * w + j] = ipGA.getPixelValue(j, i);
			}
		}

		float mean = 0;
		for (int i = 0; i < blur.length; i++) {
			mean += blur[i];
		}
		mean = Math.round(mean /= blur.length);

		for (int i = 0; i < lum.length; i++) {
			float val = lum[i] - blur[i] + mean;
			lum[i] = val < 255 ? val : 255;
			lum[i] = lum[i] > 0 ? lum[i] : 0;

		}

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				ipBR.putPixelValue(j, i, lum[i * w + j]);
			}
		}

	}
}
