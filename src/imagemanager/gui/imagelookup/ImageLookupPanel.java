package imagemanager.gui.imagelookup;

import imagemanager.model.SourceImage;
import imagemanager.view.SourceImageViewComponent;
import imageprocessing.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ImageLookupPanel extends JPanel {

	private JSplitPane verticalSplit = new JSplitPane();
	//private JSplitPane horizontalSplit = new JSplitPane();
	//private JSplitPane horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	private SourceImageViewComponent sourceImageViewComponent;
	//private JPanel boardRegionContainerPanel;
	private BoardRegionLookupPane boardRegionsPane;

	public ImageLookupPanel() {
		
	}

	public void initialize() {
		
		//BoardRegionToolbar = new BoardRegionToolbar();
		//BoardRegionToolbar.setPreferredSize(new Dimension(100, 100));
		//BoardRegionToolbar.setBackground(Color.RED);
		//boardRegionContainerPanel = new JPanel();
		//boardRegionContainerPanel.add(BoardRegionToolbar);
		//boardRegionContainerPanel.add(boardRegionsPane);
		
//		horizontalSplit.setTopComponent(boardRegionsPane);
//		horizontalSplit.setResizeWeight(1.0);
//		horizontalSplit.setOneTouchExpandable(true);
		
		sourceImageViewComponent.setPreferredSize(new Dimension(400, 400));
		verticalSplit.setLeftComponent(sourceImageViewComponent);
		verticalSplit.setRightComponent(boardRegionsPane);
		verticalSplit.setOneTouchExpandable(true);
		this.setLayout(new BorderLayout());
		this.add(verticalSplit);
		
		
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("ImageLookupPanel foucus lost");
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("ImageLookupPanel focus gained");
			}
		});
	}

	public SourceImageViewComponent getSourceImageViewComponent() {
		return sourceImageViewComponent;
	}

	public void setSourceImageViewComponent(
			SourceImageViewComponent sourceImageViewComponent) {
		this.sourceImageViewComponent = sourceImageViewComponent;
	}

	public BoardRegionLookupPane getBoardRegionsPane() {
		return boardRegionsPane;
	}

	public void setBoardRegionsPane(BoardRegionLookupPane boardRegionsPane) {
		this.boardRegionsPane = boardRegionsPane;
	}

}
