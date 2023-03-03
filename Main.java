import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
	static final int TARGET_FRAME_TIME = 16;

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(53, 29, TerminalColor.BLACK);
		TerminalInputHook input = new TerminalInputHook();
		input.startHook(System.in);
		Game game = new Game();

		while (true) {
			game.draw(canvas);

			if (!game.gameEnded) {
				game.tick();
				game.inputTick(input);
			} else {
				String textHeader = " GAME END! ";
				String textScore = " FINAL SCORE: " + game.score + " ";
				String textLevel = " FINAL LEVEL: " + game.level + " ";
				canvas.drawString(canvas.width() / 2 - textHeader.length() / 2, canvas.height() / 2 - 1, textHeader, TerminalColor.WHITE, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textScore.length() / 2, canvas.height() / 2, textScore, TerminalColor.WHITE, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textLevel.length() / 2, canvas.height() / 2 + 1, textLevel, TerminalColor.WHITE, TerminalColor.BLACK);
			}


			canvas.renderBuffer(System.out);
			input.update();
			Timer.update(TARGET_FRAME_TIME);
			Thread.sleep(TARGET_FRAME_TIME);
		}
	}

}
