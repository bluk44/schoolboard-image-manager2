package imagemanager.view;

import imagemanager.gui.imagelookup.DrawableImage;
import imagemanager.gui.imagelookup.DrawableObject;
import imagemanager.gui.imagelookup.DrawableShape;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

public class ImageViewComponent extends JComponent {

	protected Map<String, DrawableObject> drawables = new HashMap<String, DrawableObject>();
	protected boolean imageInCenter = true;
	protected int imgW = 0, imgH = 0;
	
	protected AffineTransform translation = new AffineTransform();
	protected AffineTransform scale = new AffineTransform();
	protected AffineTransform global = new AffineTransform();
	
	protected boolean noImage = true;
	
	protected boolean keyboardEnabled = true;
	
	public ImageViewComponent() {
	}

	public void initialize() {
		DrawableImage noImage = new DrawableImage();
		drawables.put("image", noImage);
		
		setupListeners();
	}

	public void setImage(BufferedImage image) {
		DrawableImage img = new DrawableImage();
		img.setImage(image);
		drawables.put("image", img);
		noImage = false;
		imgW = image.getWidth();
		imgH = image.getHeight();
		
		setToInitialPosition();
	}
	
	public BufferedImage getImage(){
		DrawableImage image = (DrawableImage) drawables.get("image");
		return image.getImage();
	}
	
	public void clear(){
		DrawableImage noImage = new DrawableImage();
		drawables = new HashMap<String, DrawableObject>();
		drawables.put("image", noImage);
		this.noImage = true;
		setToInitialPosition();
	}
	
	public void putShape(String name, Shape shape) {

		drawables.put(name, new DrawableShape(shape));
	}

	// TODO
	/**
	 * Ustawia skale d aby zdjecie miescilo sie w komponencie
	 */
	
	public void fitImageToComponent() {
		DrawableImage img = (DrawableImage) drawables.get("image");
		double imgW = img.getWidth(), imgH = img.getHeight();
		double compW = getWidth(), compH = getHeight();
		
		double s1 = compW / imgW;
		double s2 = compH / imgH;
		
		if(imgH * s1 <= compH){
			scale.setToScale(s1, s1);
		} else {
			scale.setToScale(s2, s2);
		}
		setToInitialPosition();
	}

	public void moveLeft(int value){
		translation.translate(-value, 0);
	}
	
	public void moveRight(int value){
		translation.translate(value, 0);
	}
	
	public void moveUp(int value){
		translation.translate(0, -value);
	}
	
	public void moveDown(int value){
		translation.translate(0, value);
	}
	
	public void scale(double value){
		scale.scale(value, value);
	}
	
	public void disableKeyboard(){
		keyboardEnabled = false;
	}
	
	public void enableKeyboard(){
		keyboardEnabled = true;
	}
	
	public void setScale(double scaleX, double scaleY) {
		scale.setToScale(scaleX, scaleY);
		setToInitialPosition();
	}
	
	public AffineTransform getTranslation(){
		return translation;
	}
	
	public AffineTransform getScale(){
		return scale;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform saveAT = g2d.getTransform();
		global.setToIdentity();
		global.concatenate(saveAT);
		global.concatenate(translation);
		global.concatenate(scale);
		g2d.setTransform(global);
		drawImage(g2d);
		drawShapes(g2d);
		g2d.setTransform(saveAT);

		
	}
	
	protected void drawImage(Graphics2D g2d) {
		drawables.get("image").draw(g2d);
	}

	protected void transformShapes(AffineTransform at){
		Collection<DrawableObject> c = drawables.values();

		for (DrawableObject obj : c) {
			if (obj instanceof DrawableShape) {
				((DrawableShape) obj).transform(global);
			}
		}
	}
	
	protected void drawShapes(Graphics2D g2d) {
		Collection<DrawableObject> c = drawables.values();

		for (DrawableObject obj : c) {
			if (obj instanceof DrawableShape) {
				//((DrawableShape) obj).transform(concAT);
				obj.draw(g2d);
			}
		}
	}
	
	/**
	 * Ustawia przeuniecie dla wszystkich obiektow tak aby zdjecie lezalo w
	 * centrum komponentu
	 */
	private void setToInitialPosition() {

		DrawableImage image = (DrawableImage) drawables.get("image");
		double tx = (getWidth() - image.getWidth() * scale.getScaleX()) / 2;
		double ty = (getHeight() - image.getHeight() * scale.getScaleY()) / 2;

		translation.setToTranslation(tx, ty);
	}
	
	private void setupListeners(){
		
		MyComponentAdapter compResized = new MyComponentAdapter();
		this.addComponentListener(compResized);
		
		MyKeyAdapter keyAdapter = new MyKeyAdapter();
		this.addKeyListener(keyAdapter);
	}
	public class MyComponentAdapter extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);
			if (imageInCenter) {
				setToInitialPosition();
			}
			repaint();
		}
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			if(keyboardEnabled == false ) return;
			super.keyPressed(e);
			if(noImage) return;
			if(e.getKeyCode() == e.VK_LEFT){
				// przesun zdjecie w lewo
				//System.out.println("moving left ");
				moveLeft(100);
				repaint();
			} else if(e.getKeyCode() == e.VK_RIGHT){
				//System.out.println("moving right");
				moveRight(100);
				repaint();
			} else if(e.getKeyCode() == e.VK_UP){
				//System.out.println("moving up");
				moveUp(100);
				repaint();
			} else if(e.getKeyCode() == e.VK_DOWN){
				//System.out.println("moving down");
				moveDown(100);
				repaint();
			} else if(e.getKeyCode() == e.VK_F){
				//System.out.println(" fitting to image ");
				fitImageToComponent();
				setToInitialPosition();
				repaint();
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			if(keyboardEnabled == false) return;
			if(noImage) return;
			if(e.getKeyChar() == "+".charAt(0)){
				//System.out.println("magnifying ");
				scale(1.1);
				repaint();
			} else if(e.getKeyChar() == "-".charAt(0)){
				scale(0.9);
				repaint();
				//System.out.println("shrinking ");
			}
		}
	}

}
