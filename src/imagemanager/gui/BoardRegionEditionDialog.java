package imagemanager.gui;

import imagemanager.controller.BoardRegionController;
import imagemanager.model.BoardRegionParams;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class BoardRegionEditionDialog extends JDialog {

	private JButton btnIncBlur, btnDecBlur;
	private JButton btnIncThreshBlock, btnDecThreshBlock;
	private JButton btnIncThreshConst, btnDecThreshConst;
	private JButton btnSave;
	
	private JLabel lblBlur, lblThreshBlock, lblThreshConst;
	
	private JTextField tfBlur, tfThreshBlock, tfThreshConst;
	
	private BoardRegionController boardController;
	
	{
		
		Icon icnPlus = new ImageIcon("icons/plus.png");
		Icon icnMinus = new ImageIcon("icons/minus.png");

		btnIncBlur = new JButton(icnPlus);
		btnIncBlur.setPreferredSize(new Dimension(18, 18));
		
		btnDecBlur = new JButton(icnMinus);
		btnDecBlur.setPreferredSize(new Dimension(18, 18));

		btnIncThreshBlock = new JButton(icnPlus);
		btnIncThreshBlock.setPreferredSize(new Dimension(18, 18));
		
		btnDecThreshBlock = new JButton(icnMinus);
		btnDecThreshBlock.setPreferredSize(new Dimension(18, 18));
		
		btnIncThreshConst = new JButton(icnPlus);
		btnIncThreshConst.setPreferredSize(new Dimension(18, 18));
		
		btnDecThreshConst = new JButton(icnMinus);
		btnDecThreshConst.setPreferredSize(new Dimension(18, 18));
		
		btnSave = new JButton("Zapisz");
		
		tfBlur = new JTextField(3);
		tfThreshBlock = new JTextField(3);		
		tfThreshConst= new JTextField(3);
		
		lblBlur = new JLabel("Wygładzanie");
		lblThreshBlock = new JLabel("Obszar progowania");
		lblThreshConst = new JLabel("Stała progowania");
	}

	public BoardRegionEditionDialog(JFrame frame, boolean modal, BoardRegionController boardController) {
		super(frame, modal);
		
		this.boardController = boardController;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		add(lblBlur, c);
		
		c.gridx = 1;
		add(btnIncBlur, c);
		
		c.gridx = 2;                                                   
		add(tfBlur, c);
		
		c.gridx = 3;
		add(btnDecBlur);
		
		c.gridx = 0;
		c.gridy = 1;
		add(lblThreshBlock, c);
		
		c.gridx = 1;
		add(btnIncThreshBlock, c);
		
		c.gridx = 2;
		add(tfThreshBlock, c);
		
		c.gridx = 3;
		add(btnDecThreshBlock, c);
		
		c.gridx = 0;
		c.gridy = 2;
		add(lblThreshConst, c);
		
		c.gridx = 1;
		add(btnIncThreshConst, c);
		
		c.gridx = 2;
		add(tfThreshConst, c);
		
		c.gridx = 3;
		add(btnDecThreshConst, c);
		
		c.gridx = 2;
		c.gridy = 4;
		add(btnSave, c);
		
		setUpListeners();
		setInitialBoardParameters();
		
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}
	
	private void setUpListeners(){
		btnIncBlur.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boardController.getParams().blurMaskSize += 2;
				boardController.updateBoardRegion();
				
				tfBlur.setText(""+boardController.getParams().blurMaskSize);
			}
		});
		
		btnDecBlur.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
								
				boardController.getParams().blurMaskSize -= 2;
				if(boardController.getParams().blurMaskSize < 0){
					boardController.getParams().blurMaskSize = 1;
				}
				boardController.updateBoardRegion();
				tfBlur.setText(""+boardController.getParams().blurMaskSize);

			}
		});
		
		btnIncThreshBlock.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				boardController.getParams().threshholdBlockSize += 2;
				boardController.updateBoardRegion();
				tfThreshBlock.setText(""+boardController.getParams().threshholdBlockSize);				
				
			}
		});
		
		btnDecThreshBlock.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				boardController.getParams().threshholdBlockSize -= 2;
				if(boardController.getParams().threshholdBlockSize < 0){
					boardController.getParams().threshholdBlockSize = 1;
				}
				boardController.updateBoardRegion();
				tfThreshBlock.setText(""+boardController.getParams().threshholdBlockSize);				
				
			}
		});
		
		btnIncThreshConst.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boardController.getParams().thresholdConstant += 1;
				boardController.updateBoardRegion();
				tfThreshConst.setText(""+boardController.getParams().thresholdConstant);
			}
		});
		
		btnDecThreshConst.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boardController.getParams().thresholdConstant -= 1;
				boardController.updateBoardRegion();
				tfThreshConst.setText(""+boardController.getParams().thresholdConstant);
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boardController.saveBoardRegion();
			}
		});
	}
	
	private void setInitialBoardParameters(){
		BoardRegionParams p = boardController.getParams();
		tfBlur.setText(""+p.blurMaskSize);
		tfThreshBlock.setText(""+p.threshholdBlockSize);
		tfThreshConst.setText(""+p.thresholdConstant);
		
	}
	
	public static void main(String[] args) {

		final JFrame frame = new JFrame("dialog test");
		JButton btn = new JButton();

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BoardRegionEditionDialog dialog = new BoardRegionEditionDialog(
						null, false, null);
			}
		});

		frame.setPreferredSize(new Dimension(800, 600));
		frame.add(btn);

		frame.pack();
		frame.setVisible(true);
	}

}
