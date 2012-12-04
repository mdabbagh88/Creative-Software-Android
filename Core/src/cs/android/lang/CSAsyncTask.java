package cs.android.lang;

import android.os.AsyncTask;
import cs.java.lang.Run;

public abstract class CSAsyncTask extends AsyncTask<Void, Void, Void> implements Run {

	public CSAsyncTask() {
		execute((Void) null);
	}

	@Override protected Void doInBackground(Void... params) {
		run();
		return null;
	}
}
