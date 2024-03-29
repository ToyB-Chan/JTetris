import java.util.Random;

public class NetworkMessage {
	private static Random random = new Random();
	private int type;
	private int id;
	private String content;

	public NetworkMessage(int type) {
		this.type = type;
		this.id = random.nextInt();
		this.content = "NONE";
	}

	public NetworkMessage(int type, String content) {
		this.type = type;
		this.id = random.nextInt();
		this.content = content;
	}

	public NetworkMessage(int type, int content) {
		this.type = type;
		this.id = random.nextInt();
		this.content = Integer.toString(content);
	}

	public NetworkMessage(int type, float content) {
		this.type = type;
		this.id = random.nextInt();
		this.content = Float.toString(content);
	}

	public NetworkMessage(String transmitString) {
		String[] arr = transmitString.split(":::");
		this.type = Integer.parseInt(arr[0]);
		this.id = Integer.parseInt(arr[1]);
		this.content = arr[2];
	}

	public int type() {
		return this.type;
	}

	public int id() {
		return this.id;
	}

	public String contentString() {
		return this.content;
	}

	public int contentInt() {
		return Integer.parseInt(this.content);
	}

	public float contentFloat() {
		return Float.parseFloat(this.content);
	}

	public String toTransmitString() {
		return this.type + ":::" + this.id + ":::" + this.content;
	}

	public static final int MESSAGE_RECEIVED = 0;
	public static final int CLOSE_CONNECTION = 1;
	public static final int USERNAME = 2;
	public static final int GAME_BEGIN = 3;
	public static final int GAME_END = 4;
	public static final int STAT_SCORE = 5;
	public static final int STAT_LEVEL = 6;
	public static final int STAT_ROWS_REMOVED = 7;
	public static final int ADD_BLOCKING_ROWS = 8;
}