package imagemanager.gui.imagelookup;

import imagemanager.model.BoardRegion;
import imagemanager.model.SourceImage;
import imageprocessing.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ImageLookupPanel extends JPanel {

	// model class
	private SourceImage sourceImage;

	private JSplitPane splitPane = new JSplitPane();
	private SourceImageViewComponent sourceImageViewComponent;

	public ImageLookupPanel() {
	}

	public void initialize() {
		
		sourceImageViewComponent.setPreferredSize(new Dimension(400, 400));
		splitPane.setLeftComponent(sourceImageViewComponent);
		splitPane.setOneTouchExpandable(true);
		this.setLayout(new BorderLayout());
		this.add(splitPane);
	}

	public SourceImageViewComponent getSourceImageViewComponent() {
		return sourceImageViewComponent;
	}

	public void setSourceImageViewComponent(
			SourceImageViewComponent sourceImageViewComponent) {
		this.sourceImageViewComponent = sourceImageViewComponent;
	}

	public SourceImage getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(SourceImage sourceImage) {
		this.sourceImage = sourceImage;
		updateView();
	}

	private void updateView() {
		BufferedImage image = Util.getBufferedImage(sourceImage.getPixels());
		sourceImageViewComponent.setImage(image);
		Set<BoardRegion> breg = sourceImage.getBoardImages();
		for (BoardRegion boardRegion : breg) {
			sourceImageViewComponent.putPolygon("BR" + boardRegion.getId(),
					boardRegion.getPerimeter().toAWTPolygon());
		}
		sourceImageViewComponent.fitImageToComponent();
		sourceImageViewComponent.repaint();

	}
}
