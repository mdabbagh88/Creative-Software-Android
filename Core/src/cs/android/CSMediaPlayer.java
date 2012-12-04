package cs.android;

import static cs.java.lang.Lang.is;
import android.media.MediaPlayer;
import cs.android.viewbase.ActivityWidget;

public class CSMediaPlayer extends ActivityWidget {

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
			try {
				mediaPlayer.stop();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			try {
				mediaPlayer.reset();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			try {
				mediaPlayer.release();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		mediaPlayer = MediaPlayer.create(context(), resource);
		mediaPlayer.start();
	}

	@Override protected void onPause() {
		super.onPause();
		if (is(mediaPlayer)) {
			mediaPlayer.reset();
			mediaPlayer.release();
		}
	}
}
