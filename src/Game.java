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
	public Timer transmitStatsTimer;

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

	public Game(String username, NetworkManager netManager) throws IOException {
		this.baseFallInterval = 250;
		this.nextTetrominos = new Tetromino[3];
		this.gameField = new GameField(10, 20);
		this.fallTimer = new Timer(this.baseFallInterval);
		this.transmitStatsTimer = new Timer(250);
		this.localStats = new PlayerStats();
		this.localStats.username = username;
		this.remoteStats = new PlayerStats();
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

		canvas.drawString(7, 15, this.localStats.username + ":", TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		canvas.drawString(7, 16, "- Score: " + this.localStats.score, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		canvas.drawString(7, 17, "- Level: " + this.localStats.level, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		canvas.drawString(7, 18, "- Rows : " + this.localStats.rowsRemoved, TerminalColor.WHITE, TerminalColor.TRANSPARENT);

		if (netManager.active()) {
			canvas.drawString(7, 20, this.remoteStats.username + ":", TerminalColor.WHITE, TerminalColor.TRANSPARENT);
			canvas.drawString(7, 21, "- Score: " + this.remoteStats.score, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
			canvas.drawString(7, 22, "- Level: " + this.remoteStats.level, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
			canvas.drawString(7, 23, "- Rows : " + this.remoteStats.rowsRemoved, TerminalColor.WHITE, TerminalColor.TRANSPARENT);
			canvas.drawString(7, 25, "Num unconfirmed messages: " + this.netManager.numUnconfirmedMessages(), TerminalColor.WHITE, TerminalColor.TRANSPARENT);
		}
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

		if (this.transmitStatsTimer.shouldExecute) {
			NetworkMessage usernameMsg = new NetworkMessage(NetworkMessage.USERNAME, this.localStats.username);
			NetworkMessage scoreMsg = new NetworkMessage(NetworkMessage.STAT_SCORE, this.localStats.score);
			NetworkMessage levelMsg = new NetworkMessage(NetworkMessage.STAT_LEVEL, this.localStats.level);
			NetworkMessage rowsMsg = new NetworkMessage(NetworkMessage.STAT_ROWS_REMOVED, this.localStats.rowsRemoved);
			this.netManager.sendUnreliable(usernameMsg);
			this.netManager.sendUnreliable(scoreMsg);
			this.netManager.sendUnreliable(levelMsg);
			this.netManager.sendUnreliable(rowsMsg);
		}

		this.activeTetrominoGhost.setRotation(this.activeTetromino.rotation);
		this.activeTetrominoGhost.relativeLocationX = this.activeTetromino.relativeLocationX;
		this.activeTetrominoGhost.relativeLocationY = this.activeTetromino.relativeLocationY;

		while (this.gameField.canTetrominoBePlaced(this.activeTetrominoGhost.relativeLocationX, this.activeTetrominoGhost.relativeLocationY + 1, activeTetrominoGhost)) {
			this.activeTetrominoGhost.relativeLocationY++;
		}

		int fullRowsRemoved = this.gameField.removeFullRows();
		int blockingRowsRemoved = this.gameField.removeBlockingRows();
		int sumRowsRemoved = fullRowsRemoved + blockingRowsRemoved;
		this.localStats.score += sumRowsRemoved * 100;
		this.localStats.rowsRemoved += sumRowsRemoved;

		if (fullRowsRemoved > 1) {
			NetworkMessage addRowsMsg = new NetworkMessage(NetworkMessage.ADD_BLOCKING_ROWS, fullRowsRemoved / 2);
			this.netManager.sendReliable(addRowsMsg);
		}

		if (sumRowsRemoved > 2) {
			SoundPlayer.playOnce("./res/rowclear.wav");
			SoundPlayer.playOnce("./res/rowclearbig.wav");
		} else if (sumRowsRemoved > 0) {
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
							NetworkMessage endMsg = new NetworkMessage(NetworkMessage.GAME_END);
							this.netManager.sendReliable(endMsg);
							
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
				NetworkMessage endMsg = new NetworkMessage(NetworkMessage.GAME_END);
				this.netManager.sendReliable(endMsg);
				
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
