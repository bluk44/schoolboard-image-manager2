package imagemanager.gui.imagelookup;

import imagemanager.controller.BoardRegionController;
import imagemanager.controller.ImageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BoardRegionToolbar extends JPanel {

	private JButton btn1 = new JButton("Korekcja tła");
	private JButton btn2 = new JButton("Progowanie");
	private JButton btn3 = new JButton("Zapisz");
	
	public BoardRegionController brController;
	
	public BoardRegionToolbar() {
		add(btn1);
		add(btn2);
		add(btn3);

		btn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BackgroundCorrectionDialog backgroundCorrectionDialog = new BackgroundCorrectionDialog();

				backgroundCorrectionDialog.show();
			}
		});
		
		btn2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ThresholdDialog dialog = new ThresholdDialog();
				dialog.show();
			}
		});
	}

	class BackgroundCorrectionDialog {
		JLabel blurLabel = new JLabel("Blur mask size");
		JTextField blur = new JTextField();

		void show() {

			JComponent[] inputs = new JComponent[2];
			inputs[0] = blurLabel;
			inputs[1] = blur;

			int result = JOptionPane.showConfirmDialog(null, inputs,
					"My custom dialog", JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				//System.out.println("You entered " + blur.getText());
				int blurVal = Integer.parseInt(blur.getText());
				brController.getParams().blurMaskSize = blurVal;
				brController.updateBoardRegion();
			} else {
				System.out
						.println("User canceled / closed the dialog, result = "
								+ result);
			}
		}
	}
	
	class ThresholdDialog{
		JLabel thresholdBlockSize = new JLabel("Rozmiar siatki progowania");
		JTextField blockSize = new JTextField();
		JLabel thresholdConst = new JLabel("Stała progowania");
		JTextField c = new JTextField();
		
		void show() {

			JComponent[] inputs = new JComponent[4];
			inputs[0] = thresholdBlockSize;
			inputs[1] = blockSize;
			inputs[2] = thresholdConst;
			inputs[3] = c;
			
			int result = JOptionPane.showConfirmDialog(null, inputs,
					"My custom dialog", JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				//System.out.println("You entered " + blur.getText());
				int blockVal = Integer.parseInt(blockSize.getText());
				int cVal = Integer.parseInt(c.getText());
				brController.getParams().threshholdBlockSize = blockVal;
				brController.getParams().thresholdConstant = cVal;
				brController.updateBoardRegion();
			} else {
				System.out
						.println("User canceled / closed the dialog, result = "
								+ result);
			}
		}
	}
	
	public BoardRegionController getBrController() {
		return brController;
	}

	public void setBrController(BoardRegionController brController) {
		this.brController = brController;
	}
}
