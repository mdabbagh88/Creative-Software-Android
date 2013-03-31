package cs.android;

import static cs.java.lang.Lang.is;
import android.media.MediaPlayer;
import cs.android.viewbase.ViewController;

public class CSMediaPlayer extends ViewController {

	private int resource;
	private MediaPlayer mediaPlayer;

	public CSMediaPlayer(IActivityWidget parent) {
		super(parent);
	}

	public CSMediaPlayer(IActivityWidget parent, int resource) {
		super(parent);
		this.resource = resource;
	}

	public MediaPlayer getPlayer() {
		return mediaPlayer;
	}

	public void play() {
		play(resource);
	}

	public void play(int resource) {
		if (mediaPlayer != null) {
			stop();
			reset();
			release();
		}
		mediaPlayer = MediaPlayer.create(context(), resource);
		mediaPlayer.start();
	}

	public void stop() {
		try {
			mediaPlayer.stop();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public void release() {
		try {
			mediaPlayer.release();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		try {
			mediaPlayer.reset();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override protected void onPause() {
		super.onPause();
		if (is(mediaPlayer)) {
			reset();
			release();
		}
	}
}
