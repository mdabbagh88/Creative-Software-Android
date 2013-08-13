package cs.android.view.adapter;

import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.equalOne;
import static cs.java.lang.Lang.is;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cs.android.viewbase.Widget;
import cs.java.lang.Call;

public abstract class OnEditorActionAdapter implements OnEditorActionListener, Call<TextView> {

	private int _action;

	public OnEditorActionAdapter(Widget<?> widget, int id) {
		widget.getTextView(id).setOnEditorActionListener(this);
	}

	public OnEditorActionAdapter(Widget<?> widget, int id, int action) {
		_action = action;
		widget.getTextView(id).setOnEditorActionListener(this);
	}

	protected boolean isSubmitAction(int actionId, KeyEvent event) {
		if (equalOne(actionId, EditorInfo.IME_ACTION_SEARCH, EditorInfo.IME_ACTION_DONE)) return true;
		if (is(event) && event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) return true;
		return false;
	}

	@Override public boolean onEditorAction(TextView view, int action, KeyEvent event) {
		if (empty(_action)) {
			if (isSubmitAction(action, event)) {
				onCall(view);
				return true;
			}
		} else if (action == _action) {
			onCall(view);
			return true;
		}
		return false;
	}
}
