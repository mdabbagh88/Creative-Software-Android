package cs.android.view.adapter;

import cs.android.viewbase.Widget;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class OnPageChangeAdapter implements OnPageChangeListener {

	public OnPageChangeAdapter(Widget parent, int id) {
		this(parent.getViewPager(id));
	}

	public OnPageChangeAdapter(ViewPager viewPager) {
		viewPager.setOnPageChangeListener(this);
	}

	public void onPageScrollStateChanged(int arg0) {
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	public void onPageSelected(int arg0) {
	}

}
