package imageprocessing;

import imageprocessing.algorithm.DisjointSets;
import imageprocessing.algorithm.DisjointSetsForest;
import imageprocessing.matrix.MatrixI;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;

public class Labeling4 {
	private static int BG = 0;
	
	public static List<Region> run(Mat image){
		List<Region> regions = new ArrayList<Region>();
		DisjointSets<Integer> labelSets = new DisjointSetsForest<Integer>();
		
		// indeks zbioru w ktorym znajduse sie dana etykieta
		Map<Integer, Integer> labelSetIndex = new HashMap<Integer, Integer>();
		
		MatrixI labeled = Util.mat2MatrixI(image);
		int w;
		int n;
		
		int l = 1;
		
		// first run
		for(int j = 0; j < image.size().height; j++){
			for(int i = 0; i < image.size().width; i++){
				
				if(labeled.getElement(i, j) == BG) continue;
				w = getW(labeled, i, j);
				n = getN(labeled, i, j);
				
				// nowa etykieta
				if(w == 0 && n == 0){
					labeled.setElement(i, j, l);
					int idx = labelSets.makeSet(l);
					labelSetIndex.put(l, idx);
					++l;
				} else if(w != 0 && n != 0){
					labeled.setElement(i, j, w);
					labelSets.union(labelSetIndex.get(w), labelSetIndex.get(n));
				} else {
					labeled.setElement(i, j, w == 0 ? n : w);					
				}
				
				
			}
		}
		//labeled.print(System.out);

		// second run
		for(int i = 0; i < labeled.getSizeY(); i++){
			for(int j = 0; j < labeled.getSizeX(); j++){
				if(labeled.getElement(j, i) == BG) continue;
				int rootIdx = labelSets.findRootIndex(labelSetIndex.get(labeled.getElement(j, i)));
				int rootLabel = labelSets.getElement(rootIdx);
				labeled.setElement(j, i, rootLabel);
				
				
			}
		}
		
		labeled.print(System.out);
		//splitRegions(regions, data, binaryImage);
		
		//return regions;
		return null;
	}
		
	private static int getW(MatrixI m, int i, int j){
		if(i-1 > -1 && i-1 < m.getSizeX()) return m.getElement(i-1, j);
		return 0;
	}
	
	private static int getN(MatrixI m,  int i, int j){
		if(j-1 > -1 && j-1 < m.getSizeY()) return m.getElement(i, j-1);
		return 0;
	}
	
	private static void splitRegions(List<Region> regions, MatrixI labeled, BufferedImage image){
		
		Map<Integer, Region> coords = new HashMap<Integer, Region>();
		
		int l = -1;
		for(int i = 0; i < labeled.getSizeY(); i++){
			for(int j = 0; j < labeled.getSizeX(); j++){
				l = labeled.getElement(j, i);
				if(l == 0) continue;
				if(coords.get(l) == null){
					Region r = new Region();
					r.setCoords(j, i);
					coords.put(l, r);
				} else {
					coords.get(l).setCoords(j, i);
				}
			}
		}
		
		Region r;
		Color fg = Color.WHITE, bg = Color.BLACK;
		for (Integer lab : coords.keySet()) {
			r = coords.get(lab);
			BufferedImage imgReg = new BufferedImage(r.getWidth(), r.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
			
			int p;
			for(int i = r.minY; i <= r.maxY; i++){
				for(int j = r.minX; j <= r.maxX; j++){
					p = labeled.getElement(j, i);
					imgReg.setRGB(j - r.minX, i - r.minY, p == lab ? fg.getRGB() : bg.getRGB());
				}
			}
			r.image = imgReg;
			regions.add(r);
		}
				
	}
	
	static class Region{
		public int maxX = 0, maxY = 0, minX = 999999, minY = 999999;
		public BufferedImage image;
		
		public void setCoords(int x, int y){
			if(x > maxX) maxX = x;
			if(x < minX) minX = x;
			if(y > maxY) maxY = y;
			if(y < minY) minY = y;
		}
		
		public int getWidth(){
			return maxX - minX+1;
		}
		
		public int getHeight(){
			return maxY - minY+1;
		}
	}
}
