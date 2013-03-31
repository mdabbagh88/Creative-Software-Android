package cs.android.view;

import static cs.android.lang.AndroidLang.alert;
import static cs.java.lang.Lang.asInt;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import cs.android.viewbase.Widget;

public class LimitTextFieldIntValue {

	private EditText _field;
	private final int _minValue;
	private int _maxValue;
	private int _beforeChangeValue;
	private final int _alertString;

	public LimitTextFieldIntValue(Widget<View> parent, int id, int minValue, int maxValue,
			int alertString) {
		_minValue = minValue;
		_maxValue = maxValue;
		_alertString = alertString;
		_field = parent.getEditText(id);
		_field.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					_beforeChangeValue = intValue();
				else if (intValue() > _maxValue || intValue() < _minValue) onWrongNumberEntered();
			}
		});
	}

	private int intValue() {
		return asInt(_field.getText());
	}

	private void onWrongNumberEntered() {
		_field.setText(_beforeChangeValue + "");
		alert(_alertString);
	}

	public void setMaxValue(int maxValue) {
		_maxValue = maxValue;
		validate();
	}

	private void validate() {
		if (intValue() > _maxValue) _field.setText(_maxValue + "");
		if (intValue() < _minValue) _field.setText(_minValue + "");
	}
}