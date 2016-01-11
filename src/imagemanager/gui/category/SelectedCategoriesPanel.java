/*
 * Created by JFormDesigner on Thu Feb 26 14:54:54 CET 2015
 */

package imagemanager.gui.category;

import imagemanager.controller.ImageQueryController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author lol rofl
 */
public class SelectedCategoriesPanel extends JPanel {
	
	public ImageQueryController imageQueryController;
	
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem unselectCategoriesMenuItem = new JMenuItem("Usuń z widoku");	
	{
		unselectCategoriesMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<CategoryViewObject> valuesList = categoryList.getSelectedValuesList();

				DefaultListModel<CategoryViewObject> model = (DefaultListModel<CategoryViewObject>)categoryList.getModel();

				for (CategoryViewObject categoryViewObject : valuesList) {
					model.removeElement(categoryViewObject);
				}
			}
		});
		popupMenu.add(unselectCategoriesMenuItem);
	}
	
	public SelectedCategoriesPanel() {
		initComponents();
		addActions();
	}

	private void initComponents() {
		
		scrollPane1 = new JScrollPane();
		categoryList = new JList<CategoryViewObject>();
		categoryList.setModel(new DefaultListModel<CategoryViewObject>());
		label1 = new JLabel();
		refreshButton = new JButton();
		selectAllCheckBox = new JCheckBox();
		selectUnassignedCheckBox = new JCheckBox();
		
		scrollPane1.setViewportView(categoryList);
		refreshButton.setText("Odswiez widok");
		selectAllCheckBox.setText("Wszystkie zdjecia");
		selectUnassignedCheckBox.setText("Nieprzypisane zdjecia");
		

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(categoryList);
		}

		//---- label1 ----
		label1.setText("Wybrane kategorie");

		//---- button1 ----
		refreshButton.setText("Odswiez widok");

		//---- checkBox1 ----
		selectAllCheckBox.setText("Wszystkie zdjecia");

		//---- checkBox2 ----
		selectUnassignedCheckBox.setText("Nieprzypisane zdjecia");

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addComponent(label1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup()
					.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
					.addGap(0, 0, Short.MAX_VALUE))
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup()
						.addComponent(selectUnassignedCheckBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectAllCheckBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
						.addComponent(refreshButton, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addComponent(label1)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(selectUnassignedCheckBox)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(selectAllCheckBox)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(refreshButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(19, Short.MAX_VALUE))
		);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	private void addActions(){
		selectAllCheckBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(selectAllCheckBox.isSelected()){
					categoryList.setEnabled(false);
					selectUnassignedCheckBox.setEnabled(false);
				} else {
					selectUnassignedCheckBox.setEnabled(true);
					categoryList.setEnabled(true);
				}
			}
		});
		
		selectUnassignedCheckBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
		refreshButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				imageQueryController.queryForImages();
			}
		});
		
		categoryList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println(e.getFirstIndex());
			}
		});
		
		categoryList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getButton()==MouseEvent.BUTTON3){
					popupMenu.show(categoryList, e.getX(), e.getY());
				}
			}
		});
	}
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - lol rofl
	private JScrollPane scrollPane1;
	private JList<CategoryViewObject> categoryList;
	private JLabel label1;
	private JButton refreshButton;
	private JCheckBox selectAllCheckBox;
	private JCheckBox selectUnassignedCheckBox;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	
	private Map<String, CategoryViewObject> chosenCategories = new HashMap<String, CategoryViewObject>();
	
	public void addCategory(CategoryViewObject catObj){
		if(chosenCategories.put(catObj.getTitle(), catObj) == null){
			getListModel().addElement(catObj);
		}
	}
	
	public Collection<CategoryViewObject> getChosenCategories(){
		return chosenCategories.values();
	}
	
	public boolean isChooseAllSelected(){
		return selectAllCheckBox.isSelected();
	}
	
	private DefaultListModel<CategoryViewObject> getListModel(){
		return (DefaultListModel<CategoryViewObject>) categoryList.getModel();
		
	}

	public ImageQueryController getImageQueryController() {
		return imageQueryController;
	}

	public void setImageQueryController(ImageQueryController imageQueryController) {
		this.imageQueryController = imageQueryController;
	}

}
