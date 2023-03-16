import java.io.IOException;

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

	// Player stats
	public PlayerStats localStats;
	public PlayerStats remoteStats;

	// User interface
	public UserInterface userInterface;

	// Network manager
	NetworkManager netManager;

	// States
	public boolean tetrominoSwapped;
	public boolean gameEnded;
	public int baseFallInterval;



	public Game(NetworkManager netManager) throws IOException {
		this.baseFallInterval = 250;
		this.nextTetrominos = new Tetromino[3];
		this.gameField = new GameField(10, 20);
		this.fallTimer = new Timer(this.baseFallInterval);
		this.localStats = new PlayerStats("Owner");
		this.remoteStats = new PlayerStats("Other");
		this.userInterface = new UserInterface(this.gameField);
		this.userInterface.parent = this.gameField;
		this.netManager = netManager;
		
		this.setActiveTetromino(Tetromino.newRandomTetromino(0));
		this.swapTetromino = Tetromino.newRandomTetromino(0);
		for (int i = 0; i < this.nextTetrominos.length; i++) {
			this.nextTetrominos[i] = Tetromino.newRandomTetromino(0);
		}
	}

	public void draw(TerminalCanvas canvas) {
		this.gameField.relativeLocationX = canvas.width() / 2 - gameField.width() / 2;
		this.gameField.relativeLocationY = canvas.height() / 2 - gameField.height() / 2;

		this.userInterface.draw(canvas);
		this.gameField.draw(canvas);
		this.activeTetrominoGhost.draw(canvas);
		this.activeTetromino.draw(canvas);

		this.swapTetromino.parent = this.gameField;
		this.swapTetromino.setRotation(0);
		this.swapTetromino.relativeLocationX = -6;
		this.swapTetromino.relativeLocationY = 5;
		this.swapTetromino.draw(canvas);

		for (int i = 0; i < this.nextTetrominos.length; i++) {
			this.nextTetrominos[i].parent = this.gameField;
			this.nextTetrominos[i].relativeLocationX = this.gameField.width() + 5;
			this.nextTetrominos[i].relativeLocationY = i * 5 + 5;
			this.nextTetrominos[i].draw(canvas);
		}
		
		canvas.drawString(gameField.getAbsoluteLocationX()+2, 1, "JTETRIS" , TerminalColor.WHITE, TerminalColor.TRANSPARENT);

		canvas.drawString(9, 20, "Score: " + this.localStats.score, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		canvas.drawString(9, 21, "Level: " + this.localStats.level, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		canvas.drawString(9, 22, "Rows : " + this.localStats.rowsRemoved, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		//canvas.drawString(1, 18, "Speed: " + 1.f / (this.fallTimer.interval / 1000.f), TerminalColor.WHITE, TerminalColor.TRANSPARENT);
	}

	public void tick() throws IOException {
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
		this.localStats.score += rowsRemoved * 100;
		this.localStats.rowsRemoved += rowsRemoved;
		
		{
			NetworkMessage msg = new NetworkMessage(NetworkMessage.ADD_BLOCKING_ROWS, rowsRemoved);
			this.netManager.send(msg);
		}

		if (rowsRemoved > 2) {
			SoundPlayer.playOnce("./res/rowclear.wav");
			SoundPlayer.playOnce("./res/rowclearbig.wav");
		} else if (rowsRemoved > 0) {
			SoundPlayer.playOnce("./res/rowclear.wav");

		}

		if(this.localStats.rowsRemoved >= (this.localStats.level * 10) || this.localStats.rowsRemoved >= 100 ){
			this.localStats.level++;
			this.fallTimer.interval = this.baseFallInterval - (this.localStats.level * 4);
		}

		this.netManager.update();

		while (this.netManager.available() > 0) {
			NetworkMessage msg = this.netManager.nextMessage();

			switch (msg.type()) {
				case NetworkMessage.USERNAME:
					this.remoteStats.username = msg.contentString();
					break;
				case NetworkMessage.GAME_END:
					this.gameEnded = true;
					break;
				case NetworkMessage.STAT_SCORE:
					this.remoteStats.score = msg.contentInt();
					break;
				case NetworkMessage.STAT_LEVEL:
					this.remoteStats.level = msg.contentInt();
					break;
				case NetworkMessage.STAT_ROWS_REMOVED:
					this.remoteStats.rowsRemoved = msg.contentInt();
					break;
				case NetworkMessage.ADD_BLOCKING_ROWS:
					for (int i = 0; i < msg.contentInt(); i++) {
						gameField.addBlockingRow();
					}

					// Move current tetromino up if we hit it while adding rows
					int retries = 0;
					while(!this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY, activeTetromino)) {
						this.activeTetromino.relativeLocationY--;
						retries++;

						if (retries > 10) {
							this.activeTetromino.relativeLocationX =- 10000; // hide it
							this.gameEnded = true;
							NetworkMessage endmsg = new NetworkMessage(NetworkMessage.GAME_END);
							this.netManager.send(endmsg);
							
							return;
						}
					}

					break;
				default:
					break;
			}
		}
	}

	public void inputTick(TerminalInputHook input) throws IOException {
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
			this.localStats.score += 1;
			SoundPlayer.playOnce("./res/move.wav");
		}

		if (input.isKeyPressed(' ')) {
			while (this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY + 1, this.activeTetromino)) {
				this.activeTetromino.relativeLocationY++;
				this.localStats.score += 1;
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

	public void setActiveTetromino(Tetromino tetromino) throws IOException {
		this.activeTetromino = tetromino;
		this.activeTetromino.parent = this.gameField;
		this.activeTetromino.relativeLocationX = this.gameField.width() / 2;
		this.activeTetromino.relativeLocationY = 0;

		int retries = 0;
		while (!this.gameField.canTetrominoBePlaced(this.activeTetromino.relativeLocationX, this.activeTetromino.relativeLocationY, this.activeTetromino)) {
			this.activeTetromino.relativeLocationY++;
			retries++;

			if (retries > 10) {
				this.activeTetromino.relativeLocationX =- 10000; // hide it
				this.gameEnded = true;
				NetworkMessage msg = new NetworkMessage(NetworkMessage.GAME_END);
				this.netManager.send(msg);
				
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
