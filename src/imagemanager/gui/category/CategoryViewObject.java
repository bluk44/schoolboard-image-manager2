package imagemanager.gui.category;

public class CategoryViewObject implements Comparable<CategoryViewObject>{
	
	private Long id;
	private String title;
	private boolean checked = false; 
	
	public CategoryViewObject(Long id, String title){
		this.id = id;
		this.title = title;
	}
	
	public Long getId(){
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(final boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public int compareTo(CategoryViewObject o) {
		return title.compareTo(o.title);
	}

	@Override
	public String toString() {
		return title;
	}
	
	
}
