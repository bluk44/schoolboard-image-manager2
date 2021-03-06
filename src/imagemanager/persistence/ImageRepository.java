package imagemanager.persistence;

import imagemanager.model.BoardRegion;
import imagemanager.model.SourceImage;

import java.util.Collection;

public interface ImageRepository {
	
	SourceImage loadImage(Long id);
	SourceImage findImage(String name);
	Collection<SourceImage> loadAllImages();
	SourceImage saveImage(SourceImage image);
	Collection<SourceImage> loadImagesFromCategories(String... categoryTitles);
	void deleteImage(SourceImage image);
	BoardRegion loadBoardRegion(Long id);
	void deleteBoardRegion(BoardRegion region);
	BoardRegion updateBoardRegion(BoardRegion region);
	
}
