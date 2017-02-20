package imagemanager.gui.imagelookup;

import imagemanager.model.SourceImage;

/**
 * Główna klasa widoku
 * 
 * @author lucas
 *	
 */
public class ImageLookup {
	
	private ImageLookupPanel imageLookupPanel;
	private BoardRegionEditionPanel BoardRegioneditionPanel;
	
	public void initialize(){
		
	}

	public ImageLookupPanel getImageLookupPanel() {
		return imageLookupPanel;
	}

	public void setImageLookupPanel(ImageLookupPanel imageLookupPanel) {
		this.imageLookupPanel = imageLookupPanel;
	}

	public BoardRegionEditionPanel getBoardRegioneditionPanel() {
		return BoardRegioneditionPanel;
	}

	public void setBoardRegioneditionPanel(
			BoardRegionEditionPanel boardRegioneditionPanel) {
		BoardRegioneditionPanel = boardRegioneditionPanel;
	}
}
  