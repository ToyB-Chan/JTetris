import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {
	static String pressedKeys = "";

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(71, 39, TerminalColor.Black);
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
		input.addListener(new TerminalInputListener() {
			@Override
			public void keyPressed(Character key) {
				pressedKeys += key;

				if (key == 'w') {
					//tetromino.setRotation((tetromino.getRotation() + 1) % 4);
				}
			}
		});

		

		
		while (true) {
			/* 
			for (int ix = 0; ix < canvas.width(); ix += 3) {
				for (int iy = 0; iy < canvas.height(); iy++) {
					TerminalColor color = TerminalColor.randomColor();
					canvas.drawString(ix, iy, "###", color, color.negative());
				}
			}
			*/


			input.update();

			String canvasText = "=== JTetris ===";
			canvas.drawString((canvas.width() / 2) - (canvasText.length() / 2), 0, canvasText, TerminalColor.randomColor(), TerminalColor.Black);

			String keyText = "Keys pressed: ";
			canvas.drawString(0, 1, keyText + pressedKeys + " ", TerminalColor.White, TerminalColor.Black);
			pressedKeys = "";

			gameField.draw(canvas);

			if (!tetromino.isGarabge()) {
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
			}


			canvas.renderBuffer(System.out);
			Thread.sleep(100);
		}
	}
}
