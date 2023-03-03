import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
	public static final int TARGET_FRAME_TIME = 16;

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(53, 29, TerminalColor.BLACK);
		TerminalInputHook input = new TerminalInputHook();
		input.startHook(System.in);
		Game game = new Game();
		boolean pause = false;

		SoundPlayer bgMusic = new SoundPlayer("./res/songa.wav", true);
		bgMusic.setVolume(0.5f);
		bgMusic.play();

		while (true) {
			game.draw(canvas);

			if (input.isKeyPressed('p') || input.isKeyPressed('P')) {
				pause = !pause;
			}

			if ((input.isKeyPressed('r') || input.isKeyPressed('R')) && game.gameEnded) {
				game = new Game();
			}

			if (!game.gameEnded && !pause) {
				game.tick();
				game.inputTick(input);
			} else if (!game.gameEnded && pause){
				String textHeader = " GAME PAUSED! ";
				String textHint = " PRESS 'P' TO CONTINUE! ";
				canvas.drawString(canvas.width() / 2 - textHeader.length() / 2, canvas.height() / 2, textHeader, TerminalColor.RED, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textHint.length() / 2, canvas.height() / 2 + 1, textHint, TerminalColor.LIGHT_GRAY, TerminalColor.BLACK);
			} else {
				String textHeader = " GAME END! ";
				String textScore = " FINAL SCORE: " + game.score + " ";
				String textLevel = " FINAL LEVEL: " + game.level + " ";
				String textRestart = " PRESS 'R' TO RESTART! ";
				canvas.drawString(canvas.width() / 2 - textHeader.length() / 2, canvas.height() / 2 - 2, textHeader, TerminalColor.RED, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textScore.length() / 2, canvas.height() / 2 - 1, textScore, TerminalColor.WHITE, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textLevel.length() / 2, canvas.height() / 2, textLevel, TerminalColor.WHITE, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textRestart.length() / 2, canvas.height() / 2 + 1, textRestart, TerminalColor.LIGHT_GRAY, TerminalColor.BLACK);
			}

			canvas.renderBuffer(System.out);
			input.update();
			Timer.update(TARGET_FRAME_TIME);
			Thread.sleep(TARGET_FRAME_TIME);
		}
	}

}
