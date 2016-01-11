package imagemanager.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Category implements Comparable<Category>{
	
	@Id
	@GeneratedValue
	private Long id;
	@Column(unique=true)
	private String title;
	
	private byte[] icon;
	
	@ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name="PARENT_ID")
	Category parent;
	
	@OneToMany(mappedBy="parent", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	Set<Category> subCategories = new HashSet<Category>();
	
	@ManyToMany(fetch=FetchType.LAZY)
	Set<SourceImage> images = new HashSet<SourceImage>();
	
	public Category(){};
	
	public Category(String title){
		this.title = title;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
		
	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}
	
	public Category getParent(){
		return parent;
	}
	
	public void setParent(Category parent){
		this.parent = parent;
	}
	
	public Set<Category> getSubCategories(){
		return subCategories;
	}
	
	public void setSubCategories(Set<Category> subCategories){
		this.subCategories = subCategories;
	}
	
	public boolean addSubCategory(Category category){
		if(subCategories.add(category)){
			category.setParent(this);
			return true;
		}
		return false;
	}
	
	public Set<SourceImage> getImages() {
		return images;
	}

	public void setImages(Set<SourceImage> images) {
		this.images = images;
	}

	public void addImageToCategory(SourceImage image){
		images.add(image);
	}
	
	public void removeImageFromCategory(SourceImage image){
		images.remove(image);
	}
	
	@Override
	public int compareTo(Category o) {
		return title.compareTo(o.title);
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", title=" + title + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
}
