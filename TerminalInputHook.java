import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TerminalInputHook {
	private boolean active;
	private volatile ConcurrentLinkedQueue<Character> queue = new ConcurrentLinkedQueue<Character>();
	private ArrayList<TerminalInputListener> listeners = new ArrayList<TerminalInputListener>();
	private Thread hookThread;

	public void startHook(InputStream stream) {
		if (this.active) {
			return;
		} 

		// Start a new thread because stream.read() is blocking
		this.hookThread = new Thread(() -> {
			while (true) {
					try {
						char key = 0;
						key = (char)stream.read();
						queue.add(key);
					} catch (Exception e) {
						break;
					}
			}
		});

		this.active = true;
		this.hookThread.start();
	}

	public void stopHook() {
		if (!this.active) {
			return;
		} 

		hookThread.stop();
		this.active = false;
	}

	public void update() {
		while (!queue.isEmpty()) {
			Character key = queue.poll();
			for (int i = 0; i < listeners.size(); i++) {
				if (listeners.get(i).key == key || listeners.get(i).key == Character.MIN_VALUE) {
					listeners.get(i).keyPressed(key);
				}
			}
		}
	}

	public boolean active() { return this.active; }
	public void addListener(TerminalInputListener listener) { this.listeners.add(listener); }
	public void removeListener(TerminalInputListener listener) { this.listeners.remove(listener); }
}