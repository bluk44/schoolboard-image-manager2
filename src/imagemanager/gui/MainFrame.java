package imagemanager.gui;

import imagemanager.controller.CategoryController;
import imagemanager.controller.ImageController;
import imagemanager.gui.imagelookup.ImageLookupPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {

	MainFrame() {
	}

	public void initializeView() {

		tabbedPane = new JTabbedPane();

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				System.out.println("Tab: " + tabbedPane.getSelectedIndex());
				if (tabbedPane.getSelectedIndex() == 1) {
					imageLookup.getSourceImageViewComponent().requestFocus();
				}
			}
		});
		
		tabbedPane.addTab("Kolekcja zdjęć", imageCollectionView);
		tabbedPane.addTab("Podgląd zdjęcia", imageLookup);

		setupTopMenu();
		this.add(tabbedPane);
		this.setJMenuBar(topMenuBar);
		pack();
		setVisible(true);

		// imageCollectionView.initView();
	}

	public void setupTopMenu() {
		JMenu categoryMenu = new JMenu("Kategoria");
		JMenu imageMenu = new JMenu("Zdjęcie");

		JMenuItem createCategoryMenuItem = new JMenuItem(
				"Stwórz nową kategorię");
		JMenuItem deleteCategoryMenuItem = new JMenuItem("Usuń kategorię");

		createCategoryMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField parentTitle = new JTextField();
				JTextField categoryTitle = new JTextField();
				JComponent[] inputs = new JComponent[] {
						new JLabel("Kategoria nadrzędna"), parentTitle,
						new JLabel("Tytuł"), categoryTitle };

				int dialogOut = JOptionPane.showConfirmDialog(null, inputs,
						"Tworzenie kategorii", JOptionPane.PLAIN_MESSAGE);
				if (dialogOut == -1)
					return;

				String parentTitleText = parentTitle.getText();
				String categoryTitleText = categoryTitle.getText();

				if (parentTitleText.isEmpty() && !categoryTitleText.isEmpty()) {
					categoryController.createCategory(categoryTitleText);
				} else {
					if (!categoryTitleText.isEmpty()) {
						categoryController.createSubCategory(categoryTitleText,
								parentTitleText);
					}
				}
			}
		});
		deleteCategoryMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField categoryTitle = new JTextField();
				JComponent[] inputs = new JComponent[] { new JLabel("Tytuł"),
						categoryTitle };

				int dialogOut = JOptionPane.showConfirmDialog(null, inputs,
						"Usuwanie kategorii", JOptionPane.PLAIN_MESSAGE);
				if (dialogOut == -1)
					return;

				String categoryTitleText = categoryTitle.getText();

				if (!categoryTitleText.isEmpty()) {
					categoryController.deleteCategory(categoryTitleText);
				}
			}
		});
		categoryMenu.add(createCategoryMenuItem);
		categoryMenu.add(deleteCategoryMenuItem);

		topMenuBar = new JMenuBar();
		topMenuBar.add(imageMenu);
		topMenuBar.add(categoryMenu);

	}

	private ImageCollectionViewPanel imageCollectionView;
	private ImageLookupPanel imageLookup;
	private JTabbedPane tabbedPane;
	private JMenuBar topMenuBar;

	public ImageController imageController;
	public CategoryController categoryController;

	public ImageCollectionViewPanel getImageCollectionView() {
		return imageCollectionView;
	}

	public void setImageCollectionView(
			ImageCollectionViewPanel imageCollectionView) {
		this.imageCollectionView = imageCollectionView;
	}

	public ImageLookupPanel getImageLookup() {
		return imageLookup;
	}

	public void setImageLookup(ImageLookupPanel imageLookup) {
		this.imageLookup = imageLookup;
	}

	public ImageController getImageController() {
		return imageController;
	}

	public void setImageController(ImageController imageController) {
		this.imageController = imageController;
	}

	public CategoryController getCategoryController() {
		return categoryController;
	}

	public void setCategoryController(CategoryController categoryController) {
		this.categoryController = categoryController;
	}

}
