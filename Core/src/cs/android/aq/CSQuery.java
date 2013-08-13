package cs.android.aq;

import java.io.File;

import com.androidquery.AQuery;
import com.androidquery.AbstractAQuery;
import com.androidquery.callback.ImageOptions;

import cs.android.viewbase.ContextPresenter;
import cs.android.viewbase.ViewController;
import cs.android.viewbase.Widget;

public class CSQuery extends AbstractAQuery<CSQuery> {

	private ContextPresenter _cp;

	public CSQuery(ViewController controller) {
		super(controller.activity());
		_cp = controller;
	}

	public CSQuery(ContextPresenter controller) {
		super(controller.context());
		_cp = controller;
	}

	public CSQuery(Widget widget) {
		super(widget.asView());
		_cp = widget;
	}

	public CSQuery dynamicImage(String url) {
		if (_cp.isNetworkConnected()) return image(url, false, false);
		else return image(url);
	}

	public CSQuery image(File file) {
		if (view != null) image(file, view.getWidth());
		return this;
	}

	public int width() {
		return getView().getWidth();
	}

	public CSQuery gone(boolean gone) {
		if (gone) gone();
		else visible();
		return this;
	}

	public ImageOptions imageOptions(int widthInt) {
		ImageOptions options = new ImageOptions();
		options.animation = AQuery.FADE_IN;
		options.targetWidth = widthInt;
		return options;
	}

}