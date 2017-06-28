package imagemanager.controller;

import imagemanager.gui.category.CategoryViewObject;
import imagemanager.gui.category.CategoryViewPanel;
import imagemanager.gui.image.ThumbnailPanel;
import imagemanager.model.Category;
import imagemanager.model.SourceImage;
import imagemanager.persistence.CategoryRepository;
import imagemanager.persistence.ImageRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO obsługa wyjatkow
public class CategoryController {
	
	protected CategoryRepository categoryRepo;
	protected ImageRepository imageRepo;
	
	public CategoryViewPanel categoryView;
	public ThumbnailPanel thumbnailPanel;
	
	public CategoryController(){
		System.out.println("CategoryController constructor called");
	}
	
	public void initializeViews(){
		System.out.println("categoryController initViews called");
		categoryView.initView(categoryRepo.loadRootCategories());
	}
	
	public void createCategory(String title){
		Category category = new Category(title);
		category = categoryRepo.saveCategory(category);				
		categoryView.createCategory(category);
		System.out.println(category);
	}
	
	public void createSubCategory(String title, String parent){
		Category childCat = new Category(title);
		childCat = categoryRepo.saveCategory(childCat);
		Category parentCat = categoryRepo.findCategory(parent);
		parentCat.addSubCategory(childCat);
		categoryRepo.saveCategory(parentCat);
				
		categoryView.createCategory(parent, childCat);
	}
	
	public void renameCategory(String newTitle, String oldTitle){
		Category category = categoryRepo.findCategory(oldTitle);
		category.setTitle(newTitle);
		categoryRepo.saveCategory(category);
		categoryView.modifyCategory(oldTitle, category);
	}	
	
	public void deleteCategory(String title){
		Category category = categoryRepo.findCategory(title);
		categoryRepo.deleteCategory(category);
		categoryView.removeCategory(title);
	}
	
	public void assignImagesToCategories(){
//		Collection<CategoryViewObject> catObjs = categoryView.getSelectedCategories();
//		Collection<String> imageNames = thumbnailPanel.getSelectedImagesNames();
//		List<Category> categories = new ArrayList<Category>();
//		for (CategoryViewObject catObj : catObjs) {
//			categories.add(categoryRepo.findCategory(catObj.getTitle()));
//		}
//		List<SourceImage> images = new ArrayList<SourceImage>();
//		for (String name : imageNames) {
//			images.add(imageRepo.findImage(name));
//		}
//		
//		for (Category category : categories) {
//			for (SourceImage sourceImage : images) {
//				category.addImageToCategory(sourceImage);
//			}
//			categoryRepo.saveCategory(category);
//		}
	}
	
	public void unassignCategoryFromImages(String categoryTitle){
//		Collection<String> imageNames = thumbnailPanel.getSelectedImagesNames();
//		categoryRepo.unassignCategoryFromImages(categoryTitle, imageNames.toArray(new String[]{}));
	}
	
	public Collection<String> getCategoryNamesFromSelectedImages(){
//		Collection<String> imageNames = thumbnailPanel.getSelectedImagesNames();
//		Collection<Category> categories = categoryRepo.loadCategoriesFromImages(imageNames.toArray(new String[]{}));
//		Collection<String> catTitles = new ArrayList<String>();
//		for (Category category : categories) {
//			catTitles.add(category.getTitle());
//		}
//		return catTitles;
		return null;
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
	
	public CategoryViewPanel getCategoryView() {
		return categoryView;
	}

	public void setCategoryView(CategoryViewPanel categoryView) {
		this.categoryView = categoryView;
	}

	public ThumbnailPanel getThumbnailPanel() {
		return thumbnailPanel;
	}

	public void setThumbnailPanel(ThumbnailPanel thumbnailPanel) {
		this.thumbnailPanel = thumbnailPanel;
	}
}
