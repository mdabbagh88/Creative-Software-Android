package cs.android.view;

import static cs.java.lang.Lang.SECOND;
import static cs.java.lang.Lang.doLater;
import android.graphics.PorterDuff.Mode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cs.android.R;
import cs.java.lang.Run;

public abstract class OnEffectClick implements OnClickListener {

	public OnEffectClick(final ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageView.setColorFilter(R.color.button_selection);
				doLater(SECOND / 3, new Run() {
					@Override
					public void run() {
						imageView.setColorFilter(0x00000000);
					}
				});
				OnEffectClick.this.onClick(imageView);
			}
		});
	}

	public OnEffectClick(final View view) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				view.getBackground().setColorFilter(R.color.button_selection, Mode.SRC_OVER);
				view.getBackground().invalidateSelf();
				doLater(SECOND / 3, new Run() {
					@Override
					public void run() {
						view.getBackground().setColorFilter(0x00000000, Mode.SRC_OVER);
						view.getBackground().invalidateSelf();
					}
				});
				OnEffectClick.this.onClick(view);
			}
		});
	}

}
