package imagemanager.gui.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JToggleButton;

public class ImageIconButton extends JToggleButton {

	int iconAreaSize;
	int margin;
	
	BufferedImage icon;
	AffineTransform at = new AffineTransform();
	AffineTransform conc = new AffineTransform();
	
	public ImageIconButton(BufferedImage icon, int iconAreaSize, int margin) {
		this.icon = icon;
		this.iconAreaSize = iconAreaSize;
		this.margin = margin;
		setPreferredSize(new Dimension(iconAreaSize+2*margin, iconAreaSize+2*margin));
		setTransform();

	}

	private void setTransform(){
		AffineTransform scale = new AffineTransform();
		AffineTransform translation = new AffineTransform();
		
		double imgW = icon.getWidth(), imgH = icon.getHeight();

		double s1 = iconAreaSize / imgW;
		double s2 = iconAreaSize / imgH;

		if (imgH * s1 <= iconAreaSize) {
			scale.setToScale(s1, s1);
		} else {
			scale.setToScale(s2, s2);
		}
		
		double imgWs = icon.getWidth() * scale.getScaleX();
		double imgHs = icon.getHeight() * scale.getScaleY();
		
		double tx = (iconAreaSize - imgWs) / 2+margin, ty = (iconAreaSize - imgHs) / 2+margin;
		
		translation.translate(tx, ty);
		
		at.concatenate(translation);
		at.concatenate(scale);
		
	}
	@Override
	protected void paintBorder(Graphics g) {}
	
	@Override
	protected void paintComponent(Graphics g) {
		System.out.println("paint called");
		
		System.out.println(isSelected());
		// TODO Auto-generated method stub
		//super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.clearRect(0, 0, iconAreaSize+2*margin, iconAreaSize+2*margin);
		if(isSelected()){
			drawBackground(g2d);
		}
		AffineTransform saveAt = g2d.getTransform();
		conc.setToIdentity();
		conc.concatenate(saveAt);
		conc.concatenate(at);
		g2d.setTransform(conc);
		g2d.drawImage(icon, 0, 0, null);
		g2d.setTransform(saveAt);
		
	}
	
	private void drawBackground(Graphics2D g){
		g.setColor(Color.RED);
		g.fillRoundRect(0, 0, iconAreaSize+2*margin, iconAreaSize+2*margin, 10, 10);
	}
	
}
