import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
	public static final int TARGET_FRAME_TIME = 16;
	public static final int NET_PORT = 51470;

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(53, 29, TerminalColor.BLACK);
		TerminalInputHook input = new TerminalInputHook();
		input.startHook(System.in);
    
    	SoundPlayer bgMusic = new SoundPlayer("./res/songa.wav", true);
		bgMusic.setVolume(0.5f);
		bgMusic.play();
    
		NetworkManager netManager = new NetworkManager();
		Mainmenu menu = new Mainmenu(netManager);
		String username = "User";
		
		while (true) {
			menu.tick();
			menu.draw(canvas);
			menu.inputTick(input);

			if (menu.startGame) {
				break;
			}

			canvas.renderBuffer(System.out);
			input.update();
			Timer.update(TARGET_FRAME_TIME);
			Thread.sleep(TARGET_FRAME_TIME);
		}

		boolean multiplayer = menu.multiplayer;
		boolean host = menu.host;

		Game game = new Game(username, netManager);
		boolean pause = false;

		while (true) {
			game.draw(canvas);

			if (input.isKeyPressed('p') || input.isKeyPressed('P')) {
				pause = !pause;
			}

			if ((input.isKeyPressed('r') || input.isKeyPressed('R')) && game.gameEnded) {
				if (!multiplayer || multiplayer && host) {
					game = new Game(username, netManager);
					NetworkMessage msg = new NetworkMessage(NetworkMessage.GAME_BEGIN);
					netManager.sendReliable(msg);
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
							game = new Game(username, netManager);
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
