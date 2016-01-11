package imagemanager.persistence;

import imagemanager.model.Category;
import java.util.Collection;

public interface CategoryRepository {
	
	Category loadCategory(Long id);
	Collection<Category> loadRootCategories();
	Collection<Category> loadCategoriesFromImages(String... imageNames);
	Category findCategory(String title);
	Category saveCategory(Category category);
	Category addSubCategory(Category subCategory, String parent);
	void assignImagesToCategory(String categoryTitle, String... imageNames);
	void unassignCategoryFromImages(String categoryTitle, String... imageNames);
	void deleteCategory(Category category);
}