package imagemanager.gui.imagelookup;

import imagemanager.model.BoardRegion;
import imageprocessing.Util;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;

public class BoardRegionsPane extends JDesktopPane {
	
	
	
	public void openBoardRegion(BoardRegion boardRegion){
		
//		BoardRegionViewComponent comp = new BoardRegionViewComponent(boardRegion);
//			
//		comp.setMinimumSize(new Dimension(300, 240));
//		
//		this.add(comp);
//		comp.setVisible(true);
//		comp.moveToFront();
		
//		final ImageViewComponent imgComp = new ImageViewComponent();
//		imgComp.initialize();
//		imgComp.setImage(Util.readImageFromFile("images/wb01.png"));
				
//		JInternalFrame newWindow = new JInternalFrame(("internal Window"), true, true, true, true);
//		MyInternalFrame newWindow = new MyInternalFrame("test");
		BoardRegionViewComponent newWindow = new BoardRegionViewComponent(boardRegion);
		newWindow.getViewComponent().requestFocusInWindow();
		newWindow.setVisible(true);
		newWindow.setSize(400, 400);
		newWindow.setMinimumSize(new Dimension(400, 400));
//		newWindow.add(imgComp);
		this.add(newWindow);
		newWindow.moveToFront();
	}
	
	class MyInternalFrame extends JInternalFrame {
		
		ImageViewComponent imgComp = new ImageViewComponent();
		
		public MyInternalFrame(String title){
			super(title, true, true, true, true);			
			imgComp.initialize();
			imgComp.setImage(Util.readImageFromFile("images/wb01.png"));
			this.add(imgComp);
		}
	}
}
