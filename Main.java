import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {
	static String pressedKeys = "";

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(71, 39, TerminalColor.black);
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
		
		while (true) {
			input.update();
			if (input.isKeyPressed('w')) {
				tetromino.setRotation((tetromino.getRotation() + 1) % 4);
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
			canvas.drawString((canvas.width() / 2) - (canvasText.length() / 2), 0, canvasText, TerminalColor.green, TerminalColor.black);
			String keyText = "Keys pressed: ";
			canvas.drawString(0, 1, keyText + pressedKeys + " ", TerminalColor.white, TerminalColor.black);
			pressedKeys = "";
			gameField.draw(canvas);
			tetromino.draw(canvas);

			if (gameField.canTetrominoBePlaced(tetromino.relativeLocationX, tetromino.relativeLocationY + 1, tetromino)) {
				tetromino.relativeLocationY++;
			} else {
				if (tetromino.relativeLocationY == 1) {
					System.out.println("Game end");
					break;
				}

				gameField.addTetromino(tetromino.relativeLocationX, tetromino.relativeLocationY, tetromino);
				tetromino = Tetromino.newRandomTetromino(rnd.nextInt(4));
				tetromino.parent = gameField;
				tetromino.relativeLocationX = 5;
				tetromino.relativeLocationY = 1;
			}

			canvas.renderBuffer(System.out);
			Thread.sleep(200);
		}
	}
}
