package imageprocessing;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import test.Test;

public class LocalAdaptiveThresholding {

	public static BufferedImage threshold(BufferedImage image, int rad, double offset) {

		if (image.getType() != BufferedImage.TYPE_BYTE_GRAY) {
			return null;
		}
		WritableRaster rast = image.getRaster();

		int imgW = image.getWidth(), imgH = image.getHeight();
		BufferedImage result = new BufferedImage(imgW, imgH, image.getType());

		for (int i = 0; i < imgW; i++) {
			for (int j = 0; j < imgH; j++) {
				double s = rast.getSampleDouble(i, j, 0);
				int[] samples = getSamples(i, j, rad, rast);
				double avg = getAve(samples);
				if (s < avg + offset * avg) {
					result.getRaster().setSample(i, j, 0, 0.0);
				} else {
					result.getRaster().setSample(i, j, 0, 255.0);

				}

			}
		}
		
		return result;
	}

	private static double getAve(int[] tab) {
		double sum = 0;
		for (int i : tab) {
			sum += i;
		}

		return sum / tab.length;
	}

	private static int[] getSamples(int i, int j, int r, WritableRaster rast) {

		int imgw = rast.getWidth(), imgh = rast.getHeight();

		int x0 = (i - r) > 0 ? i - r : 0;
		int y0 = (j - r) > 0 ? j - r : 0;

		int x1 = (i + r) < (imgw - 1) ? i + r : imgw - 1;
		int y1 = (j + r) < (imgh - 1) ? j + r : imgh - 1;

		int fw = x1 - x0 + 1, fh = y1 - y0 + 1;

		int[] samples = new int[fw * fh];

		rast.getSamples(x0, y0, fw, fh, 0, samples);

		return samples;
	}

	public static void printTab(int[] tab) {
		for (int i : tab) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
}
