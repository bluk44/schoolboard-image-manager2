package imagemanager.controller;

import imagemanager.gui.category.CategoryViewObject;
import imagemanager.gui.category.SelectedCategoriesPanel;
import imagemanager.gui.image.SourceImageViewObject;
import imagemanager.gui.image.ThumbnailPanel;
import imagemanager.model.SourceImage;
import imagemanager.persistence.CategoryRepository;
import imagemanager.persistence.ImageRepository;
import imageprocessing.Util;

import java.util.ArrayList;
import java.util.Collection;

public class ImageQueryController {
	
	public SelectedCategoriesPanel selectedCategoriesPanel;
	public ThumbnailPanel thumbnailPanel;

	private CategoryRepository categoryRepo;
	private ImageRepository imageRepo;
	
	public void  queryForImages(){
		
		Collection<SourceImage> images = null;
		
		if(selectedCategoriesPanel.isChooseAllSelected()){
			images = imageRepo.loadAllImages();			
		} else {
			Collection<CategoryViewObject> categoryViewObjects = selectedCategoriesPanel.getChosenCategories();
			String[] titles = new String[categoryViewObjects.size()];
			int i = 0;
			for (CategoryViewObject catObj : categoryViewObjects) {
				titles[i++] = catObj.getTitle();
			}
			images = imageRepo.loadImagesFromCategories(titles);
		}
		
		if(images != null){
			Collection<SourceImageViewObject> imageViewObjs = new ArrayList<SourceImageViewObject>();
			for (SourceImage image : images) {
				imageViewObjs.add(new SourceImageViewObject(Util.getBufferedImage(image.getIcon()), image.getName(), image.getDate()));
			}
			thumbnailPanel.setDisplayableImages(imageViewObjs);
		}
	}

	public CategoryRepository getCategoryRepo() {
		return categoryRepo;
	}

	public void setCategoryRepo(CategoryRepository categoryRepo) {
		this.categoryRepo = categoryRepo;
	}

	public ImageRepository getImageRepo() {
		return imageRepo;
	}

	public void setImageRepo(ImageRepository imageRepo) {
		this.imageRepo = imageRepo;
	}
	
	public ThumbnailPanel getThumbnailPanel() {
		return thumbnailPanel;
	}

	public void setThumbnailPanel(ThumbnailPanel thumbnailPanel) {
		this.thumbnailPanel = thumbnailPanel;
	}

	public SelectedCategoriesPanel getSelectedCategoriesPanel() {
		return selectedCategoriesPanel;
	}

	public void setSelectedCategoriesPanel(
			SelectedCategoriesPanel selectedCategoriesPanel) {
		this.selectedCategoriesPanel = selectedCategoriesPanel;
	}
}
