package imagemanager.model.boardedition;

import imageprocessing.matrix.MatrixI;

import java.util.Collection;
import java.util.Map;

public class BoardEditionModel {
	
	private Map<Integer, TextComponent> textComponents;
	private MatrixI textCompMatrix;
	
	public BoardEditionModel(MatrixI textCompMatrix){
		this.textCompMatrix = textCompMatrix;
	}
	
	public void markComponent(int id, boolean marked){
		textComponents.get(id).setMarked(marked);		
	}
	
	
}
