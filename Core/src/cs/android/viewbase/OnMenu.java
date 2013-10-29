package cs.android.viewbase;

import android.view.Menu;
import cs.java.lang.Value;

public class OnMenu {

	public final Menu menu;
	public final Value<Boolean> result;

	public OnMenu(Menu menu) {
		this.menu = menu;
		result = new Value<Boolean>(false);
	}

}