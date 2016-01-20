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

public class Labeling4 {
	private static int FG = 255;
	private static int BG = 0;
	
	public static List<Region> run(BufferedImage binaryImage){
		List<Region> regions = new ArrayList<Region>();
		MatrixI data = Util.grayToMatrixI(binaryImage);
		DisjointSets<Integer> labelSets = new DisjointSetsForest<Integer>();
		
		// indeks zbioru w ktorym znajduse sie dana etykieta
		Map<Integer, Integer> labelSetIndex = new HashMap<Integer, Integer>();
		
		int w;
		int n;
		
		int l = 1;
		
		// first run
		for(int i = 0; i < data.getSizeY(); i++){
			for(int j = 0; j < data.getSizeX(); j++){
				
				if(data.getElement(j, i) == BG) continue;
				w = getW(data, j, i);
				n = getN(data, j, i);
				
				// nowa etykieta
				if(w == 0 && n == 0){
					data.setElement(j, i, l);
					int idx = labelSets.makeSet(l);
					labelSetIndex.put(l, idx);
					++l;
				} else if(w != 0 && n != 0){
					data.setElement(j, i, w);
					labelSets.union(labelSetIndex.get(w), labelSetIndex.get(n));
				} else {
					data.setElement(j, i, w == 0 ? n : w);
				}
				
				
			}
		}
		data.print(System.out);
		
		// second run
		for(int i = 0; i < data.getSizeY(); i++){
			for(int j = 0; j < data.getSizeX(); j++){
				if(data.getElement(j, i) == BG) continue;
				int rootIdx = labelSets.findRootIndex(labelSetIndex.get(data.getElement(j, i)));
				int rootLabel = labelSets.getElement(rootIdx);
				data.setElement(j, i, rootLabel);
				
				
			}
		}
		
		data.print(System.out);
		splitRegions(regions, data, binaryImage);
		
		return regions;
	}
		
	private static int getW(MatrixI data,  int x, int y){
		if(x-1 > -1 && x-1 < data.getSizeX()) return data.getElement(x-1, y);
		return 0;
	}
	
	private static int getN(MatrixI data,  int x, int y){
		if(y-1 > -1 && y-1 < data.getSizeY()) return data.getElement(x, y-1);
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
