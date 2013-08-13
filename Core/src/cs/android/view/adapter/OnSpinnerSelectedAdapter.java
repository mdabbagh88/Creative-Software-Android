package cs.android.view.adapter;

import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.YES;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class OnSpinnerSelectedAdapter implements OnItemSelectedListener {

	private boolean _nothingSelected;
	private int _position;
	private boolean _ignoreFirstEvent = YES;

	public OnSpinnerSelectedAdapter(Spinner spinner) {
		spinner.setOnItemSelectedListener(this);
	}

	public boolean isNothingSelected() {
		return _nothingSelected;
	}

	@Override public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (_ignoreFirstEvent) {
			_ignoreFirstEvent = NO;
			return;
		}
		_position = arg2;
		onSelectChange();
	}

	@Override public void onNothingSelected(AdapterView<?> arg0) {
		if (_ignoreFirstEvent) {
			_ignoreFirstEvent = NO;
			return;
		}
		_position = -1;
		_nothingSelected = YES;
		onSelectChange();
	}

	public void onSelectChange() {

	}

	public int position() {
		return _position;
	}

	protected void ignoreFirstEvent(boolean ignoreFirstEvent) {
		_ignoreFirstEvent = ignoreFirstEvent;
	}

}
