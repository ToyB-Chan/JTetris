public class Mainmenu {
	public int selectedIndex;
	public String[] selections;
	public boolean startGame;
	public SoundPlayer bgMusic;
	


	public Mainmenu(SoundPlayer bgMusic){
		this.bgMusic = bgMusic;
		this.selectedIndex = 0;
		this.selections = new String[]{"PLAY", "SOUND",};
	}

	public void draw(TerminalCanvas canvas){
		canvas.drawArea(0, 0, canvas.width(), canvas.height(), ' ', TerminalColor.TRANSPARENT, TerminalColor.BLACK);

		for (int i = 0; i < selections.length; i++) {
			if (i == selectedIndex) {
				canvas.drawString(canvas.width()/2, canvas.height()/2 + i * 2, selections[i], TerminalColor.BLACK, TerminalColor.WHITE);
			} else {
				canvas.drawString(canvas.width()/2, canvas.height()/2 + i * 2, selections[i], TerminalColor.WHITE, TerminalColor.BLACK);
			}
		}
	}

	public void inputTick(TerminalInputHook input) {
		if((input.isKeyPressed('W') || input.isKeyPressed('w'))&& this.selectedIndex > 0 ){
			this.selectedIndex--;
		}

		if((input.isKeyPressed('S') || input.isKeyPressed('s'))&& this.selectedIndex < this.selections.length-1) {
			this.selectedIndex++;
		}
		
		if(input.isKeyPressed(' ') && selectedIndex == 0){
			this.startGame = true;
		}

		if((input.isKeyPressed('A') || input.isKeyPressed('a')) && this.selectedIndex == 1){
			this.bgMusic.setVolume(0.5f);
		}

		if((input.isKeyPressed('D') || input.isKeyPressed('d')) && this.selectedIndex == 1){
			this.bgMusic.setVolume(0.7f);
		}

	}

	public void tick(){
		

	}
}


