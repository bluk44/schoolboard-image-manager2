package imagemanager.gui;

import imagemanager.gui.category.CategoryViewPanel;
import imagemanager.gui.category.SelectedCategoriesPanel;
import imagemanager.gui.image.ThumbnailPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageCollectionViewPanel extends JPanel {
	
	public void initView(){
		setLayout(new GridBagLayout());
//		JButton comp1 = new JButton();
//		JButton comp2 = new JButton();
		JPanel comp1 = new JPanel();
		JPanel comp2 = new JPanel();
		JPanel comp3 = new JPanel();
		
		//CategoryViewPanel catPanel = new CategoryViewPanel();
		//JScrollPane sp = new JScrollPane();
		//sp.setViewportView(catPanel);
		comp1.setLayout(new GridLayout());
		comp1.add(sp);
		sp.setViewportView(categoriesPanel);
		comp1.setBackground(Color.yellow);
		comp2.setBackground(Color.black);
		comp3.setBackground(Color.gray);
		comp1.setPreferredSize(new Dimension(200, 150));
		comp1.setBorder(BorderFactory.createTitledBorder("tits"));
		comp3.setPreferredSize(new Dimension(200, 250));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		c.weighty = 1.0;
		add(comp1, c);
		c.gridx = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridheight = 2;
		add(thumbnailPanel, c);
		c.weightx = 0.0;
		c.gridy = 1;
		c.gridx = 0;
		c.gridheight = 1;
//		add(comp3, c);
		selectedCategoriesPanel.setPreferredSize(new Dimension(200, 250));
		add(selectedCategoriesPanel, c);
		setPreferredSize(new Dimension(800, 600));
		setBackground(Color.blue);
	}
	
	public ImageCollectionViewPanel(){
		System.out.println("ImageCollectionViewPanel constructor called");
//      Container container = new Container();
//      container.setLayout(new GridBagLayout());
//		GridBagConstraints c = new GridBagConstraints();
//		container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//		c.fill = GridBagConstraints.BOTH;		
//		c.weightx = 0.5;
//		c.weighty = 0;
//		container.add(imageQueryPanel, c);
//		
//		c.weightx = 0.5;
//		c.weighty = 1.0;
//		c.gridy = 1;
//		c.ipady = 0;
//		container.add(thumbnailPanel, c);

	}
	
	public JScrollPane sp = new JScrollPane();
	public CategoryViewPanel categoriesPanel;
	public SelectedCategoriesPanel selectedCategoriesPanel;
	public ThumbnailPanel thumbnailPanel;
	
	public CategoryViewPanel getCategoriesPanel() {
		return categoriesPanel;
	}
	public void setCategoriesPanel(CategoryViewPanel categoriesPanel) {
		this.categoriesPanel = categoriesPanel;
	}
	public SelectedCategoriesPanel getSelectedCategoriesPanel() {
		return selectedCategoriesPanel;
	}
	public void setSelectedCategoriesPanel(
			SelectedCategoriesPanel selectedCategoriesPanel) {
		this.selectedCategoriesPanel = selectedCategoriesPanel;
		
	}
	public ThumbnailPanel getThumbnailPanel() {
		return thumbnailPanel;
	}
	public void setThumbnailPanel(ThumbnailPanel thumbnailPanel) {
		this.thumbnailPanel = thumbnailPanel;
	}

	
}
