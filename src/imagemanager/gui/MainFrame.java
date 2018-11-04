package imagemanager.gui;

import imagemanager.controller.CategoryController;
import imagemanager.controller.ImageController;
import imagemanager.gui.imagelookup.BoardRegionViewPane;
import imagemanager.view.SourceImageViewComponent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {

	MainFrame() {
	}

	public void initializeView() {

		tabbedPane = new JTabbedPane();
		sourceImageView.requestFocus();
//		tabbedPane.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				if (tabbedPane.getSelectedIndex() == 1) {
//					sourceImageView.requestFocus();
//				} else if(tabbedPane.getSelectedIndex() == 2){
//					boardRegionViewPane.requestFocus();
//				}
//			}
//		});
		//tabbedPane.addTab("Kolekcja zdjęć", imageCollectionView);
		tabbedPane.addTab("Podgląd zdjęcia", sourceImageView);
		//tabbedPane.addTab("Edycja regionu tablicy", boardRegionViewPane);
		setupTopMenu();
		this.add(tabbedPane);
		this.setJMenuBar(topMenu);
		pack();
		setVisible(true);

		// imageCollectionView.initView();
	}

	public void setupTopMenu() {
//		JMenu categoryMenu = new JMenu("Kategoria");
//		JMenu imageMenu = new JMenu("Zdjęcie");
//		JMenu settingsMenu = new JMenu("Ustawienia");
//		JMenu setBoardCleanerMenu = new JMenu("Ustaw rodzaj tablicy");
//		
//		JMenuItem createCategoryMenuItem = new JMenuItem(
//				"Stwórz nową kategorię");
//		JMenuItem deleteCategoryMenuItem = new JMenuItem("Usuń kategorię");
//
//		JRadioButtonMenuItem rbi1 = new JRadioButtonMenuItem("rbi1");
//		JRadioButtonMenuItem rbi2 = new JRadioButtonMenuItem("rbi2");
//		
//		createCategoryMenuItem.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JTextField parentTitle = new JTextField();
//				JTextField categoryTitle = new JTextField();
//				JComponent[] inputs = new JComponent[] {
//						new JLabel("Kategoria nadrzędna"), parentTitle,
//						new JLabel("Tytuł"), categoryTitle };
//
//				int dialogOut = JOptionPane.showConfirmDialog(null, inputs,
//						"Tworzenie kategorii", JOptionPane.PLAIN_MESSAGE);
//				if (dialogOut == -1)
//					return;
//
//				String parentTitleText = parentTitle.getText();
//				String categoryTitleText = categoryTitle.getText();
//
//				if (parentTitleText.isEmpty() && !categoryTitleText.isEmpty()) {
//					categoryController.createCategory(categoryTitleText);
//				} else {
//					if (!categoryTitleText.isEmpty()) {
//						categoryController.createSubCategory(categoryTitleText,
//								parentTitleText);
//					}
//				}
//			}
//		});
//		deleteCategoryMenuItem.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JTextField categoryTitle = new JTextField();
//				JComponent[] inputs = new JComponent[] { new JLabel("Tytuł"),
//						categoryTitle };
//
//				int dialogOut = JOptionPane.showConfirmDialog(null, inputs,
//						"Usuwanie kategorii", JOptionPane.PLAIN_MESSAGE);
//				if (dialogOut == -1)
//					return;
//
//				String categoryTitleText = categoryTitle.getText();
//
//				if (!categoryTitleText.isEmpty()) {
//					categoryController.deleteCategory(categoryTitleText);
//				}
//			}
//		});
////		setBoardCleanerMenuItem.addActionListener(new ActionListener() {
////
////			@Override
////			public void actionPerformed(ActionEvent e) {
////				System.out.println("action performed");
////				JOptionPane.showOptionDialog(null, "Ciemna czy jasna tablica",
////						"Wybierz opcje", JOptionPane.DEFAULT_OPTION,
////						JOptionPane.INFORMATION_MESSAGE, null, new String[] {
////								"Jasna", "Ciemna" }, "Jasna");
////			}
////		});
//		
//		
//		
//		categoryMenu.add(createCategoryMenuItem);
//		categoryMenu.add(deleteCategoryMenuItem);
//
//		JMenuItem importImagesMenuItem = new JMenuItem("Zaimportuj zdjęcia");
//		importImagesMenuItem
//				.addActionListener(new ImportImagesActionListener());
//		imageMenu.add(importImagesMenuItem);
//		settingsMenu.add(setBoardCleanerMenu);
//
//		
//		
//		topMenuBar = new JMenuBar();
//		topMenuBar.add(imageMenu);
//		topMenuBar.add(categoryMenu);
//		topMenuBar.add(settingsMenu);
	}

	private ImageCollectionViewPanel imageCollectionView;

	private JTabbedPane tabbedPane;
	private MainTopMenu topMenu;
	
	
	private ImageController imageController;
	private CategoryController categoryController;
	
	private SourceImageViewComponent sourceImageView;
	private BoardRegionViewPane boardRegionViewPane;
	
	public ImageCollectionViewPanel getImageCollectionView() {
		return imageCollectionView;
	}

	public void setImageCollectionView(
			ImageCollectionViewPanel imageCollectionView) {
		this.imageCollectionView = imageCollectionView;
	}

	public MainTopMenu getTopMenu() {
		return topMenu;
	}

	public void setTopMenu(MainTopMenu topMenu) {
		this.topMenu = topMenu;
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

	public SourceImageViewComponent getSourceImageView() {
		return sourceImageView;
	}

	public void setSourceImageView(SourceImageViewComponent sourceImageView) {
		this.sourceImageView = sourceImageView;
	}

	public BoardRegionViewPane getBoardRegionViewPane() {
		return boardRegionViewPane;
	}

	public void setBoardRegionViewPane(BoardRegionViewPane boardRegionViewPane) {
		this.boardRegionViewPane = boardRegionViewPane;
	}

}
