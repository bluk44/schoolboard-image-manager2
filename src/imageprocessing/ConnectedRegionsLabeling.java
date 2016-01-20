package imageprocessing;

import ij.process.ImageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectedRegionsLabeling {
		
	
	public static Results run(ImageProcessor ip, int BG){
		int sizeX = ip.getWidth();
		int sizeY = ip.getHeight();
		
		int[] samples = getPixelValues(ip);
		int[] labeled = new int[samples.length];
		int[] neighbours = new int[4];

		int nPtr = 0;
		int currentLabel = 1;
		DisjointSets labelSet = new DisjointSets(1);
				
		// first run
		currentLabel = 1;
		for(int y=0;y<sizeY;y++){		
			for (int x = 0; x < sizeX; x++) {
				int currentPixel = samples[idx(sizeX, x, y)];
				if(currentPixel == BG) continue;
				nPtr = 0;

				if(x > 0 && labeled[idx(sizeX, x-1, y)] != BG) neighbours[nPtr++] = labeled[idx(sizeX, x-1, y)]; // W 
				if(y > 0 && labeled[idx(sizeX, x, y-1)] != BG) neighbours[nPtr++] = labeled[idx(sizeX, x, y-1)]; // N
				if(x > 0 && y > 0 && labeled[idx(sizeX, x-1, y-1)] != BG) neighbours[nPtr++] = labeled[idx(sizeX, x-1, y-1)]; // NW
				if(x < sizeX-1 && y > 0 && labeled[idx(sizeX, x+1, y-1)] != BG) neighbours[nPtr++] = labeled[idx(sizeX, x+1, y-1)]; // NE
				
				if(nPtr == 0){
					labeled[idx(sizeX, x,y)] = currentLabel++;
					labelSet.addElement();
				} else {
					int lPrev = neighbours[0];
					int lNow;
					int lMin = neighbours[0];
					for(int i=0; i<nPtr; i++){
						lNow = neighbours[i];
						if(lNow < lMin){
							lMin = lNow;
						}
						labelSet.union(lNow, lPrev);
					}
					labeled[idx(sizeX, x,y)] = lMin;
				}
			}
		}

		// second run

		int[] regionsSize = new int[labelSet.elements.size()];
		
		for(int y=0;y<sizeY;y++){
			for (int x = 0; x < sizeX; x++) {
				//if(labeled[idx(sizeX, x,y)] == 0) continue;
				int root = labelSet.find(labeled[idx(sizeX, x,y)]);
				labeled[idx(sizeX, x,y)] = root;
				
				++regionsSize[root];
			}
		}
		
		
		
		return new Results(ip, labeled, regionsSize);
	}
		
	protected static int idx(int sizeX, int x, int y){
		return y*sizeX + x;
	}
	
	protected static void setElement(int sizeX, int[] samples, int x, int y, int val){
		samples[idx(sizeX, x, y)] = val;
	}
	
	protected static int[] getPixelValues(ImageProcessor ip){
		int sizeX = ip.getWidth();
		int sizeY = ip.getHeight();
		
		int[] pixelVals = new int[sizeX * sizeY];
		for(int i = 0; i < sizeY; i++){
			for(int j = 0; j < sizeX; j++){
				pixelVals[i*sizeX+j] = (int)ip.getPixelValue(j, i);
			}
		}
		
		return pixelVals;
	}
	
	protected static class DisjointSets {
		protected List<Integer> elements;
		
		public DisjointSets(){
			elements = new ArrayList<Integer>();
		}
		
		public DisjointSets(int initCap){
			elements = new ArrayList<Integer>(initCap);
			for (int i = 0; i < initCap; i++) {
				elements.add(-1);		}
		}
		
		public void addElement(){
			elements.add(-1);
		}
		/**
		 * finds root of tree
		 * @param el element 
		 * @return
		 */
		public int find(int el){
			if(elements.get(el) < 0){
				return el;
			} else {
				elements.set(el, find(elements.get(el)));
				return elements.get(el);
			}
		}
		
		public void union(int el1, int el2){
			int root1 = find(el1);
			int root2 = find(el2);
			
			if(root1 == root2) return;
			
			if(elements.get(root2) < elements.get(root1)){
				elements.set(root2, elements.get(root2) + elements.get(root1));
				elements.set(root1, root2);
			} else if(elements.get(root1) == elements.get(root2)){ 
				if(root1 < root2){
					elements.set(root1, elements.get(root1) + elements.get(root2));
					elements.set(root2, root1);		
				} else {
					elements.set(root2, elements.get(root2) + elements.get(root1));
					elements.set(root1, root2);
				}
			} else{
				
				elements.set(root1, elements.get(root1) + elements.get(root2));
				elements.set(root2, root1);			
			}
		}
		
	}
	
	public static class Results {
		private int nRegions;
		private ImageProcessor image;
		private int[] labeledImage;
		
		private List<Region> regionsList;
		//private Map<Integer, Region> regionsMap;
		
		public Results(ImageProcessor image, int[] labeledImage, int[] regionsSize){
			this.image = image;
			this.labeledImage = labeledImage;
			
			Map<Integer, Integer> regId_regIdx = new HashMap<Integer, Integer>();
			int regionsCount = 0;
			
			// inicjalizacja mapy ID regionu -> IDX na liscie wszystkich regionow	
			for(int i = 0; i < regionsSize.length; i++){
				if(regionsSize[i] == 0) continue;
				regId_regIdx.put(i, regionsCount++);
			}
			
			// inicjalizacja listy regionow
			regionsList = new ArrayList<Region>(regionsCount -1);
			for(int i = 0; i < regionsSize.length; i++){
				if(regionsSize[i] == 0) continue;
				regionsList.add(new Region(image, i, regionsSize[i]));
			}
			
			// dodanie mapowania posRegion -> posImage dla kazdego piksela
			int[] pixelIdxRegion = new int[regionsSize.length];
			
			int imgW = image.getWidth(), imgH = image.getHeight();
			for(int i = 0; i < imgH; i++){
				for(int j = 0; j < imgW; j++){
					int regId = labeledImage[i*imgW + j];
					int posRegion = pixelIdxRegion[regId]++;
					regionsList.get(regId_regIdx.get(regId)).setPixelMapping(posRegion, i, j);
				}
			}
						
		}
		
		public Region getRegion(int idx){
			return regionsList.get(idx);
		}
		
		public List<Region> getAllRegions(){
			return regionsList;
		}
	}
	
	public static class Region{
		ImageProcessor image;
		Integer id;
		int size;
		int minX, maxX, minY, maxY;
		
		int[][] imageMapping; // [][0] -> y, [][1] -> x
		
		public Region(ImageProcessor image, int id, int size){
			this.image = image;
			this.id = id;
			this.size = size;
			
			imageMapping = new int[size][2];
			minX = image.getWidth();
			minY = image.getHeight();
		}
		
		public int[] getPixels(){
			int[] pixels = new int[size];
			
			for(int i = 0; i < imageMapping.length; i++){
				pixels[i] = (int) image.getPixelValue(imageMapping[i][1], imageMapping[i][0]);
			}

			return pixels;
		}
		
		public void setPixel(int idx, int val){
			image.putPixelValue(imageMapping[idx][1], imageMapping[idx][0], val);
		}
		
		public void setPixels(int[] pixels){
			if(pixels.length != imageMapping.length) return;
			
			for(int i = 0; i < pixels.length; i++){
				setPixel(i, pixels[i]);
			}
		}
		
		public void setPixels(int val){
			for(int i = 0; i < imageMapping.length; i++){
				setPixel(i, val);
			}
		}
		public void setPixelMapping(int posRegion, int y, int x){
			imageMapping[posRegion][0] = y;
			imageMapping[posRegion][1] = x;

			if(y < minY ) minY = y;
			if(y > maxY ) maxY = y;
			if(x < minX ) minX = x;
			if(x > maxX ) maxX = x;			
			
		}
		
		public int[] getPixelMapping(int posRegion){
			return imageMapping[posRegion];
		}
		
		public void setImage(ImageProcessor image){
			this.image = image;
		}
		
		public Integer getId(){
			return id;
		}
		
		public int getSize(){
			return size;
		}
		
		public void printRegion(){
			for(int i = minY; i <= maxY; i++){
				for(int j = minX; j <= maxX; j++){
					System.out.print(" "+(int)(image.getPixelValue(j, i)/ 128));
				}
				System.out.println();
			}
		}
		
		@Override
		public String toString() {
			return "region: "+id+" size: "+size;
		}
		
	}
}
