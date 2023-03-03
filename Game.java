public class Game {

	// Tetrominos
	public Tetromino activeTetromino;
	public Tetromino activeTetrominoGhost;
	public Tetromino swapTetromino;
	public Tetromino[] nextTetrominos;

	// Game field
	public GameField gameField;

	// Timers
	public Timer fallTimer;

	// User interface
	public UserInterface userInterface;

	// Scores
	public int score;
	public int level;

	// States
	public boolean tetrominoSwapped;
	public boolean forcePlaced;
	public boolean gameEnded;

	public Game() {
		this.nextTetrominos = new Tetromino[3];
		this.gameField = new GameField(10, 20);
		this.fallTimer = new Timer(250);
		this.userInterface = new UserInterface();
	}

	public void draw(TerminalCanvas canvas) {
		this.gameField.relativeLocationX = canvas.width() / 2 - gameField.width() / 2;
		this.gameField.relativeLocationY = canvas.height() / 2 - gameField.height() / 2;

		this.userInterface.draw(canvas);
		this.gameField.draw(canvas);
		this.activeTetromino.draw(canvas);
		this.activeTetrominoGhost.draw(canvas);
	}

	public void tick() {
		this.fallTimer.interval = 250;

		if (this.fallTimer.shouldExecute || this.forcePlaced) {
			if (gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY + 1, activeTetromino)) {
				activeTetromino.relativeLocationY++;
			} else {
				if (gameField.canTetrominoBePlaced(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY, activeTetromino)) {
					gameField.addTetromino(activeTetromino.relativeLocationX, activeTetromino.relativeLocationY, activeTetromino);
				}
			}
		}
	}

	public void inputTick(TerminalInputHook input) {
		if (input.isKeyPressed('w') || input.isKeyPressed('W')) {
			this.activeTetromino.setRotation((this.activeTetromino.getRotation() + 1) % 4);
			
			if (!this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
				for (int i = 0; i < 4; i++) {
					if (this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX + i, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
						this.activeTetromino.relativeLocationX += i;
						break;
					}

					if (this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX - i, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
						this.activeTetromino.relativeLocationX -= i;
						break;
					}

					if (this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY - i, this.activeTetromino)) {
						this.activeTetromino.relativeLocationY -= i;
						break;
					}

					// if we are at the end of the loop we were not able to find a valid place, thus we rotate it back.
					if (i + 1  >= 4) {
						this.activeTetromino.setRotation((this.activeTetromino.getRotation() - 1) % 4);
					}
				}
			}
		}

		if ((input.isKeyPressed('a') || input.isKeyPressed('A')) && this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX - 1, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
			this.activeTetromino.relativeLocationX--;
		}

		if ((input.isKeyPressed('d') || input.isKeyPressed('D')) && this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX + 1, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
			this.activeTetromino.relativeLocationX++;
		}

		if ((input.isKeyPressed('s') || input.isKeyPressed('S')) && this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY + 1, this.activeTetromino)) {
			this.activeTetromino.relativeLocationY++;
		}

		if (input.isKeyPressed(' ')) {
			while (this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY + 1, this.activeTetromino)) {
				this.activeTetromino.relativeLocationY++;
			}

			this.forcePlaced = true;
		}

		if (input.isKeyPressed('c') && !this.tetrominoSwapped) {
			this.tetrominoSwapped = true;
		}
	}

	public void setActiveTetromino(Tetromino tetromino) {
		this.activeTetromino = tetromino;
		this.activeTetromino.parent = this.gameField;
		this.activeTetromino.relativeLocationX = this.gameField.width() / 2;
		this.activeTetromino.relativeLocationY = 0;

		int retries = 0;
		while (!this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
			this.activeTetromino.relativeLocationY++;
			retries++;

			if (retries > 5) {
				this.gameEnded = true;
				return;
			}
		}

		this.activeTetrominoGhost = this.activeTetromino.copy();
		for (int i = 0; i < this.activeTetrominoGhost.blocks.length; i++) {
			activeTetrominoGhost.blocks[i].renderAsGhost = true;
		}
	}
}
