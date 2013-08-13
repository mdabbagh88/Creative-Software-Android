package cs.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class UntouchableEditText extends EditText {

	public UntouchableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public UntouchableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UntouchableEditText(Context context) {
		super(context);
	}

	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
}
