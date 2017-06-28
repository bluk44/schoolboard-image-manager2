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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;

import org.scijava.input.KeyCode;

public class ThumbnailPanel extends JPanel implements Scrollable {

	private static Integer thumbSize = 175;
	private static int thumbIconAreaSize = 150, thumbMargin = 5;
	
	private boolean selectionActive;
		
	private CategoryController categoryController;
	private ImageController imageController;
	
	private ThumbMouseListener thumbMouseListener = new ThumbMouseListener();
	
	private Set<String> selection = new HashSet<String>();
	
	public ThumbnailPanel() {
		setLayout(myLayout);
		setupListeners();
		System.out.println("Thump Panel" + hasFocus());
	}

	public void setDisplayableImages(Collection<SourceImage> images) {
		removeAll();
		for (SourceImage image : images) {
			ThumbnailComponent comp = new ThumbnailComponent(image.getName(), Util.mat2Img(image.getIcon().getMat()), thumbIconAreaSize, thumbMargin);
			comp.iconButton.addMouseListener(thumbMouseListener);
			add(comp);
			}
		refresh();
	}

	public Set<String> getSelection(){
		return selection;
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
	
	private void setupListeners(){
		this.addKeyListener(new MyKeyListener());
		
	}

	private ActionListener removeCategoryActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			categoryController.unassignCategoryFromImages(((JMenuItem) e
					.getSource()).getText());
		}

	};
	
	
	class ThumbMouseListener extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			requestFocus();
			
			ImageIconButton btn = (ImageIconButton)e.getSource();
			ThumbnailComponent tc = (ThumbnailComponent)btn.getParent();
			if(tc.isSelected()){
				System.out.println(""+tc.getImageName()+" selected");
				selection.add(tc.getImageName());
			} else {
				selection.remove(tc.getImageName());
				System.out.println(""+tc.getImageName()+"not selected");
			}
		}
	}
	
	class MyKeyListener extends KeyAdapter {
		
		@Override
		public void keyReleased(KeyEvent e) {
			 if(e.getKeyCode() == KeyEvent.VK_ENTER){
			 if(selection.size() == 1){
				 for (String string : selection) {
					imageController.openSourceImage(string);
				}	
			 }
			 
			 }		
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
