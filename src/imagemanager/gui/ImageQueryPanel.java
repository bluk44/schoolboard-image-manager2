package imagemanager.gui;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;

public class ImageQueryPanel extends JPanel {

	private javax.swing.JCheckBox allLabelsCheckBox;
	private javax.swing.JCheckBox unassignedCheckBox;
	private javax.swing.JButton showImagesButton;
	
	public ImageQueryPanel() {
		initComponents();
	}
	
	public boolean isAllLabelsCheckBoxSelected(){
		return allLabelsCheckBox.isSelected();
	}
	
	public boolean isUnassignedCheckBoxSelected(){
		return unassignedCheckBox.isSelected();
	}
	
	public void addAllLabelsCheckBoxActionListener(ActionListener listener){
		allLabelsCheckBox.addActionListener(listener);
	}
	
	public void addUnassignedCheckBoxActionListener(ActionListener listener){
		unassignedCheckBox.addActionListener(listener);
	}
	
	public void addShowImagesButtonActionListener(ActionListener listener){
		showImagesButton.addActionListener(listener);
	}
	
	@SuppressWarnings("unchecked")
	private void initComponents() {

		allLabelsCheckBox = new javax.swing.JCheckBox();
		unassignedCheckBox = new javax.swing.JCheckBox();
		showImagesButton = new javax.swing.JButton();

		allLabelsCheckBox.setText("Zaznacz wszystkie");
		unassignedCheckBox.setText("Nieprzypisane");

		showImagesButton.setText("Pokaż zdjęcia");
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addComponent(allLabelsCheckBox)
				.addComponent(unassignedCheckBox)
				.addComponent(showImagesButton));

		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup()
				.addComponent(allLabelsCheckBox)
				.addComponent(unassignedCheckBox)
				.addComponent(showImagesButton)));
		
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
	}
	
	private void addActions(){
		
	}

}
