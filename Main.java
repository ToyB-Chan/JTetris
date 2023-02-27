import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
	static final int TARGET_FRAME_TIME = 16;
	
	// Essential 
	static TerminalCanvas canvas;
	static TerminalInputHook input;
	static GameField gameField;
	static UserInterface userInterface;

	// Timers
	static Timer fallTimer; 

	// Tetrominos
	static Tetromino activeTetromino;
	static Tetromino activeTetrominoGhost;

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		canvas = new TerminalCanvas(71, 39, TerminalColor.BLACK);
		input = new TerminalInputHook();
		input.startHook(System.in);
		gameField = new GameField(10, 20);
		gameField.relativeLocationX = 10;
		gameField.relativeLocationY = 10;
		UserInterface userInterface = new UserInterface();
		Timer fallTimer = new Timer(250);

		setActiveTetromino(Tetromino.newRandomTetromino(0));
		
		while (true) {

			// Input start
			input.update();
			if (input.isKeyPressed('w') || input.isKeyPressed('W')) {
				activeTetromino.setRotation((activeTetromino.getRotation() + 1) % 4);
				
				if (!gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY, activeTetromino)) {
					for (int i = 0; i < 4; i++) {
						if (gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX + i, activeTetromino.relativeLocationY, activeTetromino)) {
							activeTetromino.relativeLocationX += i;
							break;
						}

						if (gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX - i, activeTetromino.relativeLocationY, activeTetromino)) {
							activeTetromino.relativeLocationX -= i;
							break;
						}

						if (gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY - i, activeTetromino)) {
							activeTetromino.relativeLocationY -= i;
							break;
						}

						// if we are at the end of the loop we were not able to find a valid place, thus we rotate it back.
						if (i + 1  >= 4) {
							activeTetromino.setRotation((activeTetromino.getRotation() - 1) % 4);
						}
					}
				}
			}

			if ((input.isKeyPressed('a') || input.isKeyPressed('A')) && gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX - 1, activeTetromino.relativeLocationY, activeTetromino)) {
				activeTetromino.relativeLocationX--;
			}

			if ((input.isKeyPressed('d') || input.isKeyPressed('D')) && gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX + 1, activeTetromino.relativeLocationY, activeTetromino)) {
				activeTetromino.relativeLocationX++;
			}

			if ((input.isKeyPressed('s') || input.isKeyPressed('S')) && gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY + 1, activeTetromino)) {
				activeTetromino.relativeLocationY++;
			}

			if (input.isKeyPressed(' ')) {
				while (gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY + 1, activeTetromino)) {
					activeTetromino.relativeLocationY++;
				}
			}

			for (int i = 0; i <= 6; i++) {
				if (i < 10 && input.isKeyPressed(String.valueOf(i).charAt(0))) {
					// Set tetromino
				}
			}
			// Input end

			// Update ghost start
			activeTetrominoGhost.setRotation(activeTetromino.rotation);
			activeTetrominoGhost.relativeLocationX = activeTetromino.relativeLocationX;
			activeTetrominoGhost.relativeLocationY = activeTetromino.relativeLocationY;
			while(gameField.canTetrominoBePlaced(activeTetrominoGhost.relativeLocationX, activeTetrominoGhost.relativeLocationY + 1, activeTetrominoGhost)) {
				activeTetrominoGhost.relativeLocationY++;
			}
			// Update ghost end 

			gameField.removeFullRows();

			// Draw start
			String canvasText = "=== J Tetris ===";
			canvas.drawString((canvas.width() / 2) - (canvasText.length() / 2), 0, canvasText, TerminalColor.GREEN, TerminalColor.BLACK);
			String keyText = "Keys pressed: ";
			canvas.drawString(0, 1, keyText + input.getPressedKeys().toString() + " ", TerminalColor.WHITE, TerminalColor.BLACK);
			userInterface.draw(canvas);
			gameField.draw(canvas);
			activeTetrominoGhost.draw(canvas);
			activeTetromino.draw(canvas);
			canvas.renderBuffer(System.out);
			// Draw end

			// Timer start
			if (fallTimer.shouldExecute) {
				if (gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY + 1, activeTetromino)) {
					activeTetromino.relativeLocationY++;
				} else {
					if (activeTetromino.relativeLocationY == 1) {
						System.out.println("Game end");
						break;
					}

					if (gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY, activeTetromino)) {
						gameField.addTetromino(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY, activeTetromino);
						setActiveTetromino(Tetromino.newRandomTetromino(0));
					}
				}
			}

			fallTimer.update(TARGET_FRAME_TIME);
			// Timer end

			Thread.sleep(TARGET_FRAME_TIME);
		}
	}

	public static void setActiveTetromino(Tetromino tetromino) {
		activeTetromino = tetromino;
		activeTetromino.parent = gameField;
		activeTetromino.relativeLocationX = gameField.width() / 2;
		activeTetromino.relativeLocationY = 1;
		activeTetrominoGhost = activeTetromino.copy();
		for (int i = 0; i < activeTetrominoGhost.blocks.length; i++) {
			activeTetrominoGhost.blocks[i].renderAsGhost = true;
		}
	}
}
