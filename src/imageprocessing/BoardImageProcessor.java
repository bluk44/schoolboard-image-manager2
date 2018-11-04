package imageprocessing;

import imagemanager.model.BoardRegion;
import imagemanager.model.SourceImage;
import imagemanager.view.ImageViewComponent;

import java.awt.Point;
import java.awt.Polygon;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import test.Test;

public abstract class BoardImageProcessor {

	protected int blurMaskSize;
	protected int thresholdBlockSize;
	protected int thresholdConstant;
	protected double[] backgroundColor = new double[3];

	private int textMapSectorSize = 20;

	public BoardRegion run(SourceImage image, Point[] quadrangle) {
		Mat boardROI = getBoardROI(image.getImage(), quadrangle);
		Mat mask = createMask(boardROI);

		System.out.println(mask.channels());
		Test.showImage(Util.mat2Img(mask), "mask");
		
		Polygon[] textRegions = findTextRegions(mask);
		
				ImageViewComponent ic = new ImageViewComponent();
				ic.initialize();
				ic.setImage(Util.mat2Img(mask));

				for (int i = 0; i < textRegions.length; i++) {
					ic.putShape("p" + i, textRegions[i]);

				}

				Test.showComponent(ic, "image");		
				
		return null;
	}

	private Mat getBoardROI(Mat image, Point[] quadrangle) {

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
		Imgproc.warpPerspective(image, dstImage, trans, new Size(x2 - x1 - 1,
				y2 - y1 - 1));

		return dstImage;

	}

	protected abstract Mat createMask(Mat image);

	private Polygon[] findTextRegions(Mat img) {
		Mat binary = Util.getBinaryImage(img);

		Mat textMap = createTextMap(binary);
		textMap = ConnectedComponentsLabeling.run(textMap);

		int[] label = new int[1];

		for (int i = 0; i < textMap.height(); i++) {
			for (int j = 0; j < textMap.width(); j++) {
				textMap.get(i, j, label);
				labelTextInSector(i, j, label, binary);
			}
		}

		Polygon[] bounds = getRegions(binary);
		
		return bounds;

	}

	private Polygon[] getRegions(Mat image) {
		Map<Integer, Region> map = new HashMap<Integer, Region>();

		for (int i = 0; i < image.height(); i++) {
			for (int j = 0; j < image.width(); j++) {

				int label = (int) image.get(i, j)[0];

				if (label != 0) {
					Region r = map.get(label);
					if (r == null) {
						r = new Region();
						map.put(label, r);
					}
					if (j < r.minX)
						r.minX = j;
					if (j > r.maxX)
						r.maxX = j;
					if (i < r.minY)
						r.minY = i;
					if (i > r.maxY)
						r.maxY = i;
				}
			}
		}

		Collection<Region> col = map.values();
		Polygon[] bounds = new Polygon[col.size()];
		int i = 0;
		for (Region region : col) {
			bounds[i++] = region.getBounds();
		}

		return bounds;
	}

	private Mat createTextMap(Mat binaryImage) {

		int mapWidth = (int) Math.ceil(binaryImage.width()
				/ (double) textMapSectorSize);
		int mapHeight = (int) Math.ceil(binaryImage.height()
				/ (double) textMapSectorSize);

		int[] fg = { 1 };
		int[] bg = { 0 };

		Mat textMap = new Mat(new Size(mapWidth, mapHeight), CvType.CV_32SC1);

		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				textMap.put(i, j, bg);
			}
		}

		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				if (scanForWhitePixel(binaryImage, i, j)) {
					textMap.put(i, j, fg);
				}
			}
		}

		return textMap;
	}

	private boolean scanForWhitePixel(Mat image, int y, int x) {
		for (int i = y * textMapSectorSize; i < y * textMapSectorSize
				+ textMapSectorSize; i++) {
			for (int j = x * textMapSectorSize; j < x * textMapSectorSize
					+ textMapSectorSize; j++) {

				if (i > image.height() - 1)
					break;
				if (j > image.width() - 1)
					break;

				if (image.get(i, j)[0] != 0) {
					return true;
				}
			}
		}
		return false;
	}

	private void labelTextInSector(int y, int x, int[] label, Mat image) {
		for (int i = y * textMapSectorSize; i < y * textMapSectorSize
				+ textMapSectorSize; i++) {
			for (int j = x * textMapSectorSize; j < x * textMapSectorSize
					+ textMapSectorSize; j++) {
				if (i > image.height() - 1)
					break;
				if (j > image.width() - 1)
					break;

				if (image.get(i, j)[0] != 0) {
					image.put(i, j, label);
				}
			}
		}
	}

	private class Region {
		public Integer minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE,
				maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

		public Polygon getBounds() {
			int[] xpoints = { minX, maxX, maxX, minX };
			int[] ypoints = { minY, minY, maxY, maxY };

			Polygon bounds = new Polygon(xpoints, ypoints, 4);

			return bounds;
		}

	}
}
