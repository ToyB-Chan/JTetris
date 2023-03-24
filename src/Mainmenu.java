import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Mainmenu {
	public int selectedIndex;
	public String[] selections;
	public boolean startGame;
	public boolean multiplayer;
	public boolean host;
	public String username;
	public NetworkManager netManager;
	private int mode;
	private String inputIp;
	

	public Mainmenu(NetworkManager netManager){
		this.selectedIndex = 0;
		this.selections = new String[]{"PLAY SOLO", "HOST GAME", "JOIN GAME"};
		this.multiplayer = false;
		this.host = false;
		this.mode = 0;
		this.netManager = netManager;
	}

	public void draw(TerminalCanvas canvas) throws UnknownHostException{
		canvas.drawArea(0, 0, canvas.width(), canvas.height(), '\r', TerminalColor.TRANSPARENT, TerminalColor.BLACK);

		if (this.mode == 0) {
			for (int i = 0; i < selections.length; i++) {
				if (i == selectedIndex) {
					canvas.drawString(canvas.width()/2 - selections[i].length()/2, canvas.height()/2 + i * 2, selections[i], TerminalColor.BLACK, TerminalColor.WHITE);
				} else {
					canvas.drawString(canvas.width()/2 - selections[i].length()/2, canvas.height()/2 + i * 2, selections[i], TerminalColor.WHITE, TerminalColor.BLACK);
				}
			}
		}

		if (this.mode == 1) {
			String hostText = "Waiting for other player...";
			String ipText = "IP: " + InetAddress.getLocalHost();
			String cancelText = "CANCLE";
			canvas.drawString(canvas.width()/2 - hostText.length()/2, canvas.height()/2, hostText, TerminalColor.WHITE, TerminalColor.BLACK);
			canvas.drawString(canvas.width()/2 - ipText.length()/2, canvas.height()/2 + 2, ipText, TerminalColor.WHITE, TerminalColor.BLACK);
			canvas.drawString(canvas.width()/2 - cancelText.length()/2, canvas.height()/2 + 4, cancelText, TerminalColor.BLACK, TerminalColor.WHITE);
		}

		if (this.mode == 2) {
			String ipText = "Input IP: " + this.inputIp;
			String connectText = "CONNECT";
			canvas.drawString(canvas.width()/2 - ipText.length()/2, canvas.height()/2, ipText, TerminalColor.WHITE, TerminalColor.BLACK);
			canvas.drawString(canvas.width()/2 - connectText.length()/2, canvas.height()/2 + 2, connectText, TerminalColor.BLACK, TerminalColor.WHITE);
		}

		if (this.mode == 3) {
			String connectingText = "Connecting to host...";
			String cancelText = "CANCLE";
			canvas.drawString(canvas.width()/2 - connectingText.length()/2, canvas.height()/2, connectingText, TerminalColor.WHITE, TerminalColor.BLACK);
			canvas.drawString(canvas.width()/2 - cancelText.length()/2, canvas.height()/2 + 2, cancelText, TerminalColor.BLACK, TerminalColor.WHITE);
		}
	}

	public void inputTick(TerminalInputHook input) {
		if((input.isKeyPressed('W') || input.isKeyPressed('w')) && this.mode == 0 && this.selectedIndex > 0 ){
			this.selectedIndex--;
		} else if ((input.isKeyPressed('S') || input.isKeyPressed('s')) && this.mode == 0 && this.selectedIndex < this.selections.length-1) {
			this.selectedIndex++;
		} else if(input.isKeyPressed('\r') && this.mode == 0 && selectedIndex == 0){
			this.startGame = true;

			this.username = "User";
		} else if(input.isKeyPressed('\r') && this.mode == 0 && selectedIndex == 1){
			this.mode = 1;
		} else if(input.isKeyPressed('\r') && this.mode == 0 && selectedIndex == 2){
			this.inputIp = "";
			this.mode = 2;
		} else if(input.isKeyPressed('\r') && this.mode == 1){
			this.mode = 0;
		} else if(input.isKeyPressed('\r') && this.mode == 2){
			this.mode = 3;
		} else if(input.isKeyPressed('\r') && this.mode == 3){
			this.mode = 0;
		}

		if (this.mode == 2) {
			List<Character> keys = input.getPressedKeys();
			for (int i = 0; i < keys.size(); i++) {
				if (keys.get(i) == '\b' && inputIp.length() > 0) {
					inputIp = inputIp.substring(0, inputIp.length() - 1);
					continue;
				}


				inputIp += keys.get(i);
			}

			inputIp = inputIp.replaceAll("\n", "");
			inputIp = inputIp.replaceAll("\r", "");
			inputIp = inputIp.replaceAll(" ", "");
		}
	}

	public void tick() throws IOException{
		if (this.mode == 1) {
			if (this.netManager.host(Main.NET_PORT)) {
				this.multiplayer = true;
				this.host = true;
				this.startGame = true;

				this.username = "HOST";
			}
		}

		if (this.mode == 3) {
			if (this.netManager.connect(this.inputIp, Main.NET_PORT)) {
				this.multiplayer = true;
				this.host = false;
				this.startGame = true;

				this.username = "CLIENT";
			}
		}

	}
}


