package imageprocessing.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DisjointSets<T> {
	
	List<Element<T>> elements = new ArrayList<Element<T>>(); 
	
	public int makeSet(T data){
		elements.add(new Element<T>(data));
		
		return elements.size() - 1;
	}
	
	public List<Element<T>> getAllElements(){
		return elements;
	}
	
	public T getElement(int idx){
		return elements.get(idx).getData();
	}
	
	public List<List<T>> getSets(){
		List<List<T>> sets = new ArrayList<List<T>>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		int idx = 0;
		for (Element<T> element : elements) {
			
			// znajdz roota
			int rootIdx = findRootIndex(idx);
			
			// jesli nie ma zbioru na liscie zbiorow to go dodaj
			// dodaj tez roota do nowego zbioru
			if(map.get(rootIdx) == null){
				int newSetIdx = sets.size();
				map.put(rootIdx, newSetIdx);
				Element<T> rootEl = elements.get(rootIdx);

				sets.add(new ArrayList<T>(-rootEl.rootIdx));				
				sets.get(newSetIdx).add(rootEl.getData());
			}
			
			// jesli element nie jest rootem do dodaj go do zbioru
			if(! element.isRoot()){
				int setIdx = map.get(rootIdx);
				sets.get(setIdx).add(element.getData());
			} 
			++idx;
		}
		
		return sets;
	}
		
	public abstract void union(int el1Idx, int el2Idx);
	public abstract int findRootIndex(int elementIdx);
	
}

class Element<T> {
	public int rootIdx;
	T data;
	
	public boolean isRoot(){
		return rootIdx < 0 ? true : false;
	}
	
	public Element(T data){
		rootIdx = -1;
		this.data = data;
	}
	
	public T getData(){
		return data;
	}
	
}