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
		
		for (int i = 0; i < gameField.height(); i++) {
			canvas.drawPixel(gameField.getAbsoluteLocationX() - 1, gameField.getAbsoluteLocationY() + i, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + i) * 255), (int)(Math.sin(time * speed + 50 + i) * 200) ));
			canvas.drawPixel(gameField.getAbsoluteLocationX() + gameField.width(), gameField.getAbsoluteLocationY() + i, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + i) * 255), (int)(Math.sin(time * speed + 50 + i) * 200) ));
		}
		//gamefield frame y
		for (int j = -1; j <= gameField.width(); j++) {
			canvas.drawPixel(gameField.getAbsoluteLocationX() + j, gameField.getAbsoluteLocationY() - 1, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + j) * 255), (int)(Math.sin(time * speed + 50 + j) *200) ));
			canvas.drawPixel(gameField.getAbsoluteLocationX() + j, gameField.getAbsoluteLocationY() +gameField.height(), new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + j ) * 255), (int)(Math.sin(time * speed + 50 + j) * 200) ));
		}
		//gamefield frame x
		for (int k = 2; k < 9; k++) {
			canvas.drawPixel(gameField.getAbsoluteLocationX() -9, gameField.getAbsoluteLocationY() + k, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + k) * 255), (int)(Math.sin(time * speed + 50 + k) * 200) ));
			canvas.drawPixel(gameField.getAbsoluteLocationX()  -3, gameField.getAbsoluteLocationY() + k, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + k) * 255), (int)(Math.sin(time * speed + 50 + k) * 200) ));
		}
		//swap y frame
		for (int l = -9; l < -2; l++) {
			canvas.drawPixel(gameField.getAbsoluteLocationX() + l, gameField.getAbsoluteLocationY() +2, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + l) * 255), (int)(Math.sin(time * speed + 50 + l) *200) ));
			canvas.drawPixel(gameField.getAbsoluteLocationX() + l, gameField.getAbsoluteLocationY() +8, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + l ) * 255), (int)(Math.sin(time * speed + 50 + l) * 200) ));
		}
		//swap x frame
		for (int m = 2; m < 18; m++) {
			canvas.drawPixel(gameField.getAbsoluteLocationX() + gameField.width() + 2, gameField.getAbsoluteLocationY() + m, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + m) * 255), (int)(Math.sin(time * speed + 50 + m) * 200) ));
			canvas.drawPixel(gameField.getAbsoluteLocationX() + gameField.width() + 8, gameField.getAbsoluteLocationY() + m, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + m) * 255), (int)(Math.sin(time * speed + 50 + m) * 200) ));
		}
		
		for (int n = gameField.width()+2; n < gameField.width()+9; n++) {
			canvas.drawPixel(gameField.getAbsoluteLocationX() + n, gameField.getAbsoluteLocationY() +2, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + n) * 255), (int)(Math.sin(time * speed + 50 + n) *200) ));
			canvas.drawPixel(gameField.getAbsoluteLocationX() + n, gameField.getAbsoluteLocationY() +18, new TerminalColor((int)(Math.sin(time * speed) * 255), (int)(Math.sin(time * speed + 10 + n ) * 255), (int)(Math.sin(time * speed + 50 + n) * 200) ));
		}

}
}	



