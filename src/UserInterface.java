public class UserInterface extends GameObject {
	//private TetrominoBlock[][] grid;
	public int width =10;
	public int height =20;
	public int time = 0;
	public float speed = 0.0005f;
	public GameField gameField;

	public UserInterface (GameField gameField){
	this.gameField=gameField;
	}


	@Override
	public void draw(TerminalCanvas canvas) {
		time += Main.TARGET_FRAME_TIME;
		//canvas.drawArea(9, 10, 1, 20, 'L', TerminalColor.TRANSPARENT, TerminalColor.BLUE);
		for (int i = 0; i < gameField.height(); i++) {
			canvas.drawPixel(gameField.getAbsoluteLocationX() - 1, gameField.getAbsoluteLocationY() + i, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + i) * 255), (int)(Math.sin(time * speed + 50 + i) * 200) ));
			canvas.drawPixel(gameField.getAbsoluteLocationX() + gameField.width(), gameField.getAbsoluteLocationY() + i, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + i) * 255), (int)(Math.sin(time * speed + 50 + i) * 200) ));
		}
		for (int j = -1; j <= gameField.width(); j++) {
			canvas.drawPixel(gameField.getAbsoluteLocationX() + j, gameField.getAbsoluteLocationY() - 1, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + j) * 255), (int)(Math.sin(time * speed + 50 + j) *200) ));
			canvas.drawPixel(gameField.getAbsoluteLocationX() + j, gameField.getAbsoluteLocationY() +gameField.height(), new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + j ) * 255), (int)(Math.sin(time * speed + 50 + j) * 200) ));
		}
		
}
}	



