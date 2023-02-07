import java.util.Random;

public enum TerminalColor {
	Black,
	DarkRed,
	DarkGreen,
	DarkYellow,
	DarkBlue,
	DarkPink,
	DarkCyan,
	LightGray,
	DarkGray,
	Red,
	Green,
	Yellow,
	Blue,
	Pink,
	Cyan,
	White,
	Transparent;

	public int fgAnsiCode() {
		switch (this) {
			case Black: return 30;
			case DarkRed: return 31;
			case DarkGreen: return 32;
			case DarkYellow: return 33;
			case DarkBlue: return 34;
			case DarkPink: return 35;
			case DarkCyan: return 36;
			case LightGray: return 37;
			case DarkGray: return 90;
			case Red: return 91;
			case Green: return 92;
			case Yellow: return 93;
			case Blue: return 94;
			case Pink: return 95;
			case Cyan: return 96;
			case White: return 97;

			// transparent should be handled in code, if we end up here we return white
			case Transparent: return 97;
			default: return 0;
		}
	}

	public int bgAnsiCode() {
		switch (this) {
			case Black: return 40;
			case DarkRed: return 41;
			case DarkGreen: return 42;
			case DarkYellow: return 43;
			case DarkBlue: return 44;
			case DarkPink: return 45;
			case DarkCyan: return 46;
			case LightGray: return 47;
			case DarkGray: return 100;
			case Red: return 101;
			case Green: return 102;
			case Yellow: return 103;
			case Blue: return 104;
			case Pink: return 105;
			case Cyan: return 106;
			case White: return 107;

			// transparent should be handled in code, if we end up here we return black
			case Transparent: return 40;
			default: return 0;
		}
	}

	public TerminalColor negative() {
		switch (this) {
			case Black: return White;
			case DarkRed: return Cyan;
			case DarkGreen: return Pink;
			case DarkYellow: return Blue;
			case DarkBlue: return Yellow;
			case DarkPink: return Green;
			case DarkCyan: return Red;
			case LightGray: return DarkGray;
			case DarkGray: return LightGray;
			case Red: return DarkCyan;
			case Green: return DarkPink;
			case Yellow: return DarkBlue;
			case Blue: return DarkYellow;
			case Pink: return DarkGreen;
			case Cyan: return DarkRed;
			case White: return Black;

			// negative of transparent is still transparent
			case Transparent: return Transparent;
			default: return Black;
		}
	}

	public static TerminalColor randomColor() {
		Random rnd = new Random();
		int i = rnd.nextInt(TerminalColor.values().length - 1); // we don't want to return transparent as color
		return TerminalColor.values()[i];
	}
}