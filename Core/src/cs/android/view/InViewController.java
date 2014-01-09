package cs.android.view;

import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import cs.android.R;
import cs.android.viewbase.ViewController;
import cs.java.lang.Call;
import cs.java.lang.Value;

public class InViewController extends ViewController {

	int _inViewId;
	ViewController _controller;
	Call<InViewController> _onHide;

	public InViewController(ViewController parent, int inViewId) {
		super(parent);
		_inViewId = inViewId;
	}

	public void hideController() {
		hideFromFrame(null);
	}

	public void hideFromFrame(final Call<InViewController> onDone) {
		if (no(_controller)) return;
		Animation animation = AnimationUtils.loadAnimation(context(), R.anim.center_to_right);
		_controller.asView().startAnimation(animation);
		_controller.onDeinitialize(getState());
		getViewGroup(_inViewId).removeView(_controller.asView());
		_controller.onDestroy();
		_controller = null;
		if (is(onDone)) onDone.onCall(InViewController.this);
		if (is(_onHide)) _onHide.onCall(InViewController.this);
	}

	public void hideInView() {
		hideFromFrame(null);
	}

	public void onBackPressed(Value<Boolean> goBack) {
		super.onBackPressed(goBack);
		if (is(_controller) && goBack.get()) {
			goBack.set(NO);
			hideFromFrame(null);
		}
	}

	public void setOnHideListener(Call<InViewController> onHide) {
		_onHide = onHide;
	}

	public void showController(final ViewController controller) {
		if (is(_controller)) hideFromFrame(new Call<InViewController>() {
			public void onCall(InViewController value) {
				showControllerImpl(controller);
			}
		});
		else showControllerImpl(controller);
	}

	void showControllerImpl(ViewController controller) {
		_controller = controller;
		getViewGroup(_inViewId).addView(_controller.asView());
		_controller.onInitialize(null);
		_controller.asView().startAnimation(
				AnimationUtils.loadAnimation(context(), R.anim.right_to_center));
	}

}
