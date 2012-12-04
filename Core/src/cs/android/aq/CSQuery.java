package cs.android.aq;

import java.io.File;

import android.app.Activity;
import android.view.View;

import com.androidquery.AbstractAQuery;

import cs.android.ApplicationContext;

public class CSQuery extends AbstractAQuery<CSQuery> {

	public CSQuery(Activity act) {
		super(act);
	}

	public CSQuery() {
		super(ApplicationContext.getContext());
	}

	public CSQuery(View view) {
		super(view);
	}

	public CSQuery image(File file) {
		if (view != null) image(file, view.getWidth());
		return this;
	}

}