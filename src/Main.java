import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
	public static final int TARGET_FRAME_TIME = 16;

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(53, 29, TerminalColor.BLACK);
		TerminalInputHook input = new TerminalInputHook();
		input.startHook(System.in);

		boolean multiplayer = false;
		boolean host = false;
		NetworkManager netManager = new NetworkManager();

		if (multiplayer && host) {
			netManager.host(5611);
		} else if (multiplayer) {
			netManager.connect("127.0.0.1", 5611);
		}

		Game game = new Game(netManager);
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
				if (!multiplayer || multiplayer && host) {
					game = new Game(netManager);
					NetworkMessage msg = new NetworkMessage(NetworkMessage.GAME_BEGIN);
					netManager.send(msg);
				}
			}

			if (!game.gameEnded && !pause) {
				game.tick();
				game.inputTick(input);
			} else if (!game.gameEnded && pause && !multiplayer){
				String textHeader = " GAME PAUSED! ";
				String textHint = " PRESS 'P' TO CONTINUE! ";
				canvas.drawString(canvas.width() / 2 - textHeader.length() / 2, canvas.height() / 2, textHeader, TerminalColor.RED, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textHint.length() / 2, canvas.height() / 2 + 1, textHint, TerminalColor.LIGHT_GRAY, TerminalColor.BLACK);
			} else if (game.gameEnded) {
				String textHeader = " GAME END! ";
				String textScore = " FINAL SCORE: " + game.localStats.score + " ";
				String textLevel = " FINAL LEVEL: " + game.localStats.level + " ";
				String textRestart = " PRESS 'R' TO RESTART! ";
				canvas.drawString(canvas.width() / 2 - textHeader.length() / 2, canvas.height() / 2 - 2, textHeader, TerminalColor.RED, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textScore.length() / 2, canvas.height() / 2 - 1, textScore, TerminalColor.WHITE, TerminalColor.BLACK);
				canvas.drawString(canvas.width() / 2 - textLevel.length() / 2, canvas.height() / 2, textLevel, TerminalColor.WHITE, TerminalColor.BLACK);

				if (!multiplayer || multiplayer && host) {
					canvas.drawString(canvas.width() / 2 - textRestart.length() / 2, canvas.height() / 2 + 1, textRestart, TerminalColor.LIGHT_GRAY, TerminalColor.BLACK);
				} else if (multiplayer) {
					netManager.update();

					while (netManager.available() > 0) {
						NetworkMessage msg = netManager.nextMessage();

						if (msg.type() == NetworkMessage.GAME_BEGIN) {
							game = new Game(netManager);
						}
					}
				}
			}

			canvas.renderBuffer(System.out);
			input.update();
			Timer.update(TARGET_FRAME_TIME);
			Thread.sleep(TARGET_FRAME_TIME);
		}
	}
}
