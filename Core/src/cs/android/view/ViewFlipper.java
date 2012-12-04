package cs.android.view;

import android.content.Context;
import android.util.AttributeSet;

// Fix from http://daniel-codes.blogspot.com/2010/05/viewflipper-receiver-not-registered.html
// Fixing bug : http://code.google.com/p/android/issues/detail?id=6191
public class ViewFlipper extends android.widget.ViewFlipper {

	public ViewFlipper(Context context) {
		super(context);
	}

	public ViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override protected void onDetachedFromWindow() {
		try {
			super.onDetachedFromWindow();
		} catch (IllegalArgumentException e) {
			stopFlipping();
		}
	}

}
