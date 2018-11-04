package imagemanager.view;

import imagemanager.controller.BoardRegionController;
import imagemanager.model.BoardRegion;
import imagemanager.model.TextRegion;
import imageprocessing.FloodFill;
import imageprocessing.Util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import test.Test;

public class BoardRegionViewComponent extends ImageViewComponent {
	
	private BoardRegionController boardRegionController;
	
	public static enum DisplayMode {
		NORMAL, TEXT_REGION_SELECTION
	}
	
	private  DisplayMode mode = DisplayMode.NORMAL;
	
	private JPopupMenu popupMenu;
	private JMenuItem MImarkTextRegion;
	
	private RectangleMarker rectMarker = new RectangleMarker();
		
	public void initialize(){
		super.initialize();
		setupListeners();
		setupPopupMenu();

	}
	
	public void setBoardRegion(BoardRegion region){
		clear();
		System.out.println(region);
		
		setImage(Util.mat2Img(region.getClearedImage()));
//		Collection<TextRegion> tr = region.getTextRegions();
//		
//		for (TextRegion textRegion : tr) {
//			putShape("TR"+textRegion.getId(), textRegion.getPerimeter());
//		}
		
	}

	public BoardRegionController getBoardRegionController() {
		return boardRegionController;
	}

	public void setBoardRegionController(BoardRegionController boardRegionontroller) {
		this.boardRegionController = boardRegionontroller;
	}
	
	private void setDisplayMode(DisplayMode mode){
		this.mode = mode;
		
		repaint();
	}
	
	private void setupPopupMenu(){
		popupMenu = new JPopupMenu();
		
		MImarkTextRegion = new JMenuItem("Zaznacz region textu");
		MImarkTextRegion.addActionListener(new ALmarkTextRegion());
		popupMenu.add(MImarkTextRegion);
	}
	
	private void setupListeners(){
		MyMouseAdapter mouse = new MyMouseAdapter();
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		System.out.println("paint component called");
		super.paintComponent(g);
		
		rectMarker.draw(g);
	}
	
	@Override
	protected void drawObjects(Graphics2D g2d) {
		drawImage(g2d);
		if (mode == DisplayMode.NORMAL) {
			drawShapes(g2d);
		}
	}
	
	private void markTextComponent(int x, int y){
		
		AffineTransform trans = getTranslation();
		AffineTransform scale = getScale();
		try {
			AffineTransform invScale = scale.createInverse();
			AffineTransform invTrans = trans.createInverse();
			Point2D ptSrc = new Point2D.Double(x, y);
			Point2D ptDst = new Point2D.Double();
			invTrans.transform(ptSrc, ptSrc);

			invScale.transform(ptSrc, ptSrc);
			System.out.println(" scaled "+ptSrc);
			//inv.transform(ptDst, ptDst);
			//System.out.println("transformed "+ptDst);
			
			BufferedImage image = getImage();
			Color rep = Color.RED;
			Color target = Color.WHITE;
			if(ptSrc.getX() <= 0 || ptSrc.getY() <= 0) return;
			FloodFill.run(image, (int)ptSrc.getX(), (int)ptSrc.getY(), target, rep);
			
			Test.showImage(image, "filled");
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		System.out.println("Translation "+getTranslation().getTranslateX() + " "+getTranslation().getTranslateY());
//		double trX = getTranslation().getTranslateX();
//		double trY = getTranslation().getTranslateY();
//		double px = x;
//		double py = y;
//		double dx = px - trX;
//		double dy = py - trY;
//		System.out.println("dx "+dx + "dy "+dy);
//		BufferedImage image = getImage();
//		Color rep = Color.RED;
//		Color target = Color.WHITE;
//		if(dx <= 0 || dy <= 0) return;
//		FloodFill.run(image, (int)dx, (int)dy, target, rep);
//		
//		Test.showImage(image, "filled");
	}
	
	
	private class ALmarkTextRegion implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setDisplayMode(DisplayMode.TEXT_REGION_SELECTION);
			//disableKeyboard();
		}		
	}
	
	private class MyMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			if(e.getButton() == MouseEvent.BUTTON3){
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
				
				
			}
			if(e.getButton() == MouseEvent.BUTTON1 && mode == DisplayMode.TEXT_REGION_SELECTION){
				System.out.println("x "+e.getX()+"y "+e.getY());
				
				markTextComponent(e.getX(), e.getY());
			}
//			} else if(mode == DisplayMode.TEXT_REGION_SELECTION && e.getButton() == MouseEvent.BUTTON1){
//				rectMarker.update(e.getX(), e.getY());
//				repaint();
//			}
			
		}
		
//		@Override
//		public void mousePressed(MouseEvent e) {
//			// TODO Auto-generated method stub
//			if(e.getButton() == MouseEvent.BUTTON1){
//				rectMarker.start(e.getX(), e.getY());
//				repaint();
//			}
//		}
//		
//		@Override
//		public void mouseDragged(MouseEvent e) {
//			rectMarker.update(e.getX(), e.getY());
//			repaint();
//		}
//		
//		@Override
//		public void mouseReleased(MouseEvent e) {
//			rectMarker.reset();
//			repaint();
//		}
		
	}
	
	
}
