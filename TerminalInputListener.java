public class TerminalInputListener {
	public Character key;

	public TerminalInputListener() { 
		this.key = Character.MIN_VALUE; 
	}
	
	public TerminalInputListener(Character key) { 
		this.key = key; 
	}

	public void keyPressed(Character key) { }
}