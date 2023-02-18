import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {
	static final int TARGET_FRAME_TIME = 16;
	static String pressedKeys = "";

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(71, 39, TerminalColor.BLACK);
		TerminalInputHook input = new TerminalInputHook();
		GameField gameField = new GameField(10, 20);
		gameField.relativeLocationX = 10;
		gameField.relativeLocationY = 10;
		Tetromino tetromino = Tetromino.newRandomTetromino(0);
		tetromino.parent = gameField;
		tetromino.relativeLocationX = 5;
		tetromino.relativeLocationY = 1;
		Random rnd = new Random();
		input.startHook(System.in);
		Timer fallTimer = new Timer(250);
		
		while (true) {
			input.update();
			if (input.isKeyPressed('w')) {
				tetromino.setRotation((tetromino.getRotation() + 1) % 4);
				
				if (!gameField.canTetrominoBePlaced(tetromino.relativeLocationX, tetromino.relativeLocationY, tetromino)) {
					for (int i = 0; i < 4; i++) {
						if (gameField.canTetrominoBePlaced(tetromino.relativeLocationX + i, tetromino.relativeLocationY, tetromino)) {
							tetromino.relativeLocationX += i;
							break;
						}

						if (gameField.canTetrominoBePlaced(tetromino.relativeLocationX - i, tetromino.relativeLocationY, tetromino)) {
							tetromino.relativeLocationX -= i;
							break;
						}

						if (gameField.canTetrominoBePlaced(tetromino.relativeLocationX, tetromino.relativeLocationY - i, tetromino)) {
							tetromino.relativeLocationY -= i;
							break;
						}
					}
					
				}
			}

			if (input.isKeyPressed('a') && gameField.canTetrominoBePlaced(tetromino.relativeLocationX - 1, tetromino.relativeLocationY, tetromino)) {
				tetromino.relativeLocationX--;
			}

			if (input.isKeyPressed('d') && gameField.canTetrominoBePlaced(tetromino.relativeLocationX + 1, tetromino.relativeLocationY, tetromino)) {
				tetromino.relativeLocationX++;
			}

			if (input.isKeyPressed('s') && gameField.canTetrominoBePlaced(tetromino.relativeLocationX, tetromino.relativeLocationY + 1, tetromino)) {
				tetromino.relativeLocationY++;
			}

			if (input.isKeyPressed(' ')) {
				while (gameField.canTetrominoBePlaced(tetromino.relativeLocationX, tetromino.relativeLocationY + 1, tetromino)) {
					tetromino.relativeLocationY++;
				}
			}

			String canvasText = "=== JTetris ===";
			canvas.drawString((canvas.width() / 2) - (canvasText.length() / 2), 0, canvasText, TerminalColor.GREEN, TerminalColor.BLACK);
			String keyText = "Keys pressed: ";
			canvas.drawString(0, 1, keyText + pressedKeys + " ", TerminalColor.WHITE, TerminalColor.BLACK);
			pressedKeys = "";
			gameField.draw(canvas);
			tetromino.draw(canvas);

			if (fallTimer.shouldExecute) {
				if (gameField.canTetrominoBePlaced(tetromino.relativeLocationX, tetromino.relativeLocationY + 1, tetromino)) {
					tetromino.relativeLocationY++;
				} else {
					if (tetromino.relativeLocationY == 1) {
						System.out.println("Game end");
						break;
					}

					if (gameField.canTetrominoBePlaced(tetromino.relativeLocationX, tetromino.relativeLocationY, tetromino))
					{
						gameField.addTetromino(tetromino.relativeLocationX, tetromino.relativeLocationY, tetromino);
						tetromino = Tetromino.newRandomTetromino(rnd.nextInt(4));
						tetromino.parent = gameField;
						tetromino.relativeLocationX = 5;
						tetromino.relativeLocationY = 1;
					}
				}
			}

			canvas.renderBuffer(System.out);
			fallTimer.update(TARGET_FRAME_TIME);
			Thread.sleep(TARGET_FRAME_TIME);
		}
	}
}
