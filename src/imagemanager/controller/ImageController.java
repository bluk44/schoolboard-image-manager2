package imagemanager.controller;

import imagemanager.gui.imagelookup.ImageLookupPanel;
import imagemanager.gui.imagelookup.SourceImageViewComponent;
import imagemanager.gui.imagelookup.SourceImageViewComponent.Mode;
import imagemanager.model.BoardRegion;
import imagemanager.model.SourceImage;
import imagemanager.persistence.ImageRepository;

import java.awt.Point;
import java.io.File;
import java.util.Collection;
import java.util.Set;

import org.eclipse.persistence.exceptions.DatabaseException;

public class ImageController {

	private ImageRepository imageRepo;
	protected ImageLookupPanel imageLookup;

	public void createSourceImage(File f) {
		System.out.println("create source image called");
		SourceImage sourceImage = new SourceImage(f);
		try{
			imageRepo.saveImage(sourceImage);
		} catch(Exception e){
			System.out.println("database extepion writing image "+f.getName());		
		}
	}
	
	public void openBoardRegion(Long id){
		SourceImage source = imageLookup.getSourceImage();
		Set<BoardRegion> regions = source.getBoardImages();
		for (BoardRegion region : regions) {
			if(region.getId().equals(id)){
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
	
	public void deleteBoardRegion(Long id){
		SourceImage source = imageLookup.getSourceImage();
		Set<BoardRegion> regions = source.getBoardImages();
		for (BoardRegion region : regions) {
			if(region.getId().equals(id)){
				imageRepo.deleteBoardRegion(region);
				source.getBoardImages().remove(region);
				
				updateSourceImageView(source);
				break;
			}
		}
	
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
