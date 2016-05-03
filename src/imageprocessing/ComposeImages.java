package imageprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ComposeImages {

	public static BufferedImage compose(BufferedImage image, BufferedImage mask, Color bgColor) {
		BufferedImage result = Util.copy(image);

		WritableRaster resRast = result.getRaster();
		WritableRaster maskRast = mask.getRaster();
		
		int imgW = image.getWidth(), imgH = image.getHeight();
		
		for(int i = 0; i < imgW; i++){
			for(int j = 0; j < imgH; j++){
				int rastSample = maskRast.getSample(i, j, 0);
				if(rastSample == 0){
					result.setRGB(i, j, bgColor.getRGB());
				}
			}
		}
		
		return result;
	}

}
