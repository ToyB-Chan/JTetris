import java.io.File;
import javax.sound.sampled.*;

public class SoundPlayer {
	public boolean valid;
	public boolean loop;
	public File file;
	protected Clip clip;

	SoundPlayer(String filePath, boolean loop) {
		try {
			this.file = new File ("./" + filePath);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL());
			this.clip = AudioSystem.getClip();
			clip.open(audioIn);
			this.valid = true;

			if (loop) {
				clip.loop(-1);
			}
		} catch(Exception e) {
			this.valid = false;
		}
	}

	public void play() {
		clip.start();
	}

	public void pause() {
		clip.stop();
	}

	public void reset() {
		clip.setFramePosition(0);
	}

	public boolean active() {
		return clip.isActive();
	}

	public void setVolume(float vol) {
		FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		control.setValue(20.f * (float)Math.log10(vol));
	}

	public float getVolume() {
		FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		return (float)Math.pow(10, control.getValue() / 20.f);
	}

	public static SoundPlayer playOnce(String filePath) {
		SoundPlayer player = new SoundPlayer(filePath, false);
		player.play();
		return player;
	}

	public static SoundPlayer playLoop(String filePath) {
		SoundPlayer player = new SoundPlayer(filePath, true);
		player.play();
		return player;
	}
}