package imagemanager.gui.image;

import imageprocessing.Util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

public class ThumbnailComponent extends JComponent{
	
	ImageIconButton iconButton;
	JLabel label;
	
	public ThumbnailComponent(String imageName, BufferedImage icon, int iconAreaSize, int margin){
		iconButton = new ImageIconButton(icon, iconAreaSize, margin);
		label = new JLabel(imageName);
		this.setLayout(new GridBagLayout());
		this.add(iconButton);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 1;
		this.add(label, c);
	}
	
	public void setSelected(boolean selected){
		iconButton.setSelected(selected);
	}
	
	public boolean isSelected(){
		return iconButton.isSelected();
	}
	
	public String getImageName(){
		return label.getText();
	}
}
