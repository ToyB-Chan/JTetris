import java.util.ArrayList;
import java.util.List;

public class Timer {
	public static List<Timer> activeTimers = new ArrayList<Timer>();
	public int interval;
	protected boolean shouldExecute = false;
	protected int elapsedTime = 0;

	public Timer(int interval) {
		this.interval = interval;
		activeTimers.add(this);
	}

	public void invalidate() {
		activeTimers.remove(this);
	}

	public static void update(int deltaTime) {
		for (int i = 0; i < activeTimers.size(); i++) {
			activeTimers.get(i).elapsedTime += deltaTime;

			if (activeTimers.get(i).elapsedTime > activeTimers.get(i).interval) {
				activeTimers.get(i).elapsedTime = 0;
				activeTimers.get(i).shouldExecute = true;
			} else {
				activeTimers.get(i).shouldExecute = false;
			}
		}
	}
}
