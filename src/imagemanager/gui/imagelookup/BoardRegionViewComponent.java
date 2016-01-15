package imagemanager.gui.imagelookup;

import imagemanager.model.BoardRegion;
import imageprocessing.Util;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;

public class BoardRegionViewComponent extends JInternalFrame {
	
	protected BoardRegion boardRegion;
	protected ImageViewComponent viewComponent;
	
	public BoardRegionViewComponent(BoardRegion boardRegion){
		super(""+boardRegion.getId(), true, true, true, true);
		this.boardRegion = boardRegion;
		viewComponent = new ImageViewComponent();
		viewComponent.initialize();
		viewComponent.setImage(Util.getBufferedImage(boardRegion.getPixels()));
		this.add(viewComponent);
	
//		this.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				// TODO Auto-generated method stub
//				super.mouseClicked(e);
//				System.out.println("mouse clicked");
//				viewComponent.requestFocus();
//			}
//		});
	}
	
	@Override
	public void moveToFront() {
		// TODO Auto-generated method stub
		super.moveToFront();
		viewComponent.requestFocus();

	}
	
	public ImageViewComponent getViewComponent(){
		return viewComponent;
	}
}
