package imagemanager.gui.image;

import imagemanager.controller.CategoryController;
import imagemanager.controller.ImageController;
import imagemanager.model.SourceImage;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Scrollable;

public class ThumbnailPanel extends JPanel implements Scrollable {
	
	
	private static Integer thumbSize = 175;
	
	private boolean selectionKeyPressed;

	private JMenu removeLabelOption = null;
	private  JPopupMenu imagePopupMenu = null;

	private CategoryController categoryController;
	private ImageController imageController;
	
	public ThumbnailPanel() {
		setLayout(myLayout);
		addMouseListener(mouseListener);
	}


	public void setDisplayableImages(Collection<ThumbnailComponent> images) {
		removeAll();
		for (ThumbnailComponent thumbnailComponent2 : images) {
			add(thumbnailComponent2);
		}
		refresh();
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

	private void resetSelection() {
		Component[] comps = getComponents();
		for (Component component : comps) {
			((ThumbnailComponent) component).setSelected(false);
		}
	}
	
	private ActionListener removeCategoryActionListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			categoryController.unassignCategoryFromImages(((JMenuItem)e.getSource()).getText());
		}
		
	};
	
	private MouseListener mouseListener = new MouseAdapter() {
				
		@Override
		public void mouseClicked(MouseEvent e) {
//			boolean s = ((ThumbnailComponent) e.getSource()).isSelected();
//			resetSelection();
//			((ThumbnailComponent) e.getSource()).setSelected(s);

			//System.out.println("mouse clicked "+e.getSource());
//			if (e.getButton() == MouseEvent.BUTTON3) {
//				
//				Collection<String> catNames = categoryController.getCategoryNamesFromSelectedImages();
//				removeLabelOption = new JMenu("usun etykietę");
//				for (String name : catNames) {
//					JMenuItem item = new JMenuItem(name);
//					item.addActionListener(removeCategoryActionListener);
//					removeLabelOption.add(item);
//				}
//				imagePopupMenu = new JPopupMenu();
//				imagePopupMenu.add(removeLabelOption);
//				imagePopupMenu.show((JComponent)e.getSource(), e.getX(), e.getY());
//			} else if (!selectionKeyPressed
//					&& e.getSource().getClass()
//							.equals(ThumbnailComponent.class)) {
//				boolean s = ((ThumbnailComponent) e.getSource()).isSelected();
//				resetSelection();
//				((ThumbnailComponent) e.getSource()).setSelected(s);
//			}

			if (e.getButton() == MouseEvent.BUTTON1){
				
				// load sourceImage
				ThumbnailComponent tc = (ThumbnailComponent) e.getSource();
				imageController.setupImageView(tc.getImageName());
			}

		}
		
	};
	
//	private void addDisplayableImages(Collection<ImageIconButton> images) {
//		for (ImageIconButton imageObj : images) {
//			Component comp = new ThumbnailComponent2();
//			add(comp);
//		}
//	}

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