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
	
	public void openBoardRegion(Long id){
		System.out.println("opening region "+id);
		SourceImage source = imageLookup.getSourceImage();
		Set<BoardRegion> regions = source.getBoardImages();
		System.out.println("number of regions "+regions.size());
		for (BoardRegion region : regions) {
			System.out.println(region.getId());
			if(region.getId().equals(id)){
				System.out.println("region found");
				imageLookup.getBoardRegionsPane().openBoardRegion(region);
				break;
			}
		}
	}
	
	public void createBoardRegion(Point[] quadrangle){
		SourceImage source = imageLookup.getSourceImage();
		source.extractBoardRegion(quadrangle);
		
		source = imageRepo.saveImage(source);
		
		updateSourceImageView(source);
	}

	
	public void setupImageView(String imageName) {
		SourceImage source = imageRepo.findImage(imageName);
		
		updateSourceImageView(source);
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
	
	private void updateSourceImageView(SourceImage source){
		imageLookup.setSourceImage(source);

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
		view.fitImageToComponent();
		view.repaint();
	}
}
