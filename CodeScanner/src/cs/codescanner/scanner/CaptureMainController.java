package cs.codescanner.scanner;

import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.info;
import static cs.java.lang.Lang.list;

import java.io.IOException;
import java.util.Collection;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import cs.android.viewbase.OnKeyDownResult;
import cs.android.viewbase.ViewController;
import cs.codescanner.CaptureController;
import cs.codescanner.R;
import cs.codescanner.scanner.camera.CameraManager;
import cs.java.collections.List;

public class CaptureMainController extends ViewController implements SurfaceHolder.Callback {

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private Result savedResultToShow;
	private ViewfinderView viewfinderView;
	private Result _lastResult;
	private boolean hasSurface;
	private Collection<BarcodeFormat> decodeFormats;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	protected final CaptureController controller;
	private List<BarcodeFormat> _formats = list();

	public CaptureMainController(CaptureController controller) {
		super(controller);
		this.controller = controller;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	public Collection<BarcodeFormat> getFormats() {
		return _formats;
	}

	public Handler handler() {
		return handler;
	}

	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		_lastResult = rawResult;
		beepManager.playBeepSoundAndVibrate();
		onDecodeDone();
	}

	protected void onDecodeDone() {
	}

	public void onCreate(Bundle icicle) {
		hasSurface = false;
		inactivityTimer = new InactivityTimer(controller.activity());
		beepManager = new BeepManager(controller.activity());
	}

	public void onDestroy() {
		inactivityTimer.shutdown();
	}

	public void onKeyDown(OnKeyDownResult onKeyDown) {
		super.onKeyDown(onKeyDown);
		if (onKeyDown._keyCode == KeyEvent.KEYCODE_BACK) {
			if (_lastResult != null) {
				restartPreviewAfterDelay(0L);
				onKeyDown._return.set(true);
			}
		} else if (onKeyDown._keyCode == KeyEvent.KEYCODE_FOCUS
				|| onKeyDown._keyCode == KeyEvent.KEYCODE_CAMERA) onKeyDown._return.set(true);
	}

	public void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) getView(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
	}

	public void onResume() {
		cameraManager = new CameraManager(activity());

		viewfinderView = (ViewfinderView) getView(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		handler = null;
		_lastResult = null;

		onResetStatus();

		SurfaceView surfaceView = (SurfaceView) getView(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) initCamera(surfaceHolder);
		else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		beepManager.updatePrefs();

		inactivityTimer.onResume();
		decodeFormats = null;
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		onResetStatus();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) info("*** WARNING *** surfaceCreated() gave us a null surface!");
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	protected void onFrameworkBug() {
	}

	protected void onResetStatus() {
		viewfinderView.setVisibility(View.VISIBLE);
		_lastResult = null;
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		if (handler == null) savedResultToShow = result;
		else {
			if (result != null) savedResultToShow = result;
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			cameraManager.openDriver(surfaceHolder);
			if (handler == null)
				handler = new CaptureActivityHandler(this, decodeFormats, null, cameraManager);
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			error(ioe);
			onFrameworkBug();
		} catch (RuntimeException e) {
			error(e);
			onFrameworkBug();
		}
	}

	public Result lastResult() {
		return _lastResult;
	}
}
