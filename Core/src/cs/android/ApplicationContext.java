package cs.android;

import static cs.java.lang.Lang.info;
import android.app.Application;
import android.content.Context;
import cs.android.lang.AndroidLangCore;
import cs.java.lang.Lang;

public class ApplicationContext extends Application {

	private static Context context;

	public static Context getContext() {
		return context;
	}

	@Override public void onCreate() {
		context = this;
		Lang.initialize(new AndroidLangCore());
		super.onCreate();
	}

	@Override public void onLowMemory() {
		info();
		super.onLowMemory();
	}

	@Override public void onTerminate() {
		info();
		super.onTerminate();
	}

}
