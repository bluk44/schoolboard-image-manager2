package imagemanager.view;

import imagemanager.controller.BoardRegionController;
import imagemanager.gui.imagelookup.ImageViewComponent;
import imagemanager.model.BoardRegion;
import imagemanager.model.TextRegion;
import imagemanager.model.boardedition.BoardEditionModel;
import imageprocessing.Util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class BoardRegionViewComponent extends ImageViewComponent {
	
	private BoardRegionController boardRegionController;
	
	public static enum DisplayMode {
		NORMAL, TEXT_REGION_SELECTION
	}
	
	private  DisplayMode mode = DisplayMode.NORMAL;
	
	private JPopupMenu popupMenu;
	private JMenuItem MImarkTextRegion;
	
	private RectangleMarker rectMarker = new RectangleMarker();
	
	private BoardEditionModel boardEditionModel;
	
	public void initialize(){
		super.initialize();
		setupListeners();
		setupPopupMenu();
	}
	
	public void setBoardRegion(BoardRegion region){
		clear();
		setImage(Util.mat2Img(region.getResultImage()));
		Collection<TextRegion> tr = region.getTextRegions();
		
		for (TextRegion textRegion : tr) {
			putShape("TR"+textRegion.getId(), textRegion.getPerimeter());
		}
		
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
	
	private class ALmarkTextRegion implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setDisplayMode(DisplayMode.TEXT_REGION_SELECTION);
			disableKeyboard();
		}		
	}
	
	private class MyMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			//if (noImage)
			//	return;
//			System.out.println("mouse clicked");

			if(e.getButton() == MouseEvent.BUTTON3){
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			} else if(mode == DisplayMode.TEXT_REGION_SELECTION && e.getButton() == MouseEvent.BUTTON1){
				rectMarker.update(e.getX(), e.getY());
				repaint();
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			if(e.getButton() == MouseEvent.BUTTON1){
				rectMarker.start(e.getX(), e.getY());
				repaint();
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			rectMarker.update(e.getX(), e.getY());
			repaint();
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			rectMarker.reset();
			repaint();
		}
		
	}
	
	
}
