package cs.codescanner;

import android.os.Bundle;
import android.view.WindowManager.LayoutParams;
import cs.android.viewbase.ViewController;
import cs.codescanner.scanner.CaptureMainController;

public abstract class CaptureController extends ViewController {

	protected final CaptureMainController capture = new CaptureMainController(this) {
		@Override protected void onDecodeDone() {
			CaptureController.this.onDecodeDone();
		};

		@Override protected void onFrameworkBug() {
		}
	};

	public CaptureController() {
		super(layout(R.layout.capture));
	}

	protected void onDecodeDone() {
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity().getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

}
