package cs.android.viewbase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class ToggleButtonGroup extends RadioGroup {

	public ToggleButtonGroup(Context context) {
		super(context);
		initialize();
	}

	public ToggleButtonGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	private void initialize() {
		setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
				for (int j = 0; j < radioGroup.getChildCount(); j++) {
					ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
					view.setChecked(view.getId() == i);
				}
			}
		});
	}

}
