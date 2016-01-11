package imagemanager.gui.imagelookup;

import imagemanager.controller.ImageController;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class SourceImageViewComponent extends ImageViewComponent {

	public static enum Mode {
		 DISPLAY, MANUAL_BOARD_SELECTION
	}
	
	protected Mode mode = Mode.DISPLAY;

	private JPopupMenu popupMenu;
	
	private JMenu openRegionSubmenu, deleteRegionSubmenu;
	private JMenuItem addRegionManuallyMenuItem,
			addRegionAutomaticallyMenuItem;
	
	public ImageController imageController;
	
	public SourceImageViewComponent() {
	}

	@Override
	public void initialize() {
		super.initialize();
		setupListeners();
		setupPopupMenu();
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		switch (mode) {
		case DISPLAY:
			drawables.remove("boardSelection");
			break;
		case MANUAL_BOARD_SELECTION:
			drawables.put("boardSelection", new QuadrangleSelecting());
		}
	}
	
	public ImageController getImageController() {
		return imageController;
	}

	public void setImageController(ImageController imageController) {
		this.imageController =  imageController;
	}
		
	protected Collection<String> getRegionKeys(){
		Collection<String> regionKeys = new LinkedList<String>();
		Collection<String> allkeys = drawables.keySet();
		for (String key : allkeys) {
			if(key.startsWith("BR")){
				regionKeys.add(key);
			}
		}
		
		return regionKeys;
	}
	
	@Override
	protected void drawObjects(Graphics2D g2d) {
		drawImage(g2d);
		if (mode == Mode.DISPLAY) {
			drawShapes(g2d);
		} else if (mode == Mode.MANUAL_BOARD_SELECTION) {
			drawSelection(g2d);
		}
	}

	protected void drawSelection(Graphics2D g2d) {
		QuadrangleSelecting mqs = (QuadrangleSelecting) drawables
				.get("boardSelection");
		if (mqs != null) {
			mqs.draw(g2d);
		}
	}

	private Point getPointOnImage(Point point) {
		try {
			AffineTransform at = new AffineTransform();
			at.concatenate(scale);
			at.concatenate(translation);
			AffineTransform inv = at.createInverse();

			point.x = (int) (point.x + inv.getTranslateX());
			point.y = (int) (point.y + inv.getTranslateY());

			point.x = (int) (point.x * inv.getScaleX());
			point.y = (int) (point.y * inv.getScaleY());

		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return point;
	}

	public Point[] getSelectedQuadrangle() throws QuadrangleIncompleteException{
			QuadrangleSelecting quad = (QuadrangleSelecting) drawables.get("boardSelection");
			return quad.getSelectedQuadrangle();
	}
	
	private void setupListeners(){
		
		MyKeyAdapter keyboard = new MyKeyAdapter();
		this.addKeyListener(keyboard);
		MyMouseAdapter mouse = new MyMouseAdapter();
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
		MyComponentAdapter compResized = new MyComponentAdapter();
		this.addComponentListener(compResized);
	}
	
	private void setupPopupMenu() {
		popupMenu = new JPopupMenu();
		openRegionSubmenu = new JMenu("pokaz region tablicy");
		addRegionManuallyMenuItem = new JMenuItem("dodaj region tablicy recznie");
		addRegionAutomaticallyMenuItem = new JMenuItem("dodaj region tablicy automatycznie");
		deleteRegionSubmenu = new JMenu("usun region tablicy");
		popupMenu.add(openRegionSubmenu);
		popupMenu.addSeparator();
		popupMenu.add(addRegionManuallyMenuItem);
		popupMenu.add(addRegionAutomaticallyMenuItem);
		popupMenu.addSeparator();
		popupMenu.add(deleteRegionSubmenu);
	
	}
	
	class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(noImage) return;
			
			if(e.getKeyCode() == KeyEvent.VK_M){
				System.out.println("manual board selection activated");
				setMode(Mode.MANUAL_BOARD_SELECTION);
				repaint();
			} else if(e.getKeyCode() == KeyEvent.VK_A){
				System.out.println("automatic board selection activated");
			} else  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("enter pressed");
				if (mode == Mode.MANUAL_BOARD_SELECTION) {
					try {
	
						Point[] quadrangle = getSelectedQuadrangle();
						imageController.createBoardRegion(quadrangle);
						drawables.remove("boardSelection");
						repaint();
						
					} catch (QuadrangleIncompleteException e1) {
						System.out.println(e1.getMessage());
					}
				}
			}
		}
	}

	class MyMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("mouse clicked");
			if(noImage) return;
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (mode == Mode.MANUAL_BOARD_SELECTION) {
					
					// dodaj wierzcholek regionu tablicy
					Point onImage = getPointOnImage(new Point(e.getX(),
							e.getY()));
					if (onImage.x >= 0 && onImage.y >= 0 && onImage.x < imgW
							&& onImage.y < imgH) {
						QuadrangleSelecting mqs = (QuadrangleSelecting) drawables
								.get("boardSelection");
						mqs.addPoint(onImage);
						repaint();
					}
				}

			} else if (e.getButton() == MouseEvent.BUTTON3) {
				if (mode == Mode.DISPLAY) {

					// wywolaj menu kontekstowe
					Collection<String> regionKeys = getRegionKeys();
					openRegionSubmenu.removeAll();
					for (String string : regionKeys) {
						openRegionSubmenu.add(new JMenuItem(string));
					}
					popupMenu.show(e.getComponent(), e.getX(), e.getY());

				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(noImage) return;
			if (mode == Mode.MANUAL_BOARD_SELECTION) {
				Point onImage = getPointOnImage(new Point(e.getX(), e.getY()));
				if (onImage.x >= 0 && onImage.y >= 0 && onImage.x < imgW
						&& onImage.y < imgH) {
					QuadrangleSelecting mqs = (QuadrangleSelecting) drawables
							.get("boardSelection");
					mqs.dragPoint(onImage);
					repaint();
				}
			}
		}
	}

}
