package cs.android.view;

import static cs.java.lang.Lang.list;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import cs.android.viewbase.ViewController;
import cs.java.collections.List;

public class CSPagerAdapter extends PagerAdapter {

	private List<View> _views = list();
	private List<String> _titles;

	public CSPagerAdapter(List<String> titles, ViewController... views) {
		_titles = titles;
		for (ViewController viewController : views)
			_views.add(viewController.asView());
	}

	@Override public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override public int getCount() {
		return _views.count();
	}

	public CharSequence getPageTitle(int position) {
		return _titles.at(position);
	}

	@Override public Object instantiateItem(ViewGroup container, int position) {
		View view = _views.at(position);
		container.addView(view, 0);
		return view;
	}

	@Override public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
