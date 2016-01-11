package imagemanager.controller;

import imagemanager.gui.imagelookup.ImageLookupPanel;
import imagemanager.gui.imagelookup.SourceImageViewComponent;
import imagemanager.model.SourceImage;
import imagemanager.persistence.ImageRepository;

import java.awt.Polygon;

public class ImageController {

	private ImageRepository imageRepo;
	private ImageLookupPanel imageLookup;

	public void createSourceImage() {
		
	}
	
	public void createBoardRegion(Polygon quadrangle){
		System.out.println("updating model...");
		System.out.println("updating view...");
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
}
