public class GameObject {
	public int relativeLocationX = 0;
	public int relativeLocationY = 0;
	public GameObject parent;

	public void setRelativeLocation(int x, int y) {
		this.relativeLocationX = x;
		this.relativeLocationY = y;
	}

	public int getAbsoluteLocationX() {
		if (this.parent == null) {
			return this.relativeLocationX;
		} else {
			return this.parent.getAbsoluteLocationX() + this.relativeLocationX;
		}
	}

	public int getAbsoluteLocationY() {
		if (this.parent == null) {
			return this.relativeLocationY;
		} else {
			return this.parent.getAbsoluteLocationY() + this.relativeLocationY;
		}
	}

	public void setAbsoluteLocation(int x, int y) {
		
	}

	public void draw(TerminalCanvas canvas) {

	}
}
