package cs.android.view;

import cs.android.viewbase.Widget;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OnSeekBarChange implements OnSeekBarChangeListener {

	public OnSeekBarChange(Widget<?> view, int... viewId) {
		for (int id : viewId)
			((SeekBar) view.getView(id)).setOnSeekBarChangeListener(this);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}
