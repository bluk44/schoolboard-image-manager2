package imagemanager.model.boardedition;

import java.awt.Rectangle;

public class TextComponent {
	
	private Integer id;
	
	private boolean marked;
	private Rectangle bounds;
	
	// TODO dopisac bounds do konstruktora
	public TextComponent(int id){
		this.id = id;
		this.marked = false;
	}
	
	public int getId(){
		return id;
	}
	
	public void setMarked(boolean marked){
		this.marked = marked;
	}
	
}
