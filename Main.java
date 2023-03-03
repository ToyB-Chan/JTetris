import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
	static final int TARGET_FRAME_TIME = 16;
	
	// Essential 



	// Timers
	static Timer fallTimer; 

	// Tetrominos
	static Tetromino activeTetromino;
	static Tetromino activeTetrominoGhost;

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(71, 39, TerminalColor.BLACK);
		TerminalInputHook input = new TerminalInputHook();
		input.startHook(System.in);
		Game game = new Game();


		
		while (true) {
			game.tick();
			game.inputTick(input);
			game.draw(canvas);
			Thread.sleep(TARGET_FRAME_TIME);
		}
	}

}
