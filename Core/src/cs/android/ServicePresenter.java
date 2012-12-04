package cs.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import cs.android.viewbase.ContextPresenter;

public abstract class ServicePresenter extends ContextPresenter {
	protected final Service service;

	public ServicePresenter(Service service) {
		super(service);
		this.service = service;
	}

	protected IBinder onBind(Intent intent) {
		return null;
	}

	protected void onCreate() {
	}

	protected void onDestroy() {
	}

	protected void onRebind(Intent intent) {
	}

	protected void onStart(Intent intent, int startId) {
	}

	protected int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}

	protected boolean onUnbind(Intent intent) {
		return false;
	}

	protected void stopSelf() {
		service.stopSelf();
	}
}