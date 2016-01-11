package imagemanager.persistence.jpa;

import imagemanager.model.SourceImage;
import imagemanager.persistence.ImageRepository;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
@Repository
public class ImageRepositoryJPA implements ImageRepository{
	
	@PersistenceContext
	protected EntityManager em;
	
	@Override
	public SourceImage loadImage(Long id) {
		return em.find(SourceImage.class, id);
	}

	@Override
	public SourceImage findImage(String name) {
		Query query = em
				.createQuery("select i from SourceImage i where i.name=:param1");
		query.setParameter("param1", name);
		return (SourceImage) query.getSingleResult();
	}
	
	@Override
	public Collection<SourceImage> loadAllImages() {
		Query query = em.createQuery("SELECT s FROM SourceImage s");
		return query.getResultList();
	}

	@Override
	@Transactional(readOnly=false)
	public SourceImage saveImage(SourceImage image) {
		SourceImage merged = em.merge(image);
		em.flush();
		return merged;
	}

	@Override
	public void deleteImage(SourceImage image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<SourceImage> loadImagesFromCategories(
			String... categoryTitles) {
		Collection<String> titles = new ArrayList<String>();
		for (String string : categoryTitles) {
			titles.add(string);
		}
		
		Collection<SourceImage> images = em
				.createQuery(
						"select s from SourceImage s join s.categories c where c.title in :param1", SourceImage.class)
				.setParameter("param1", titles).getResultList();
		return images;
	}
	
}
