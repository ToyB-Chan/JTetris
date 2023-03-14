public class NetworkMessage {
	public int type;
	private String content;

	public NetworkMessage(int type) {
		this.type = type;
		this.content = "NONE";
	}

	public NetworkMessage(int type, String content) {
		this.type = type;
		this.content = content;
	}

	public NetworkMessage(String transmitString) {
		String[] arr = transmitString.split(":::");
		this.type = Integer.parseInt(arr[0]);
		this.content = arr[1];
	}

	public String getContentString() {
		return this.content;
	}

	public int getContentInt() {
		return Integer.parseInt(this.content);
	}

	public float getContentFloat() {
		return Float.parseFloat(this.content);
	}

	public String toTransmitString() {
		return this.type + ":::" + this.content;
	}

	public static final int MESSAGE_RECEIVED = 0;
	public static final int USERNAME = 1;
	public static final int GAME_BEGIN = 2;
	public static final int GAME_END = 3;
	public static final int STAT_SCORE = 4;
	public static final int STAT_LEVEL = 5;
	public static final int STAT_ROWS_REMOVED = 6;
	public static final int ADD_ROWS = 7;
}