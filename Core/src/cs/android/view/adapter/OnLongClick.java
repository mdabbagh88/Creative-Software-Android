package cs.android.view.adapter;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import cs.android.viewbase.IsView;
import cs.android.viewbase.Widget;

public abstract class OnLongClick implements OnLongClickListener {

	public OnLongClick(IsView view) {
		view.asView().setOnLongClickListener(this);
	}

	public OnLongClick(View view) {
		view.setOnLongClickListener(this);
	}

	public OnLongClick(ViewGroup view) {
		view.setOnLongClickListener(this);
	}

	public OnLongClick(ViewParent view) {
		((ViewGroup) view).setOnLongClickListener(this);
	}

	public OnLongClick(Widget<?> view, int... viewId) {
		for (int id : viewId)
			view.getView(id).setOnLongClickListener(this);
	}

}
