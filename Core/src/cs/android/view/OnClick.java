package cs.android.view;

import android.view.View;
import android.view.View.OnClickListener;
import cs.android.viewbase.IsView;
import cs.android.viewbase.Widget;

public abstract class OnClick implements OnClickListener {

	public OnClick(IsView view) {
		view.asView().setOnClickListener(this);
	}

	public OnClick(View view) {
		view.setOnClickListener(this);
	}

	public OnClick(Widget<?> view, int... viewId) {
		for (int id : viewId)
			view.getView(id).setOnClickListener(this);
	}

}
