package imagemanager.gui.imagelookup;

public class QuadrangleIncompleteException extends Exception {
	@Override
	public String getMessage() {
		return "Quadranle incomplete - not enough points";
	}
}
