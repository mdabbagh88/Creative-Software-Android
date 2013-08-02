package cs.android.viewbase;

import cs.java.lang.Value;

public class OnMenuItem {
	public final Value<Boolean> result;
	public final com.actionbarsherlock.view.MenuItem item;

	public OnMenuItem(com.actionbarsherlock.view.MenuItem item) {
		this.item = item;
		this.result = new Value<Boolean>(false);
	}
}