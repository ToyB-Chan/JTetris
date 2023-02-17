public class TerminalColor {
	public int r;
	public int g;
	public int b;
	public boolean isTransparent = false;

	public TerminalColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public TerminalColor(int r, int g, int b, boolean transparent) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.isTransparent = transparent;
	}

	public void invert() {
		this.r = (255 - this.r);
		this.g = (255 - this.g);
		this.b = (255 - this.b);
	}

	public static final TerminalColor black = new TerminalColor(0, 0, 0);
	public static final TerminalColor darkRed = new TerminalColor(139, 0, 0);
	public static final TerminalColor darkGreen = new TerminalColor(0, 100, 0);
	public static final TerminalColor orange = new TerminalColor(255, 127, 0);
	public static final TerminalColor darkBlue = new TerminalColor(0, 0, 139);
	public static final TerminalColor magenta = new TerminalColor(139, 0, 139);
	public static final TerminalColor darkCyan = new TerminalColor(0, 139, 139);
	public static final TerminalColor darkGray = new TerminalColor(77, 77, 77);
	public static final TerminalColor lightGray = new TerminalColor(161, 161, 161);
	public static final TerminalColor red = new TerminalColor(255, 0, 0);
	public static final TerminalColor green = new TerminalColor(0, 205, 0);
	public static final TerminalColor yellow = new TerminalColor(255, 215, 0);
	public static final TerminalColor blue = new TerminalColor(30, 144, 255);
	public static final TerminalColor pink = new TerminalColor(255, 105, 180);
	public static final TerminalColor aqua = new TerminalColor(72, 209, 204);
	public static final TerminalColor white = new TerminalColor(255, 255, 255);
	public static final TerminalColor transparent = new TerminalColor(0, 0, 0, true);
}