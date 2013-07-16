package cs.android.lang;

import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.info;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.warn;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cs.android.model.Settings;

public class C2DMManager extends BroadcastReceiver {

	public static void registerC2DM(Context context, String sender) {
		info("cloud", "Trying to register C2DM", sender);
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", sender);
		context.startService(registrationIntent);
	}

	public static void unregisterC2DM(Context context) {
		info("cloud", "Trying to unregister from C2DM");
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		context.startService(unregIntent);
	}

	@Override public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION"))
			onC2dmRegistration(context, intent);
		else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE"))
			onC2DMReceive(context, intent);
		else warn("Unexpected action");
	}

	private void onC2dmRegistration(Context context, Intent intent) {
		String registrationId = intent.getStringExtra("registration_id");
		if (is(intent.getStringExtra("error")))
			onC2DMRegistrationFailed(intent.getStringExtra("error"));
		else if (is(intent.getStringExtra("unregistered")))
			onC2DMUnregister();
		else if (is(registrationId)) {
			Settings.get().save("notification_registration_id", registrationId);
			onC2DMRegister(registrationId);
		} else warn("no action");
	}

	protected void onC2DMReceive(Context context, Intent intent) {
	}

	protected void onC2DMRegister(String registrationId) {
	}

	protected void onC2DMRegistrationFailed(String error) {
		error("Registration failed", error);
	}

	protected void onC2DMUnregister() {
	}

	protected String registrationId() {
		return Settings.get().loadString("notification_registration_id");
	}
}
