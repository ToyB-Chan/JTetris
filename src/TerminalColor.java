import java.util.Random;

public class TerminalColor {
	public final int R;
	public final int G;
	public final int B;

	public TerminalColor(int R, int G, int B) {
		// clamp values between 0-255
		R = R > 255 ? 255 : R < 0 ? 0 : R;
		G = G > 255 ? 255 : G < 0 ? 0 : G;
		B = B > 255 ? 255 : B < 0 ? 0 : B;
		
		this.R = R;
		this.G = G;
		this.B = B;
	}

	public TerminalColor getInverted() {
		return new TerminalColor(255 - this.R, 255 - this.G, 255 - this.B);
	}

	public static TerminalColor getRandomColor(int seed) {
		int random = new Random(seed).nextInt(5);

		switch(random) {
			case (0):
		  		return TerminalColor.BLUE;

			case (1):
				return TerminalColor.RED;

			case (2):
			 	return TerminalColor.YELLOW;

			case (3):
			 	return TerminalColor.GREEN;

			case (4):
				return TerminalColor.MAGENTA;
			
			default: return null; // Should never happen
		}		
	}

	public static final TerminalColor BLACK = new TerminalColor(0, 0, 0);
	public static final TerminalColor DARK_RED = new TerminalColor(139, 0, 0);
	public static final TerminalColor DARK_GREEN = new TerminalColor(0, 100, 0);
	public static final TerminalColor ORANGE = new TerminalColor(255, 127, 0);
	public static final TerminalColor DARK_BLUE = new TerminalColor(3, 48, 161);
	public static final TerminalColor MAGENTA = new TerminalColor(139, 0, 139);
	public static final TerminalColor DARK_CYAN = new TerminalColor(0, 139, 139);
	public static final TerminalColor DARK_GRAY = new TerminalColor(77, 77, 77);
	public static final TerminalColor LIGHT_GRAY = new TerminalColor(161, 161, 161);
	public static final TerminalColor RED = new TerminalColor(205, 0, 0);
	public static final TerminalColor GREEN = new TerminalColor(0, 205, 0);
	public static final TerminalColor YELLOW = new TerminalColor(255, 215, 0);
	public static final TerminalColor BLUE = new TerminalColor(30, 144, 255);
	public static final TerminalColor PINK = new TerminalColor(255, 105, 180);
	public static final TerminalColor AQUA = new TerminalColor(72, 209, 204);
	public static final TerminalColor WHITE = new TerminalColor(255, 255, 255);
	public static final TerminalColor TRANSPARENT = new TerminalColor(0, 0, 0);
}