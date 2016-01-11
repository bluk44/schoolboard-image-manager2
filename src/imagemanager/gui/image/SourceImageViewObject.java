package imagemanager.gui.image;

import java.awt.image.BufferedImage;

public class SourceImageViewObject {
	
	protected BufferedImage icon;
	protected String name;
	protected Long date;
	
	public SourceImageViewObject(BufferedImage image, String name, Long date){
		this.name = name;
		this.icon = image;
		this.date = date;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public Long getDate() {
		return date;
	}

}
