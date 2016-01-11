package imagemanager.gui.image;

import imageprocessing.Util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JToggleButton;

public class ThumbnailComponent extends JToggleButton {
	
	private SourceImageViewObject image;
	private BufferedImage scaledIcon;
	
	private static int DEF_THUMB_SIZE = 150;

	// ustalane przez rodzica
	private Dimension thumbImageArea;

	// położenie obszaru miniaturki (zależne od rzeczywisttch wymiarow,
	// imageArea i marginesu)
	private int imageAreaX = 0, imageAreaY = 0;

	// gorny margines
	private final int margin = 10;

	// private BufferedImage originalImage;
	//private SourceImage image;
	
	private int imageX, imageY;

	public ThumbnailComponent(SourceImageViewObject image) {
		this(image, DEF_THUMB_SIZE);
	}

	public ThumbnailComponent(SourceImageViewObject image, int thumbSize) {
		this.image = image;
		setThumbImageArea(thumbSize);
		addMouseListener(mouseListener);
		addKeyListener(keyListener);
	}

	public void setThumbImageArea(int imageAreaSize) {
		thumbImageArea = new Dimension(imageAreaSize, imageAreaSize);
		setMinimumSize(new Dimension(thumbImageArea.getSize().width + 2
				* margin, thumbImageArea.getSize().height + 2 * margin));
		resizeThumbImage();
	}
	
	//TODO zmienic resizera
	private void resizeThumbImage() {
		BufferedImage icon = image.getIcon();
		int a = (icon.getWidth() > icon.getHeight()) ? icon.getWidth() : icon.getHeight();
		double ratio = thumbImageArea.getWidth() / a;
		scaledIcon = Util.resize(icon, ratio);

	}

	@Override
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.BLUE);
		g.drawRect(imageAreaX, imageAreaY, thumbImageArea.width,
				thumbImageArea.height);

		g.drawImage(scaledIcon, imageX, imageY, null);
		if (isSelected()){
			g.setColor(Color.RED);
			g.drawRect(imageX, imageY, scaledIcon.getWidth(),
					scaledIcon.getHeight());
		}
		
		g.setColor(Color.BLACK);
		g.drawString(image.getName(), imageX + (thumbImageArea.width  - image.getName().length() * 6)/2, thumbImageArea.height - 5);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		imageAreaX = (width - thumbImageArea.width) / 2;
		imageAreaY = (height - thumbImageArea.height) / 2;
		// imageX = imageAreaX;
		// imageY = imageAreaY;
		imageX = (int) (imageAreaX + ((thumbImageArea.getWidth() - scaledIcon
				.getWidth()) / 2.d));
		imageY = (int) (imageAreaY + ((thumbImageArea.getHeight() - scaledIcon
				.getHeight()) / 2.d));

	}

	public SourceImageViewObject getImageObject(){
		return image;
	}
	
	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			e.getComponent().getParent().dispatchEvent(e);
		}

	};
	
	private KeyListener keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			e.getComponent().getParent().dispatchEvent(e);			
		};
		
		@Override
		public void keyReleased(KeyEvent e) {
			e.getComponent().getParent().dispatchEvent(e);						
		};
	};
}
