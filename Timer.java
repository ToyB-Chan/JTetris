public class Timer {
	protected final int interval;
	protected boolean shouldExecute = false;
	protected int elapsedTime = 0;

	public Timer(int interval) {
		this.interval = interval;
	}

	public void update(int deltaTime) {
		this.elapsedTime += deltaTime;

		if (this.elapsedTime > this.interval) {
			this.elapsedTime = 0;
			this.shouldExecute = true;
		} else {
			this.shouldExecute = false;
		}
	}
}
