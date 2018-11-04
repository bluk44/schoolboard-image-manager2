package imageprocessing; 

import imageprocessing.Labeling4.Region;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import test.Test;

public class TextLocating {

	private static int fgColor = Color.WHITE.getRGB();
	private static int bgColor = Color.BLACK.getRGB();

	public static Color BG = Color.WHITE;
	
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

	private static int squareSize = 47;
	/**
	 * Szuka regionow z tekstem
	 * 
	 * Dzieli zdjecie na wieksze kwadraty. Jesli w kwadracie są biale piksele caly kwadrat jest bialy,
	 * grupuje kwadrary w osobne plamy, znajduje obwod kazdej plamy
	 * 
	 * @param image
	 * @return
	 */
	public static List<Polygon> findTextPolygons(BufferedImage image) {
		
		// zmniejszenie zdjecia
		int nh = (int) Math.round(image.getHeight() / (double) squareSize);
		int nw = (int) Math.round(image.getWidth() / (double) squareSize);

		double xScale = (double) (nw * squareSize) / image.getWidth();
		double yScale = (double) (nh * squareSize) / image.getHeight();

		BufferedImage corrected = Util.resize(image, xScale, yScale);
		//
		
//		BufferedImage textRegionsMap = new BufferedImage(nw, nh,
//				BufferedImage.TYPE_BYTE_GRAY);
//		Color c = new Color(255, 255, 255);
//	
//		int mapx = 0, mapy = 0;
//		for (int i = 0; i < nh * squareSize; i += squareSize) {
//			mapx = 0;
//			for (int j = 0; j < nw * squareSize; j += squareSize) {
//				boolean textDetected = checkForText(i, j, corrected, fgColor);
//				if (textDetected)
//					textRegionsMap.setRGB(mapx, mapy, c.getRGB());
//				++mapx;
//			}
//			++mapy;
//		}

		// tworzenie mapy regionow tekstu
		BufferedImage textRegionsMap = createTextRegionsMap(corrected);
		fillIsolatedBackground(textRegionsMap);
		//
		
		List<Region> regions = Labeling4.run(Util.image2Mat(textRegionsMap));
		List<Polygon> polys = new ArrayList<Polygon>(regions.size());

		for (Region region : regions) {
			Polygon per = findPerimeter(region.image);
			AffineTransform at = new AffineTransform();
			at.setToTranslation(region.minX - 1, region.minY - 1);
			AffineTransform scale = new AffineTransform();
			scale.setToScale(squareSize / xScale, squareSize / yScale);
			scale.concatenate(at);
			polys.add( AWTUtil.copyPolygon(per, scale));
		
		}

		return polys;
		
		//return null;
	}

