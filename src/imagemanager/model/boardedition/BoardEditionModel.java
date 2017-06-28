package imagemanager.model.boardedition;

import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;

public class BoardEditionModel {
	
	private Map<Integer, TextComponent> textComponents;
	Mat bianrizedBoardImage;
	
	public BoardEditionModel(Mat ltc){
		bianrizedBoardImage = ltc;
		initTextComps();
	}
	
	private void initTextComps(){
		textComponents = new HashMap<Integer, TextComponent>();
		for(int i = 0; i < bianrizedBoardImage.height(); i++){
			for(int j = 0; j < bianrizedBoardImage.width(); j++){
				int[] data = new int[1];
				bianrizedBoardImage.get(i, j, data);
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
		bianrizedBoardImage.get(y, x, data);
		if(data[0] == 0) return;
		textComponents.get(data[0]).setMarked(isMarked);		
	}
		
}
