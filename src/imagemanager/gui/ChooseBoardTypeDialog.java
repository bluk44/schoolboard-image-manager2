package imagemanager.gui;

import javax.swing.JDialog;
import javax.swing.JRadioButton;

public class ChooseBoardTypeDialog extends JDialog {
	
	JRadioButton btn1 = new JRadioButton("tablica z jasnym tłem");
	JRadioButton btn2 = new JRadioButton("tablica z ciemnym tłem");
	
	{
		this.add(btn1);
		this.add(btn2);
	}
}
