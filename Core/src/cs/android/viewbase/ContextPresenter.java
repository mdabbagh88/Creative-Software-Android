package cs.android.viewbase;

import static cs.java.lang.Lang.array;
import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.no;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.LayoutInflater;
import cs.android.CSApplication;
import cs.android.HasContext;
import cs.android.aq.CSQuery;
import cs.java.collections.List;
import cs.java.lang.Base;

public abstract class ContextPresenter extends Base implements HasContext {

	private Context context;
	private CSQuery _aq;

	public ContextPresenter() {
	}

	public ContextPresenter(Context context) {
		setContext(context);
	}

	public String getAppKeyHash() {
		try {
			PackageInfo info = context().getPackageManager().getPackageInfo("cs.rcherz",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				return Base64.encodeToString(md.digest(), Base64.DEFAULT);
			}
		} catch (NameNotFoundException e) {
			error(e);
		} catch (NoSuchAlgorithmException e) {
			error(e);
		}
		return "";
	}

	public ContextPresenter(HasContext context) {
		setContext(context.context());
	}

	public PackageInfo getPackageInfo() {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public CSQuery aq() {
		if (no(_aq)) _aq = new CSQuery(this);
		return _aq;
	}

	@Override public Context context() {
		if (no(context)) return CSApplication.getContext();
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

	public String getStringResource(int id) {
		return getStringResource(id, "UTF-8");
	}

	public String getStringResource(int id, String encoding) {
		try {
			return new String(getResource(id, context()), encoding);
		} catch (UnsupportedEncodingException e) {
			error(e);
			return null;
		}
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

	protected int getColor(int color) {
		return context().getResources().getColor(color);
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

	protected byte[] getResource(int id, Context context) {
		try {
			Resources resources = context.getResources();
			InputStream is = resources.openRawResource(id);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] readBuffer = new byte[4 * 1024];
			try {
				int read;
				do {
					read = is.read(readBuffer, 0, readBuffer.length);
					if (read == -1) break;
					bout.write(readBuffer, 0, read);
				} while (true);
				return bout.toByteArray();
			} finally {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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