package imagemanager.model;

import imagemanager.model.BoardRegion.BoardType;

public class BoardRegionSettings {
	
	private BoardRegionParams boardRegionParams = BoardRegionParams.getDefaultWBParams();
	private BoardType boardType = BoardType.WHITEBOARD;
	
	public BoardRegionParams getBoardRegionParams() {
		return boardRegionParams;
	}
	public void setBoardRegionParams(BoardRegionParams boardRegionParams) {
		this.boardRegionParams = boardRegionParams;
	}
	public BoardType getBoardType() {
		return boardType;
	}
	public void setBoardType(BoardType boardType) {
		this.boardType = boardType;
	}
	
}
