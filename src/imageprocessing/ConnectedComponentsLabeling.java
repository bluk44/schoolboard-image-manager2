package imageprocessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ConnectedComponentsLabeling {

	public static Mat run(Mat image) {
		
		image = Util.getBinaryImage(image);
		
		int sizeX = image.width();
		int sizeY = image.height();

		int[] samples = new int[(int) (image.total())];
		image.get(0, 0, samples);

		int[] labeled = new int[samples.length];
		int[] neighbours = new int[4];

		int nPtr = 0;
		int currentLabel = 1;
		DisjointSets labelSet = new DisjointSets(1);

		// first run
		currentLabel = 1;
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				int currentPixel = samples[idx(sizeX, x, y)];
				if (currentPixel == 0)
					continue;
				nPtr = 0;

				if (x > 0 && labeled[idx(sizeX, x - 1, y)] != 0)
					neighbours[nPtr++] = labeled[idx(sizeX, x - 1, y)]; // W
				if (y > 0 && labeled[idx(sizeX, x, y - 1)] != 0)
					neighbours[nPtr++] = labeled[idx(sizeX, x, y - 1)]; // N
				if (x > 0 && y > 0 && labeled[idx(sizeX, x - 1, y - 1)] != 0)
					neighbours[nPtr++] = labeled[idx(sizeX, x - 1, y - 1)]; // NW
				if (x < sizeX - 1 && y > 0
						&& labeled[idx(sizeX, x + 1, y - 1)] != 0)
					neighbours[nPtr++] = labeled[idx(sizeX, x + 1, y - 1)]; // NE

				if (nPtr == 0) {
					labeled[idx(sizeX, x, y)] = currentLabel++;
					labelSet.addElement();
				} else {
					int lPrev = neighbours[0];
					int lNow;
					int lMin = neighbours[0];
					for (int i = 0; i < nPtr; i++) {
						lNow = neighbours[i];
						if (lNow < lMin) {
							lMin = lNow;
						}
						labelSet.union(lNow, lPrev);
					}
					labeled[idx(sizeX, x, y)] = lMin;
				}
			}
		}

		// second run

		int[] regionsSize = new int[labelSet.elements.size()];

		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				// if(labeled[idx(sizeX, x,y)] == 0) continue;
				int root = labelSet.find(labeled[idx(sizeX, x, y)]);
				labeled[idx(sizeX, x, y)] = root;

				++regionsSize[root];
			}
		}
		
		Mat result = new Mat(image.size(), CvType.CV_32SC1);
		
		result.put(0, 0, labeled);
		
		return result;
		
//		Map<Integer, Integer> idIdxMap = new HashMap<Integer, Integer>();
//
//		int idx = 0;
//		for (int i = 0; i < regionsSize.length; i++) {
//			if (regionsSize[i] > 0) {
//				idIdxMap.put(i, idx++);
//			}
//		}
//
//		for (int y = 0; y < sizeY; y++) {
//			for (int x = 0; x < sizeX; x++) {
//				System.out.print(labeled[idx(sizeX, x, y)]);
//			}
//			System.out.println();
//		}
//
//		ConnectedComponent[] components = new ConnectedComponent[idIdxMap
//				.size()];
//
//		for (int y = 0; y < sizeY; y++) {
//			for (int x = 0; x < sizeX; x++) {
//				int compId = labeled[idx(sizeX, x, y)];
//				int compIdx = idIdxMap.get(compId);
//				if (components[compIdx] == null) {
//					components[compIdx] = new ConnectedComponent();
//				}
//				
//				ConnectedComponent cc = components[compIdx];
//				cc.indexList.add(idx(sizeX, x, y));
//				if(x < cc.minX) cc.minX = x;
//				if(x > cc.maxX) cc.maxX = x;
//				if(y < cc.minY) cc.minY = y;
//				if(y > cc.maxY) cc.maxY = y;
//			}
//		}
//
//		return components;
		
		
	}

	protected static int idx(int sizeX, int x, int y) {
		return y * sizeX + x;
	}

	protected static void setElement(int sizeX, int[] samples, int x, int y,
			int val) {
		samples[idx(sizeX, x, y)] = val;
	}
	
	protected static class DisjointSets {
		protected List<Integer> elements;

		public DisjointSets() {
			elements = new ArrayList<Integer>();
		}

		public DisjointSets(int initCap) {
			elements = new ArrayList<Integer>(initCap);
			for (int i = 0; i < initCap; i++) {
				elements.add(-1);
			}
		}

		public void addElement() {
			elements.add(-1);
		}

		/**
		 * finds root of tree
		 * 
		 * @param el
		 *            element
		 * @return
		 */
		public int find(int el) {
			if (elements.get(el) < 0) {
				return el;
			} else {
				elements.set(el, find(elements.get(el)));
				return elements.get(el);
			}
		}

		public void union(int el1, int el2) {
			int root1 = find(el1);
			int root2 = find(el2);

			if (root1 == root2)
				return;

			if (elements.get(root2) < elements.get(root1)) {
				elements.set(root2, elements.get(root2) + elements.get(root1));
				elements.set(root1, root2);
			} else if (elements.get(root1) == elements.get(root2)) {
				if (root1 < root2) {
					elements.set(root1,
							elements.get(root1) + elements.get(root2));
					elements.set(root2, root1);
				} else {
					elements.set(root2,
							elements.get(root2) + elements.get(root1));
					elements.set(root1, root2);
				}
			} else {

				elements.set(root1, elements.get(root1) + elements.get(root2));
				elements.set(root2, root1);
			}
		}

	}

//	public static class ConnectedComponent {
//		public int id;
//		public List<Integer> indexList = new ArrayList<Integer>();
//		public Integer minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE,
//				maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
//		
//		public Polygon getBounds(){
//			int[] xpoints = {minX,maxX,maxX,minX};
//			int[] ypoints = {minY, minY, maxY, maxY};
//			
//			Polygon bounds = new Polygon(xpoints, ypoints, 4);
//			
//			return bounds;
//		}
//	}
}
