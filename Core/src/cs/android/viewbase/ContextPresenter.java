package cs.android.viewbase;

import static cs.java.lang.Lang.array;
import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.no;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import cs.android.ApplicationContext;
import cs.android.HasContext;
import cs.java.collections.List;
import cs.java.lang.Base;

public abstract class ContextPresenter extends Base implements HasContext {

	private Context context;

	public ContextPresenter() {
	}

	public ContextPresenter(Context context) {
		setContext(context);
	}

	public ContextPresenter(HasContext context) {
		setContext(context.context());
	}

	@Override public Context context() {
		if (no(context)) return ApplicationContext.getContext();
		return context;
	}

	public ActivityManager getActivityManager() {
		return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

	public String getString(int id) {
		if (empty(id)) return "";
		return context().getResources().getString(id);
	}

	public String[] getStringArray(int id) {
		if (empty(id)) return array();
		return context().getResources().getStringArray(id);
	}

	public boolean isNetworkConnected() {
		ConnectivityManager connectivity = getConnectivity();
		return is(connectivity.getActiveNetworkInfo())
				&& connectivity.getActiveNetworkInfo().isConnected();
	}

	public Bitmap loadBitmap(int id) {
		if (empty(id)) return null;
		Drawable drawable = context().getResources().getDrawable(id);
		return ((BitmapDrawable) drawable).getBitmap();
	}

	protected ConnectivityManager getConnectivity() {
		return (ConnectivityManager) context().getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	protected KeyguardManager getKeyguardManager() {
		return (KeyguardManager) getService(Context.KEYGUARD_SERVICE);
	}

	protected LayoutInflater getLayoutInflater() {
		return (LayoutInflater) context().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	protected LocationManager getLocationManager() {
		return (LocationManager) context().getSystemService(Context.LOCATION_SERVICE);
	}

	protected PowerManager getPower() {
		return (PowerManager) context().getSystemService(Context.POWER_SERVICE);
	}

	protected Object getService(String serviceName) {
		return context().getSystemService(serviceName);
	}

	protected String getString(int id, Object... args) {
		if (empty(id)) return "";
		return String.format(context().getResources().getString(id), args);
	}

	protected List<String> getStringList(int id) {
		if (empty(id)) return list();
		return list(getStringArray(id));
	}

	protected TelephonyManager getTelephony() {
		return (TelephonyManager) context().getSystemService(Context.TELEPHONY_SERVICE);
	}

	protected boolean isServiceRunning(Class<? extends Service> serviceClass) {
		for (RunningServiceInfo service : getActivityManager().getRunningServices(Integer.MAX_VALUE))
			if (serviceClass.getName().equals(service.service.getClassName())) return true;
		return false;
	}

	protected void startService(Class<? extends Service> serviceClass) {
		startService(new Intent(context(), serviceClass));
	}

	protected void startService(Intent intent) {
		context().startService(intent);
	}

	protected void stopService(Class<? extends Service> serviceClass) {
		stopService(new Intent(context(), serviceClass));
	}

	protected void stopService(Intent intent) {
		context().stopService(intent);
	}

	void setContext(Context context) {
		this.context = context;
	}
}