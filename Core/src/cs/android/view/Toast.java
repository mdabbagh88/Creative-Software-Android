package cs.android.view;

import static cs.java.lang.Lang.SECOND;
import static cs.java.lang.Lang.empty;
import android.content.Context;
import android.os.CountDownTimer;

public class Toast extends android.widget.Toast {
	int duration = SECOND;

	public Toast(Context context) {
		super(context);
	}

	@Override public void setDuration(int miliseconds) {
		super.setDuration(LENGTH_SHORT);
		duration = miliseconds;
	}

	@Override public void show() {
		super.show();
		if (empty(duration)) return;
		new CountDownTimer(duration, 1000) {
			@Override public void onFinish() {
				Toast.this.show();
			}

			@Override public void onTick(long millisUntilFinished) {
				Toast.this.show();
			}
		}.start();
	}
}
