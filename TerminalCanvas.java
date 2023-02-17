import java.io.PrintStream;

public class TerminalCanvas {

	private int width;
	private int height;
	private TerminalPixel[][] buffer;
	public boolean shouldClearTerminal;
	public TerminalColor clearColor;

	public TerminalCanvas (int width, int height, TerminalColor clearColor) {
		this.width = width;
		this.height = height;
		this.buffer = new TerminalPixel[width][height];
		this.shouldClearTerminal = true;
		this.clearColor = clearColor;
		for (int ix = 0; ix < width; ix++) {
			for (int iy = 0; iy < height; iy++) {
				this.buffer[ix][iy] = new TerminalPixel(' ', clearColor, clearColor);
			}
		}
	}

	public void writeToBuffer(int x, int y, char character, TerminalColor fgColor, TerminalColor bgColor) {
		// discard any writes outside of our bounds
		if (x >= this.width || y >= this.height || x < 0 || y < 0) {
			return;
		}

		if (fgColor == TerminalColor.TRANSPARENT) {
			character = ' ';
		}

		if (bgColor == TerminalColor.TRANSPARENT) {
			bgColor = buffer[x][y].bgColor;
		}

		buffer[x][y].character = character;
		buffer[x][y].fgColor = fgColor;
		buffer[x][y].bgColor = bgColor;
	}

	public void clearBuffer() {
		for (int ix = 0; ix < width; ix++) {
			for (int iy = 0; iy < height; iy++) {
				this.buffer[ix][iy].character = ' ';
				this.buffer[ix][iy].fgColor = this.clearColor;
				this.buffer[ix][iy].bgColor = this.clearColor;
			}
		}
	}

	public void renderBuffer(PrintStream stream) {
		int widthScale = 2;
		String outString = "";
		
		outString += "\033[8;" + this.height + ";" + this.width * widthScale + "t"; // set terminal window dimension to canvas dimension
		outString += "\033[?25l"; // disable cursor

		if (this.shouldClearTerminal) {
			outString += "\033[2J"; // clear terminal
			this.shouldClearTerminal = false;
		}

		outString += "\033[H"; // set cursor to 0, 0
		TerminalColor lastFgColor = null;
		TerminalColor lastBgColor = null;

		for (int iy = 0; iy < this.height; iy++) {
			for (int ix = 0; ix < this.width; ix++) {
				TerminalPixel pixel = buffer[ix][iy];

				if (pixel.fgColor != lastFgColor) {
					// set foregrund color 
					outString += "\033[";
					outString += "38;2;";
					outString += pixel.fgColor.R + ";";
					outString += pixel.fgColor.G + ";";
					outString += pixel.fgColor.B + "m";
					lastFgColor = pixel.fgColor;
				}

				if  (pixel.bgColor != lastBgColor) {
					// set background color
					outString += "\033[";
					outString += "48;2;";
					outString += pixel.bgColor.R + ";";
					outString += pixel.bgColor.G + ";";
					outString += pixel.bgColor.B + "m";
					lastBgColor = pixel.bgColor;
				}

				// set text
				outString += buffer[ix][iy].character;

				// strech if width scale > 1
				for (int i = 1; i < widthScale; i++) {
					outString += " ";
				} 
			}

			// only print a new line if we are not at the end of the loop
			if (iy + 1 < this.height) {
				outString += '\n';
			}
		}

		outString += "\033[0;0m"; // reset all formating

		// push to stream and clear buffer
		stream.print(outString);
		this.clearBuffer();
	}

	public void drawPixel(int x, int y, TerminalColor bgColor) {
		this.writeToBuffer(x, y, ' ', bgColor, bgColor);
	}

	public void drawString(int x, int y, String string, TerminalColor fgColor, TerminalColor bgColor) {
		for (int i = 0; i < string.length(); i++) {
			this.writeToBuffer(x + i, y, string.charAt(i), fgColor, bgColor);
		}
	}

	public void drawLine(int x, int y, int x2, int y2, char character, TerminalColor fgColor, TerminalColor bgColor) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	public void drawBlankLine(int x, int y, int x2, int y2, TerminalColor bgColor) {
		this.drawLine(x, y, x2, y2, ' ', bgColor, bgColor);
	}

	public void drawArea(int x, int y, int width, int height, char character, TerminalColor fgColor, TerminalColor bgColor) {
		for (int ix = x; ix < x + width; ix++) {
			for (int iy = y; iy < y + height; iy++) {
				this.writeToBuffer(ix, iy, character, fgColor, bgColor);
			}
		}
	}

	public void drawBlankArea(int x, int y, int width, int height, TerminalColor bgColor) {
		this.drawArea(x, y, width, height, ' ', bgColor, bgColor);
	}

	public int width() { return this.width; }
	public int height() { return this.height; }
	public char characterAt(int x, int y) { return buffer[x][y].character; }
	public TerminalColor fgColorAt(int x, int y) { return buffer[x][y].fgColor; }
	public TerminalColor bgColorAt(int x, int y) { return buffer[x][y].bgColor; }
}