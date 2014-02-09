package cs.android.view;

import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.doLater;
import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.is;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import cs.android.R;
import cs.android.view.adapter.AnimationAdapter;
import cs.android.view.adapter.OnClick;
import cs.android.viewbase.ViewController;
import cs.java.event.Event;
import cs.java.lang.Call;
import cs.java.lang.Run;
import cs.java.lang.Value;

public class InViewController extends ViewController {

	private int _frameId;
	private ViewController _controller;
	private Event<ViewController> _onHide = event();
	private ViewController _parent;

	public InViewController(ViewController parent, int frameId) {
		super(parent);
		_parent = parent;
		_frameId = frameId;
	}

	public void hide() {
		hideController(null);
	}

	public void hideController() {
		hideController(null);
	}

	public void hideController(final Call<InViewController> onDone) {
		if (!isVisible()) return;
		Animation animation = AnimationUtils.loadAnimation(context(), R.anim.center_to_right);
		animation.setAnimationListener(new AnimationAdapter() {
			public void onAnimationEnd(Animation animation) {
				if (is(onDone)) onDone.onCall(InViewController.this);
			}
		});
		_controller.onHideFromInView();
		_controller.asView().startAnimation(animation);
		_controller.onDeinitialize(getState());
		getViewGroup(_frameId).removeView(_controller.asView());
		_controller.onDestroy();
		doLater(new Run() {
			public void run() {
				_parent.onInViewHide(_controller);
				if (is(_onHide)) {
					_onHide.run(_controller);
					_onHide.clear();
				}
				_controller = null;
			}
		});
	}

	public boolean isVisible() {
		return is(_controller);
	}

	public void onBackPressed(Value<Boolean> goBack) {
		super.onBackPressed(goBack);
		if (isVisible() && goBack.get()) {
			goBack.set(NO);
			hideController(null);
		}
	}

	public Event<ViewController> onHide() {
		return _onHide;
	}

	public void showController(final ViewController controller) {
		if (isVisible()) hideController(new Call<InViewController>() {
			public void onCall(InViewController value) {
				showControllerImpl(controller);
			}
		});
		else showControllerImpl(controller);
	}

	void showControllerImpl(ViewController controller) {
		_controller = controller;
		getViewGroup(_frameId).addView(_controller.asView());
		_controller.onInitialize(null);
		_controller.asView().startAnimation(
				AnimationUtils.loadAnimation(context(), R.anim.right_to_center));
		new OnClick(_controller) {
			public void onClick(View v) {
				hideController();
			}
		};
	}

}
