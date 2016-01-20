package imageprocessing;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import java.awt.Rectangle;
import mmorpho.Constants;
import mmorpho.MorphoProcessor;
import mmorpho.StructureElement;

public class Morphology{

	public enum StructElType {
		CIRCLE(0), DIAMOND(1), SQUARE(7), H_LINE(4), V_LINE(3), H_2P(6), V_2P(5);
		public int val;

		private StructElType(int val) {
			this.val = val;
		}
	};

	public enum OpType {
		ERODE, DILATE, OPEN, CLOSE, FAST_ERODE, FAST_DILATE, FAST_OPEN, FAST_CLOSE
	};

	public static void run(ImagePlus imp, OpType op, StructElType strEl, float radius) {

		StructureElement se = new StructureElement(strEl.val, 1, radius, Constants.OFFSET0);
	
		MorphoProcessor mp = new MorphoProcessor(se);

		Rectangle rect = imp.getProcessor().getRoi();

		ByteProcessor bp = getMask((ByteProcessor) imp.getProcessor(), rect);
		switch (op) {
		case CLOSE:
			mp.close(bp);
			break;
		case DILATE:
			mp.dilate(bp);
			break;
		case ERODE:
			mp.dilate(bp);
			break;
		case OPEN:
			mp.open(bp);
			break;
		case FAST_CLOSE:
			if (se.getType() == 4 || se.getType() == 3) {
				mp.LineDilate(bp);
				mp.LineErode(bp);
			}
			mp.fclose(bp);
			break;
		case FAST_OPEN:
			if (se.getType() == 4 || se.getType() == 3) {
				mp.LineErode(bp);
				mp.LineDilate(bp);
			}
			mp.fopen(bp);
			break;
		case FAST_DILATE:
			if (se.getType() == 4 || se.getType() == 3) {
				mp.LineDilate(bp);
			}
			mp.fastDilate(bp);
			break;
		case FAST_ERODE:
			if ((se.getType() == 4) || (se.getType() == 3)) {
				mp.LineErode(bp);
				return;
			}
			mp.fastErode(bp);
			break;
		}
		imp.getProcessor().insert(bp, rect.x, rect.y);
	}
	
	public static ByteProcessor getMask(ByteProcessor paramByteProcessor, Rectangle paramRectangle) {
		int i = paramByteProcessor.getWidth();

		byte[] arrayOfByte1 = (byte[]) paramByteProcessor.getPixels();
		int j = (int) paramRectangle.getX();
		int k = (int) paramRectangle.getY();
		int l = (int) paramRectangle.getWidth();
		int i1 = (int) paramRectangle.getHeight();
		byte[] arrayOfByte2 = new byte[l * i1];
		for (int i2 = 0; i2 < arrayOfByte2.length; ++i2) {
			int i3 = j + i2 % l + i2 / l * i + k * i;
			arrayOfByte2[i2] = (byte) (arrayOfByte1[i3] & 0xFF);
		}
		return new ByteProcessor(l, i1, arrayOfByte2, paramByteProcessor.getColorModel());
	}

}
