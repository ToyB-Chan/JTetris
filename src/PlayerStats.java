public class PlayerStats {
	public String username;
	public int score;
	public int level;
	public int rowsRemoved;

	public PlayerStats(String username) {
		this.username = username;
		this.score = 0;
		this.level = 1;
		this.rowsRemoved = 0;
	}
}
