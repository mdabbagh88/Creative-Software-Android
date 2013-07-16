package cs.android.view;

import static cs.java.lang.Lang.set;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NumberPicker extends android.widget.NumberPicker {

	private int _textSize;
	private String _textColor;

	public NumberPicker(Context context) {
		super(context);
	}

	public NumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override public void addView(View child) {
		super.addView(child);
		updateView(child);
	}

	@Override public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, params);
		updateView(child);
	}

	@Override public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		updateView(child);
	}

	public void setTextColor(String textColor) {
		_textColor = textColor;
	}

	public void setTextSize(int textSize) {
		_textSize = textSize;
	}

	public void updateView(View view) {
		if (view instanceof EditText) {
			if (set(_textSize)) ((EditText) view).setTextSize(_textSize);
			if (set(_textColor)) ((EditText) view).setTextColor(Color.parseColor(_textColor));
		}
	}

}