	private static BufferedImage createTextRegionsMap(BufferedImage image){
		int mapHeight = (int) Math.round(image.getHeight() / (double) squareSize);
		int mapWidth = (int) Math.round(image.getWidth() / (double) squareSize);
		BufferedImage textMap = new BufferedImage(mapWidth, mapHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		Color fg = new Color(255, 255, 255);
		
		int mapx = 0, mapy = 0;
		for (int i = 0; i < mapHeight * squareSize; i += squareSize) {
			mapx = 0;
			for (int j = 0; j < mapWidth * squareSize; j += squareSize) {
				boolean textDetected = checkForText(i, j, image);
				if (textDetected)
					textMap.setRGB(mapx, mapy, fg.getRGB());
				++mapx;
			}
			++mapy;
		}

		return textMap;
	}
	
	private static boolean checkForText(int i, int j, BufferedImage image) {

		for (int yidx = i; yidx < i + squareSize; yidx++) {
			for (int xidx = j; xidx < j + squareSize; xidx++) {
				if (image.getRGB(xidx, yidx) != BG.getRGB()) {
					return true;
				}
			}
		}

		return false;
	}

	private static void fillIsolatedBackground(BufferedImage textRegionsMap) {
		Color white = new Color(255, 255, 255);
		Color black = new Color(0, 0, 0);
		Color gray = new Color(128, 128, 128);

		for (int i = 0; i < textRegionsMap.getWidth(); i++) {
			if (textRegionsMap.getRGB(i, 0) == black.getRGB())
				FloodFill.run(textRegionsMap, i, 0, black, gray);
		}

		for (int i = 0; i < textRegionsMap.getWidth(); i++) {
			if (textRegionsMap.getRGB(i, textRegionsMap.getHeight() - 1) == black
					.getRGB())
				FloodFill.run(textRegionsMap, i,
						textRegionsMap.getHeight() - 1, black, gray);
		}

		for (int i = 0; i < textRegionsMap.getHeight(); i++) {
			if (textRegionsMap.getRGB(0, i) == black.getRGB())
				FloodFill.run(textRegionsMap, 0, i, black, gray);
		}

		for (int i = 0; i < textRegionsMap.getHeight(); i++) {
			if (textRegionsMap.getRGB(textRegionsMap.getWidth() - 1, i) == black
					.getRGB())
				FloodFill.run(textRegionsMap, textRegionsMap.getWidth() - 1, i,
						black, gray);
		}

		for (int i = 0; i < textRegionsMap.getHeight(); i++) {
			for (int j = 0; j < textRegionsMap.getWidth(); j++) {
				if (textRegionsMap.getRGB(j, i) == black.getRGB()) {
					textRegionsMap.setRGB(j, i, white.getRGB());
				}
			}
		}

		for (int i = 0; i < textRegionsMap.getHeight(); i++) {
			for (int j = 0; j < textRegionsMap.getWidth(); j++) {
				if (textRegionsMap.getRGB(j, i) == gray.getRGB()) {
					textRegionsMap.setRGB(j, i, black.getRGB());
				}
			}
		}
	}

	public static Polygon findPerimeter(BufferedImage image) {
		BufferedImage extended = new BufferedImage(image.getWidth() + 2,
				image.getHeight() + 2, image.getType());
		extended.getRaster().setRect(1, 1, image.getRaster());
		
		int fgColor = Color.WHITE.getRGB();
		int bgColor = Color.BLACK.getRGB();
		
		int cc = 0;
		int startX = 0, startY = 0;

		boolean end = false;

		for (int i = 0; i < extended.getHeight(); i++) {
			for (int j = 0; j < extended.getWidth(); j++) {
				cc = extended.getRGB(j, i);
				if (cc == Color.WHITE.getRGB()) {
					startX = j;
					startY = i;
					end = true;
					break;
				}
			}
			if (end == true)
				break;
		}
		if (cc == 0)
			return null;

		Polygon poly = new Polygon();
		Point start = new Point(startX, startY);
		Point next = new Point(start);

		int[][] nHood;
				
		do {
			nHood = new int[2][2];
			
			int y1 = (int) next.y - 1, x1 = (int) next.x - 1;

			int fgCount = 0;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					nHood[j][i] = extended.getRGB(x1 + j, y1 + i);
					if (nHood[j][i] == fgColor)
						++fgCount;
				}
			}

			dir nextDir = null;
			
			if (fgCount == 2) {
				if (nHood[0][1] == fgColor && nHood[1][1] == fgColor)
					nextDir = dir.E;
				else if (nHood[0][0] == fgColor && nHood[0][1] == fgColor)
					nextDir = dir.S;
				else if (nHood[0][0] == fgColor && nHood[1][0] == fgColor)
					nextDir = dir.W;
				else if (nHood[1][0] == fgColor && nHood[1][1] == fgColor)
					nextDir = dir.N;
			} else if (fgCount == 3) {
				if (nHood[0][0] == bgColor)
					nextDir = dir.N;
				else if (nHood[1][0] == bgColor)
					nextDir = dir.E;
				else if (nHood[1][1] == bgColor)
					nextDir = dir.S;
				else if (nHood[0][1] == bgColor)
					nextDir = dir.W;
				
				poly.addPoint(next.x, next.y);
			} else if (fgCount == 1) {
				if (nHood[0][0] == fgColor)
					nextDir = dir.W;
				else if (nHood[1][0] == fgColor)
					nextDir = dir.N;
				else if (nHood[1][1] == fgColor)
					nextDir = dir.E;
				else if (nHood[0][1] == fgColor)
					nextDir = dir.S;
				
				poly.addPoint(next.x, next.y);
			}

			switch (nextDir) {
			case N:
				--next.y;
				break;
			case S:
				++next.y;
				break;
			case W:
				--next.x;
				break;
			case E:
				++next.x;
				break;
			}

		} while (!next.equals(start));
		
		return poly;
	}

	private static void printNHood(int[][] nh){
		for(int i = 0; i < nh[0].length; i++){
			for(int j = 0; j < nh.length; j++){
				if(nh[j][i] == fgColor)
					System.out.print("+");
				else if(nh[j][i] == bgColor)
					System.out.print("-");
				else
					System.out.print("?");
					
			}
			System.out.println();
		}
	}

}
