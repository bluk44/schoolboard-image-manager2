package imagemanager.controller;

import imagemanager.model.BoardRegion;
import imagemanager.model.BoardRegionParams;
import imagemanager.persistence.ImageRepository;
import imagemanager.view.BoardRegionViewComponent;

public class BoardRegionController {
	
	BoardRegion boardRegion;
	ImageRepository imageRepo;
	
	BoardRegionViewComponent view;
	
	public void openBoardRegion(Long id){
		boardRegion = imageRepo.loadBoardRegion(id);
		updateView();
	}
	
	private void updateView(){
		view.setBoardRegion(boardRegion);
		view.repaint();
	}

	public ImageRepository getImageRepo() {
		return imageRepo;
	}

	public void setImageRepo(ImageRepository imageRepo) {
		this.imageRepo = imageRepo;
	}

	public BoardRegionViewComponent getView() {
		return view;
	}

	public void setView(BoardRegionViewComponent view) {
		this.view = view;
	}
	
	public BoardRegionParams getParams(){
		return boardRegion.getParams();
	}
	
	public void updateBoardRegion(){
		boardRegion.clearBackground();
		updateView();
	}
	
	public void saveBoardRegion(){
		imageRepo.updateBoardRegion(boardRegion);
	}
}
