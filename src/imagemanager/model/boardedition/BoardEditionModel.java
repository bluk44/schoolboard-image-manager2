package imagemanager.model.boardedition;

import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;

public class BoardEditionModel {
	
	private Map<Integer, TextComponent> textComponents;
	Mat labeledTextComponents;
	
	public BoardEditionModel(Mat lct){
		labeledTextComponents = lct;
		initTextComps();
	}
	
	private void initTextComps(){
		textComponents = new HashMap<Integer, TextComponent>();
		for(int i = 0; i < labeledTextComponents.height(); i++){
			for(int j = 0; j < labeledTextComponents.width(); j++){
				int[] data = new int[1];
				labeledTextComponents.get(i, j, data);
				if(data[0] != 0){
					TextComponent txtComp = new TextComponent(data[0]);
					textComponents.put(txtComp.getId(), txtComp);
				}
			}
		}
	}
	
	// zaznaza komponent o podanych wspolrzednych
	// kliknieicie myszka
	public void markTextComp(int x , int y, boolean isMarked){
		int[] data = new int[1];
		labeledTextComponents.get(y, x, data);
		if(data[0] == 0) return;
		textComponents.get(data[0]).setMarked(isMarked);		
	}
		
}
