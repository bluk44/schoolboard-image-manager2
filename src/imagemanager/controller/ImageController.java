package imagemanager.controller;

import imagemanager.gui.imagelookup.ImageLookupPanel;
import imagemanager.gui.imagelookup.SourceImageViewComponent;
import imagemanager.gui.imagelookup.SourceImageViewComponent.Mode;
import imagemanager.model.BoardRegion;
import imagemanager.model.SourceImage;
import imagemanager.persistence.ImageRepository;

import java.awt.Point;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ImageController {

	private ImageRepository imageRepo;
	protected ImageLookupPanel imageLookup;

	public void createSourceImage() {
		
	}
	
	public void createBoardRegion(Point[] quadrangle){
		System.out.println("updating model...");
		SourceImage source = imageLookup.getSourceImage();
		source.extractBoardRegion(quadrangle);
		
		System.out.println("updating view...");
		updateSourceImageView();
	}
	
	public void setupImageView(String imageName) {
		SourceImage image = imageRepo.findImage(imageName);
		imageLookup.setSourceImage(image);
	}

	public ImageRepository getImageRepo() {
		return imageRepo;
	}

	public void setImageRepo(ImageRepository imageRepo) {
		this.imageRepo = imageRepo;
	}

	public ImageLookupPanel getImageLookup() {
		return imageLookup;
	}

	public void setImageLookup(ImageLookupPanel imageLookup) {
		this.imageLookup = imageLookup;
	}
	
	private void updateSourceImageView(){
		SourceImageViewComponent view = imageLookup.getSourceImageViewComponent();
		
		Collection<Long> brKeys = view.getRegionKeys();
		for (Long id : brKeys) {
			view.removeBoardregionQuadrangle(id);
		}
		
		Set<BoardRegion> regions = imageLookup.getSourceImage().getBoardImages();
		
		for (BoardRegion boardRegion : regions) {
			view.putBoardRegionQuadranle(boardRegion.getId(), boardRegion.getPerimeter());
		}
		view.setMode(Mode.DISPLAY);
		view.repaint();
	}
}
