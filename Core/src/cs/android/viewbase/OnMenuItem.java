package cs.android.viewbase;

import android.view.MenuItem;
import cs.java.lang.Value;

public class OnMenuItem {
	public final Value<Boolean> result;
	public final MenuItem item;

	public OnMenuItem(MenuItem item) {
		this.item = item;
		result = new Value<Boolean>(false);
	}

	public int id() {
		return item.getItemId();
	}

	public void result(boolean b) {
		result.set(b);
	}
}