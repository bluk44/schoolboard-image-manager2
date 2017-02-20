package imagemanager.gui.imagelookup;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import imagemanager.view.BoardRegionViewComponent;

import javax.swing.JSplitPane;

public class BoardRegionViewPane extends JSplitPane {
	
	private BoardRegionViewComponent boardRegionView;
	private BoardRegionToolbar boardRegionToolbar;
	
	public void initialize(){
		
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		setTopComponent(boardRegionToolbar);
		setBottomComponent(boardRegionView);
		
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				boardRegionView.requestFocus();
			}
		});
	}

	public BoardRegionViewComponent getBoardRegionView() {
		return boardRegionView;
	}

	public void setBoardRegionView(BoardRegionViewComponent boardRegionView) {
		this.boardRegionView = boardRegionView;
	}

	public BoardRegionToolbar getBoardRegionToolbar() {
		return boardRegionToolbar;
	}

	public void setBoardRegionToolbar(BoardRegionToolbar boardRegionToolbar) {
		this.boardRegionToolbar = boardRegionToolbar;
		
	}
	
	
}
