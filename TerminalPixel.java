public class TerminalPixel {
	public char character;
	public TerminalColor fgColor;
	public TerminalColor bgColor;

	public TerminalPixel(char character, TerminalColor fgColor, TerminalColor bgColor) {
		this.character = character;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}
}