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
	public boolean gameEnded;
	public int numRowsCleared;


	public Game() {
		this.nextTetrominos = new Tetromino[3];
		this.gameField = new GameField(10, 20);
		this.fallTimer = new Timer(0);
		this.userInterface = new UserInterface();
		this.level = 1;
		
		this.setActiveTetromino(Tetromino.newRandomTetromino(0));
		this.swapTetromino = Tetromino.newRandomTetromino(0);
		for (int i = 0; i < this.nextTetrominos.length; i++) {
			this.nextTetrominos[i] = Tetromino.newRandomTetromino(0);
		}
	}

	public void draw(TerminalCanvas canvas) {
		this.gameField.relativeLocationX = canvas.width() / 2 - gameField.width() / 2;
		this.gameField.relativeLocationY = canvas.height() / 2 - gameField.height() / 2;

		//this.userInterface.draw(canvas);
		this.gameField.draw(canvas);
		this.activeTetrominoGhost.draw(canvas);
		this.activeTetromino.draw(canvas);

		this.swapTetromino.parent = this.gameField;
		this.swapTetromino.setRotation(0);
		this.swapTetromino.relativeLocationX = -4;
		this.swapTetromino.relativeLocationY = 5;
		this.swapTetromino.draw(canvas);

		for (int i = 0; i < this.nextTetrominos.length; i++) {
			this.nextTetrominos[i].parent = this.gameField;
			this.nextTetrominos[i].relativeLocationX = this.gameField.width() + 2;
			this.nextTetrominos[i].relativeLocationY = i * 5 + 5;
			this.nextTetrominos[i].draw(canvas);
		}

		canvas.drawString(1, 1, "Score: " + this.score, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		canvas.drawString(1, 2, "Level: " + this.level, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		canvas.drawString(1, 3, "Rows Clear: " + this.numRowsCleared, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		canvas.drawString(1, 4, "Speed: " + 1.f / (this.fallTimer.interval / 1000.f), TerminalColor.WHITE, TerminalColor.TRANSPARENT);
	}

	public void tick() {
		if (this.gameEnded) {
			return;
		}

		



		if (this.fallTimer.shouldExecute) {
			if (this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY + 1, this.activeTetromino)) {
				this.activeTetromino.relativeLocationY++;
			} else {
				if (this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
					this.gameField.addTetromino(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY, this.activeTetromino);
					this.tetrominoSwapped = false;
					this.setActiveTetromino(this.popNexTetromino());
					SoundPlayer.playOnce("./res/drop.wav");
				}
			}
		}

		this.activeTetrominoGhost.setRotation(this.activeTetromino.rotation);
		this.activeTetrominoGhost.relativeLocationX = this.activeTetromino.relativeLocationX;
		this.activeTetrominoGhost.relativeLocationY = this.activeTetromino.relativeLocationY;

		while (this.gameField.canTetrominoBePlaced(this.activeTetrominoGhost.relativeLocationX, this.activeTetrominoGhost.relativeLocationY + 1, activeTetrominoGhost)) {
			this.activeTetrominoGhost.relativeLocationY++;
		}

		int rowsRemoved = this.gameField.removeFullRows();
		this.score += rowsRemoved * 100;
		this.numRowsCleared += rowsRemoved;

		if (rowsRemoved > 2) {
			SoundPlayer.playOnce("./res/rowclear.wav");
			SoundPlayer.playOnce("./res/rowclearbig.wav");
		} else if (rowsRemoved > 0) {
			SoundPlayer.playOnce("./res/rowclear.wav");

		}
		
		if(this.numRowsCleared >= (this.level * 10) || this.numRowsCleared >= 100 ){
			this.level++;
			this.fallTimer.interval = 250 - (this.level * 4);
		}
	}

	public void inputTick(TerminalInputHook input) {
		if (this.gameEnded) {
			return;
		}

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
						return;
					}
				}
			}

			SoundPlayer.playOnce("./res/rotate.wav");
		}

		if ((input.isKeyPressed('a') || input.isKeyPressed('A')) && this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX - 1, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
			this.activeTetromino.relativeLocationX--;
			SoundPlayer.playOnce("./res/move.wav");
		}

		if ((input.isKeyPressed('d') || input.isKeyPressed('D')) && this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX + 1, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
			this.activeTetromino.relativeLocationX++;
			SoundPlayer.playOnce("./res/move.wav");
		}

		if ((input.isKeyPressed('s') || input.isKeyPressed('S')) && this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY + 1, this.activeTetromino)) {
			this.activeTetromino.relativeLocationY++;
			this.score += 1;
			SoundPlayer.playOnce("./res/move.wav");
		}

		if (input.isKeyPressed(' ')) {
			while (this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY + 1, this.activeTetromino)) {
				this.activeTetromino.relativeLocationY++;
				this.score += 1;
			}

			this.gameField.addTetromino(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY, this.activeTetromino);
			this.tetrominoSwapped = false;
			this.setActiveTetromino(this.popNexTetromino());
			SoundPlayer.playOnce("./res/drop.wav");
		}

		if (input.isKeyPressed('c') && !this.tetrominoSwapped) {
			this.tetrominoSwapped = true;
			Tetromino temp = this.swapTetromino;
			this.swapTetromino = this.activeTetromino;
			this.setActiveTetromino(temp);
			SoundPlayer.playOnce("./res/swap.wav");
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
				this.activeTetromino.relativeLocationX =- 10000; // hide it
				this.gameEnded = true;
				return;
			}
		}

		this.activeTetrominoGhost = this.activeTetromino.copy();
		for (int i = 0; i < this.activeTetrominoGhost.blocks.length; i++) {
			activeTetrominoGhost.blocks[i].renderAsGhost = true;
		}
	}

	public Tetromino popNexTetromino() {
		Tetromino out = this.nextTetrominos[0];
		for (int i = 1; i < this.nextTetrominos.length; i++) {
			this.nextTetrominos[i - 1] = this.nextTetrominos[i];
		}

		this.nextTetrominos[this.nextTetrominos.length - 1] = Tetromino.newRandomTetromino(0);
		return out;
	}
}
