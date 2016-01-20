package imageprocessing.algorithm;

public class DisjointSetsForest<T> extends DisjointSets<T> {

	@Override
	public void union(int el1Idx, int el2Idx) {
		if(el1Idx == el2Idx) return;
		int root1Idx = findRootIndex(el1Idx);
		int root2Idx = findRootIndex(el2Idx);
		
		if(root1Idx >= 0 && root2Idx >= 0 && root1Idx == root2Idx) return;
		
		Element<T> el1 = elements.get(root1Idx), el2 = elements.get(root2Idx);
		
		// wiecej elementow w zbiorze 1
		if(el1.rootIdx < el2.rootIdx){
			el1.rootIdx += el2.rootIdx;
			el2.rootIdx = root1Idx;
			
		}
		// ilosc elementow rowna zostaje drzewo o mniejszym indexie
		else if(el1.rootIdx == el2.rootIdx){ 
			if(root1Idx < root1Idx){
				el1.rootIdx += el2.rootIdx;
				el2.rootIdx = root1Idx;
				
			} else {
				el2.rootIdx += el1.rootIdx;
				el1.rootIdx = root2Idx;	
			}
		}
		// wiecej elementow w zbiorze 2
		else{ 
			
			el2.rootIdx += el1.rootIdx;
			el1.rootIdx = root2Idx;		
		}
	}

	@Override
	public int findRootIndex(int elementIdx) {
		Element<T> el = elements.get(elementIdx);
		if(el.isRoot()){
			return elementIdx;
		} else {
			el.rootIdx = findRootIndex(el.rootIdx);
			return el.rootIdx;
		}
	}

}
