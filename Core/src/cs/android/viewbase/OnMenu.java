package cs.android.viewbase;

import android.view.Menu;
import android.view.MenuItem;
import cs.java.lang.Value;

public class OnMenu {

	public final Menu menu;
	public final Value<Boolean> result;

	public OnMenu(Menu menu) {
		this.menu = menu;
		result = new Value<Boolean>(false);
	}

	public MenuItem find(int id) {
		return menu.findItem(id);
	}

	public void result(boolean consumed) {
		result.set(consumed);
	}

}