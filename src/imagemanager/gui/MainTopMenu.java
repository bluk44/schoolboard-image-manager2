package imagemanager.gui;

import imagemanager.controller.BoardRegionController;
import imagemanager.controller.CategoryController;
import imagemanager.controller.ImageController;
import imagemanager.model.BoardRegion.BoardType;
import imagemanager.model.BoardRegionParams;
import imagemanager.model.BoardRegionSettings;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class MainTopMenu extends JMenuBar {
	JMenu categoryMenu = new JMenu("Kategoria");
	JMenuItem createCategoryMenuItem = new JMenuItem("Stwórz nową kategorię");
	JMenuItem deleteCategoryMenuItem = new JMenuItem("Usuń kategorię");
	
	JMenu imageMenu = new JMenu("Zdjęcie");
	JMenuItem importImagesMenuItem = new JMenuItem("Zaimportuj zdjęcia");

	JMenu boardRegionMenu = new JMenu("Tablica");
	
	JMenu setBoardCleanerMenu = new JMenu("Rodzaj tablicy");
	JRadioButtonMenuItem blackBoardOption = new JRadioButtonMenuItem("Ciemne tło, jasne litery");
	JRadioButtonMenuItem whiteBoardOption = new JRadioButtonMenuItem("Jasne tło, ciemne litery");	
	
	JMenuItem editBoardRegionOption = new JMenuItem("Edytuj region tablcy");
	
	CategoryController categoryController;
	ImageController imageController;
	BoardRegionController boardController;
	
	BoardRegionSettings boardRegionSettings;
	
	public MainTopMenu(){
		setupCategoryMenu();
		setupImageMenu();
		setupBoardRegionMenu();
		setupActionLIseners();
	}
	
	public void initialize(){
		blackBoardOption.setSelected(true);
		for(ActionListener a : blackBoardOption.getActionListeners()){
			a.actionPerformed(new ActionEvent(blackBoardOption, ActionEvent.ACTION_PERFORMED, null));
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
		
	private void setupCategoryMenu(){
		categoryMenu.add(createCategoryMenuItem);
		categoryMenu.add(deleteCategoryMenuItem);
		
		add(categoryMenu);
	}
	
	private void setupImageMenu(){
		imageMenu.add(importImagesMenuItem);
		
		add(imageMenu);
	}
	
	private void setupBoardRegionMenu(){
		boardRegionMenu.add(editBoardRegionOption);
		setupBoardCleanerMenu();

		add(boardRegionMenu);
	}
	
	private void setupBoardCleanerMenu(){
				
	    ButtonGroup group = new ButtonGroup();
	    
	    whiteBoardOption.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
							
			}
	    	
	    });
	    
	    group.add(whiteBoardOption);
	    group.add(blackBoardOption);
		
		setBoardCleanerMenu.add(blackBoardOption);
		setBoardCleanerMenu.add(whiteBoardOption);
		
		boardRegionMenu.add(setBoardCleanerMenu);
		
	}
	
	private void setupActionLIseners(){
		
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
		
		importImagesMenuItem.addActionListener(new ImportImagesActionListener());
		
		editBoardRegionOption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BoardRegionEditionDialog dialog = new BoardRegionEditionDialog(null, false, boardController);
			}
		});
		
		blackBoardOption.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boardRegionSettings.setBoardType(BoardType.BLACKBOARD);
				boardRegionSettings.setBoardRegionParams(BoardRegionParams.getDefaultBBParams());
			}
			
		});
		
		whiteBoardOption.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("whiteboard active");
				boardRegionSettings.setBoardType(BoardType.WHITEBOARD);
				boardRegionSettings.setBoardRegionParams(BoardRegionParams.getDefaultWBParams());			
			}
			
		});
	}
	
	class ImportImagesActionListener implements ActionListener {

		JFileChooser fc = new JFileChooser();

		@Override
		public void actionPerformed(ActionEvent e) {
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fc.addChoosableFileFilter(new ImageFilter());
			fc.setMultiSelectionEnabled(true);
			int val = fc.showOpenDialog((Component) e.getSource());

			if (val == JFileChooser.APPROVE_OPTION) {
				File[] files = fc.getSelectedFiles();
				
				for (File file : files) {
					if (file.isDirectory()) {
						File[] imageFiles = file.listFiles(new ImageFilter());
						for (File imageFile : imageFiles) {
							imageController.createSourceImage(imageFile);
						}
					} else if (file.isFile() && new ImageFilter().accept(file)) {
						imageController.createSourceImage(file);
					}
				}
			}
		}

	}

	class ImageFilter extends FileFilter implements java.io.FileFilter {

		@Override
		public boolean accept(File f) {

			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals("tiff") || extension.equals("tif")
						|| extension.equals("gif") || extension.equals("jpeg")
						|| extension.equals("jpg") || extension.equals("png")) {
					return true;
				} else {
					return false;
				}
			}

			return false;
		}

		@Override
		public String getDescription() {
			return "Image Files";
		}

		private String getExtension(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 && i < s.length() - 1) {
				ext = s.substring(i + 1).toLowerCase();
			}
			return ext;
		}

	}

	public BoardRegionSettings getBoardRegionSettings() {
		return boardRegionSettings;
	}

	public void setBoardRegionSettings(BoardRegionSettings boardRegionSettings) {
		this.boardRegionSettings = boardRegionSettings;
	}

	public BoardRegionController getBoardController() {
		return boardController;
	}

	public void setBoardController(BoardRegionController boardController) {
		this.boardController = boardController;
	}


}
