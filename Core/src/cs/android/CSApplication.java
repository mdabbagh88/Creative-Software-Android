package cs.android;

import static cs.java.lang.Lang.info;
import android.app.Application;
import android.content.Context;

import com.androidquery.callback.BitmapAjaxCallback;

import cs.java.lang.Lang;

public class CSApplication extends Application {

	private static Context context;

	public static Context getContext() {
		return context;
	}

	@Override public void onCreate() {
		context = this;
		super.onCreate();
	}

	@Override public void onLowMemory() {
		info();
		BitmapAjaxCallback.clearCache();
		Lang.aplication().logger().emptyLog();
		super.onLowMemory();
	}

	@Override public void onTerminate() {
		info();
		super.onTerminate();
	}

}
