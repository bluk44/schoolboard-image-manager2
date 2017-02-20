package imagemanager.model;

import javax.persistence.Embeddable;

@Embeddable
public class BoardRegionParams {
	
	public BoardRegionParams(){
		
	}
	
	public BoardRegionParams(int blurMaskSize, int threshholdBlockSize,
			int thresholdConstant) {
		super();
		this.blurMaskSize = blurMaskSize;
		this.threshholdBlockSize = threshholdBlockSize;
		this.thresholdConstant = thresholdConstant;
	}
	
	public int blurMaskSize;
	public int threshholdBlockSize;
	public int thresholdConstant;
	
	public int getBlurMaskSize() {
		return blurMaskSize;
	}
	public int getThreshholdBlockSize() {
		return threshholdBlockSize;
	}
	public int getThresholdConstant() {
		return thresholdConstant;
	}
	
	
	public static BoardRegionParams getDefaultBBParams(){
		BoardRegionParams params = new BoardRegionParams();
		params.blurMaskSize = 11;
		params.threshholdBlockSize = 21;
		params.thresholdConstant = -7;
	
		return params;
	}
	
	public static BoardRegionParams getDefaultWBParams(){
		BoardRegionParams params = new BoardRegionParams();
		params.blurMaskSize = 3;
		params.threshholdBlockSize = 11;
		params.thresholdConstant = 3;
	
		return params;
	}
}
