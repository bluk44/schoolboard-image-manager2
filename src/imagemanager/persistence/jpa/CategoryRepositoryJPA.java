package imagemanager.persistence.jpa;

import imagemanager.model.Category;
import imagemanager.model.SourceImage;
import imagemanager.persistence.CategoryRepository;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public class CategoryRepositoryJPA implements CategoryRepository {

	@PersistenceContext
	protected EntityManager em;
	
	public CategoryRepositoryJPA(){
		System.out.println("CategoryRepository constructor called");
	}
	
	@Override
	public Category loadCategory(Long id) {
		return em.find(Category.class, id);
	}

	@Override
	public Collection<Category> loadRootCategories() {
		return em.createQuery(
				"select c from Category c where c.parent is null",
				Category.class).getResultList();
	}

	@Override
	public Category findCategory(String title) {
		Query query = em
				.createQuery("select c from Category c where c.title=:param1");
		query.setParameter("param1", title);
		return (Category) query.getSingleResult();
	}

	@Override
	@Transactional(readOnly = false)
	public Category saveCategory(Category category) {
		Category merged = em.merge(category);
		em.flush();
		return merged;
	}

	@Override
	@Transactional(readOnly = false)
	public Category addSubCategory(Category subCategory, String parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCategory(Category category) {
		category = em.merge(category);
		em.remove(category);
	}

	@Override
	@Transactional(readOnly = false)
	public void assignImagesToCategory(String categoryTitle, String... imageNames) {
		Category category = (Category) em
				.createQuery("select c from Category c where c.title=:param1")
				.setParameter("param1", categoryTitle).getSingleResult();
		Collection<String> names = new ArrayList<String>();

		for (String iname : imageNames) {
			names.add(iname);
		}
		Collection<SourceImage> images = em
				.createQuery(
						"select s from SourceImage s where s.name in :inames",
						SourceImage.class).setParameter("inames", names)
				.getResultList();

		for (SourceImage sourceImage : images) {
			category.addImageToCategory(sourceImage);
		}

		em.flush();
	}
	
	@Override
	@Transactional(readOnly = false)
	public void unassignCategoryFromImages(String categoryTitle,
			String... imageNames) {
		Category category = (Category) em
				.createQuery("select c from Category c where c.title=:param1")
				.setParameter("param1", categoryTitle).getSingleResult();
		Collection<String> names = new ArrayList<String>();

		for (String iname : imageNames) {
			names.add(iname);
		}
		Collection<SourceImage> images = em
				.createQuery(
						"select s from SourceImage s where s.name in :inames",
						SourceImage.class).setParameter("inames", names)
				.getResultList();

		for (SourceImage sourceImage : images) {
			category.removeImageFromCategory(sourceImage);
		}

		em.flush();
		}
	
	@Override
	public Collection<Category> loadCategoriesFromImages(
			String... imageNames) {
		Collection<String> names = new ArrayList<String>();
		for (String string : imageNames) {
			names.add(string);
		}
		
		Collection<Category> categories = em
				.createQuery(
						"select distinct c from Category c join c.images i where i.name in :param1", Category.class)
				.setParameter("param1", names).getResultList();
		return categories;
	}
		
}
