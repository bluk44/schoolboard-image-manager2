package imagemanager.gui.image;

import imagemanager.controller.CategoryController;
import imagemanager.controller.ImageController;
import imagemanager.model.SourceImage;
import imageprocessing.Util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;

public class ThumbnailPanel extends JPanel implements Scrollable {

	private static Integer thumbSize = 175;
	private static int thumbIconAreaSize = 150, thumbMargin = 5;
	
	private boolean selectionActive;

	private JMenuItem addImagesToCategory = null;
	private JPopupMenu imagePopupMenu = null;

	private CategoryController categoryController;
	private ImageController imageController;

	private Set<ThumbnailComponent> selection = new HashSet<ThumbnailComponent>();

	public ThumbnailPanel() {
		setLayout(myLayout);
		// addMouseListener(mouseListener);
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("ctrl pressed CONTROL"), "ctrl pressed");
		getActionMap().put("ctrl pressed", new ActivateSelection());
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released CONTROL"), "ctrl released");
		getActionMap().put("ctrl released", new DisableSelection());
		
		setupPopupMenu();
		
		addKeyListener(new MyKeyListener());
	}

	public void setDisplayableImages(Collection<SourceImage> images) {
		removeAll();
		MyMouseListener listener = new MyMouseListener();
		for (SourceImage image : images) {
			ThumbnailComponent comp = new ThumbnailComponent(image.getName(), Util.getBufferedImage(image.getIcon()), thumbIconAreaSize, thumbMargin);
			add(comp);
			comp.iconButton.addMouseListener(new MyMouseListener());
			//comp.iconButton.addKeyListener(new MyKeyListener());
		}
		refresh();
	}

	public Collection<String> getSelectedImagesNames() {
		 Collection<String> names = new ArrayList<String>(selection.size());
		 for (ThumbnailComponent thumb : selection) {
		 names.add(thumb.getImageName());
		 }
		 return names;
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public void refresh() {
		revalidate();
		doLayout();
		repaint();
	}

	private LayoutManager myLayout = new LayoutManager() {

		@Override
		public void removeLayoutComponent(Component comp) {
			// TODO Auto-generated method stub
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public void layoutContainer(Container parent) {
			fitComponents();
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
			// TODO Auto-generated method stub

		}
	};

	private void fitComponents() {
		if (getComponentCount() < 1)
			return;
		double panelWidth = getWidth(), panelHeight = getHeight();

		Dimension minThumbCompSize = new Dimension(thumbSize, thumbSize);
		if (panelWidth < minThumbCompSize.getWidth())
			panelWidth = minThumbCompSize.getWidth();

		// obliczyc ile przyciskow zmiesci sie w rzedzie

		int nCompsInRow = (int) (panelWidth / minThumbCompSize.getWidth());
		nCompsInRow = (nCompsInRow > getComponentCount()) ? getComponentCount()
				: nCompsInRow;

		// ile zostanie miejsca w szerz
		int leftPixels = (int) (panelWidth - nCompsInRow
				* minThumbCompSize.getWidth());
		int addPixels = leftPixels / nCompsInRow;

		// obliczyc ile komponentow w kolumnie i wysokosc panelu
		int nCompsInCol = getComponentCount() / nCompsInRow;
		nCompsInCol += ((getComponentCount() % nCompsInRow) > 0) ? 1 : 0;

		panelHeight = nCompsInCol * minThumbCompSize.getHeight();

		// zapisuje nowy rozmiar panelu
		setPreferredSize(new Dimension((int) panelWidth, (int) panelHeight));
		// if(e.getButton() == MouseEvent.BUTTON3){
		// System.out.println("button3 pressed");
		// }
		// if (!selectionKeyPressed) {
		// boolean s = ((ThumbnailComponent) e.getSource()).isSelected();
		// resetSelection();
		// ((ThumbnailComponent) e.getSource()).setSelected(s);
		// }

		int minCompWidth = (int) minThumbCompSize.getWidth();
		int minCompHeight = (int) minThumbCompSize.getHeight();

		// wyznaczyc polozenie komponentow

		int i = 0, j = 0;
		Component[] allComps = getComponents();
		for (Component component : allComps) {
			component.setBounds(j * (minCompWidth + addPixels), i
					* minCompHeight, minCompWidth + addPixels, minCompHeight);
			++j;
			if (j == nCompsInRow) {
				j = 0;
				++i;
			}
		}
	}
	
	private void setupPopupMenu(){
		imagePopupMenu = new JPopupMenu();
		addImagesToCategory = new JMenuItem("Dodaj do kaegorii");
		addImagesToCategory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				System.out.println(paramActionEvent.getSource().getClass());
				JOptionPane.showInputDialog(getRootPane(), "Nazwa kategorii");
			}
		});
		imagePopupMenu.add(addImagesToCategory);
	}
	
	private void resetSelection() {
		Component[] comps = getComponents();
		for (Component component : comps) {
			((ThumbnailComponent) component).setSelected(false);
		}
	}

	private ActionListener removeCategoryActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			categoryController.unassignCategoryFromImages(((JMenuItem) e
					.getSource()).getText());
		}

	};
	
	private void removeDisplayableImages(Collection<SourceImage> images) {
		int i = 0;
		while (i < getComponentCount()) {
			for (SourceImage image : images) {
				if (((ThumbnailComponent) getComponent(i)).getName().equals(
						image)) {
					remove(i);
				} else {
					i++;
				}
			}
		}
		refresh();
	}
	
	class MyMouseListener extends MouseAdapter{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			System.out.println("mouse clicked");
			
			if(e.getButton() == MouseEvent.BUTTON3){
				imagePopupMenu.show(e.getComponent(), e.getX(), e.getY());
			} else {
				
				ImageIconButton btn = (ImageIconButton)e.getSource();
				ThumbnailComponent tc = (ThumbnailComponent)btn.getParent();
				if(!selectionActive){
					for (ThumbnailComponent thumbnailComponent : selection) {
						thumbnailComponent.setSelected(false);
					}
					selection = new HashSet<ThumbnailComponent>();
					
					btn.setSelected(true);
					selection.add((ThumbnailComponent)btn.getParent());

				} else {
					if(!btn.isSelected()){
						btn.setSelected(false);
						selection.remove(tc);
					} else {
						btn.setSelected(true);
						selection.add(tc);
					}
				}
									
				requestFocusInWindow();
				super.mouseClicked(e);						
			}
		}
		
	}
	
	class MyKeyListener extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if(selection.size() == 1){
					imageController.setupImageView(((ThumbnailComponent)(selection.toArray()[0])).getImageName());
				}
			}
			
		}
	}
	
	class ActivateSelection extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			selectionActive = true;
		}

	}

	class DisableSelection extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			selectionActive = false;
		}

	}

	public CategoryController getCategoryController() {
		return categoryController;
	}

	public void setCategoryController(CategoryController categoryController) {
		this.categoryController = categoryController;
	}

	public ImageController getImageController() {
		return imageController;
	}

	public void setImageController(ImageController imageController) {
		this.imageController = imageController;
	}
}
