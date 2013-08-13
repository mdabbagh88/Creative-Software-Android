package cs.android.view.adapter;

import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public abstract class OnEffectClick implements OnClickListener {

	public OnEffectClick(final View view, final int color) {
		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					view.getBackground().setColorFilter(color, Mode.SRC_OVER);
					view.getBackground().invalidateSelf();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					view.getBackground().setColorFilter(0x00000000, Mode.SRC_OVER);
					view.getBackground().invalidateSelf();
					OnEffectClick.this.onClick(view);
				}
				return true;
			}
		});
	}

	public OnEffectClick(final ImageView view, final int color) {
		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) view.setColorFilter(color);
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					view.setColorFilter(0x00000000);
					OnEffectClick.this.onClick(view);
				}
				return true;
			}
		});
	}

}
