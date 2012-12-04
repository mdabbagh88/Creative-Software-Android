package cs.codescanner;

import static cs.java.lang.Lang.Yes;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import cs.codescanner.scanner.CaptureMain;

public class CaptureActivity extends Activity {

	protected final CaptureMain capture = new CaptureMain(this) {
		@Override protected void onDecodeDone() {
			CaptureActivity.this.onDecodeDone();
		};

		@Override protected void onFrameworkBug() {
		}
	};

	public CaptureActivity() {
	}

	protected void onDecodeDone() {
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (capture.onKeyDown(keyCode, event)) return Yes;
		return super.onKeyDown(keyCode, event);
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.capture);
		capture.onCreate(savedInstanceState);
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		capture.onDestroy();
	}

	@Override protected void onPause() {
		super.onPause();
		capture.onPause();
	}

	@Override protected void onResume() {
		super.onResume();
		capture.onResume();
	}

}
