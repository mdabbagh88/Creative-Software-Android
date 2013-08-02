package cs.android.viewbase;

import cs.java.lang.Value;

public class OnMenu {

	public final com.actionbarsherlock.view.Menu menu;
	public final Value<Boolean> result;

	public OnMenu(com.actionbarsherlock.view.Menu menu) {
		this.menu = menu;
		this.result = new Value<Boolean>(false);
	}

}