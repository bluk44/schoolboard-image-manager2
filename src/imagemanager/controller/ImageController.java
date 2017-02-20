package imagemanager.controller;

import imagemanager.model.BoardRegion;
import imagemanager.model.BoardRegionSettings;
import imagemanager.model.SourceImage;
import imagemanager.persistence.ImageRepository;
import imagemanager.view.SourceImageViewComponent;

import java.awt.Point;
import java.io.File;
import java.util.Collection;
import java.util.Set;

public class ImageController {

	// model
	private SourceImage sourceImage;
	private ImageRepository imageRepo;
	private BoardRegionSettings boardRegionSettings;

	// view
	SourceImageViewComponent sourceImageView;

	public void createSourceImage(File f) {
		SourceImage sourceImage = new SourceImage(f);
		try {
			imageRepo.saveImage(sourceImage);
		} catch (Exception e) {
			System.out
					.println("database extepion writing image " + f.getName());
		}
	}

	public void createBoardRegion(Point[] quadrangle) {
		sourceImage.createBoardRegion(quadrangle,
				boardRegionSettings.getBoardType(),
				boardRegionSettings.getBoardRegionParams());

		sourceImage = imageRepo.saveImage(sourceImage);
		updateSourceImageView(sourceImage);
	}

	public void deleteBoardRegion(Long id) {

		Set<BoardRegion> regions = sourceImage.getBoardImages();
		for (BoardRegion region : regions) {
			if (region.getId().equals(id)) {
				regions.remove(region);
				imageRepo.deleteBoardRegion(region);
				break;
			}
		}
		updateSourceImageView(sourceImage);


	}

	public void openSourceImage(String imageName) {

		sourceImage = imageRepo.findImage(imageName);
		updateSourceImageView(sourceImage);

	}

	public ImageRepository getImageRepo() {
		return imageRepo;
	}

	public void setImageRepo(ImageRepository imageRepo) {
		this.imageRepo = imageRepo;
	}

	public BoardRegionSettings getBoardRegionSettings() {
		return boardRegionSettings;
	}

	public void setBoardRegionSettings(BoardRegionSettings boardRegionSettings) {
		this.boardRegionSettings = boardRegionSettings;
	}

	private void updateSourceImageView(SourceImage image) {

		sourceImageView.setSourceImage(sourceImage);

		Collection<Long> brKeys = sourceImageView.getRegionKeys();
		for (Long id : brKeys) {
			sourceImageView.removeBoardregionQuadrangle(id);
		}

		Set<BoardRegion> regions = sourceImage.getBoardImages();

		for (BoardRegion boardRegion : regions) {
			sourceImageView.putBoardRegionQuadranle(boardRegion.getId(),
					boardRegion.getPerimeter());
		}
		sourceImageView.resetView();
		sourceImageView.fitImageToComponent();
		sourceImageView.repaint();

	}

	public SourceImageViewComponent getSourceImageView() {
		return sourceImageView;
	}

	public void setSourceImageView(SourceImageViewComponent sourceImageView) {
		this.sourceImageView = sourceImageView;
	}
}
