import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TerminalInputHook {
	private boolean active;
	private volatile ConcurrentLinkedQueue<Character> queue = new ConcurrentLinkedQueue<Character>();
	private List<Character> pressedKeys = new ArrayList<Character>();
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
		this.pressedKeys.clear();

		while (!queue.isEmpty()) {
			this.pressedKeys.add(queue.poll());
		}
	}

	public boolean active() { return this.active; }
	public boolean isKeyPressed(char key) {
		return this.pressedKeys.contains(key);
	}

	public List<Character> getPressedKeys() {
		List<Character> copy = new ArrayList<Character>();
		for (int i = 0; i < this.pressedKeys.size(); i++) {
			copy.add(this.pressedKeys.get(i));
		}
		
		return copy;
	}
}