package cs.android.viewbase;

import android.widget.ViewAnimator;
import cs.android.IActivityWidget;
import cs.android.R;

public class AnimatorActivityViewer extends ActivityWidget {

	private ActivityWidget currentView;
	private final int viewAnimatorId;
	private int backOut = R.anim.center_to_right;
	private int backIn = R.anim.left_to_center;
	private int nextIn = R.anim.right_to_center;
	private int nextOut = R.anim.center_to_left;

	public AnimatorActivityViewer(IActivityWidget parent, int viewAnimatorId) {
		super(parent, viewAnimatorId);
		this.viewAnimatorId = viewAnimatorId;
	}

	public void displayView(ActivityWidget view, boolean isNext) {
		if (isNext)
			setNextAnimation();
		else setBackAnimation();
		currentView.onDeinitialize(getState());
		setCurrentView(view);
		currentView.onInitialize(null);
		getAnimator().setDisplayedChild(1);
		getAnimator().removeViewAt(0);
	}

	public ViewAnimator getAnimator() {
		return getViewAnimator(viewAnimatorId);
	}

	public ActivityWidget getCurrentView() {
		return currentView;
	}

	public void setBackAnimations(int backIn, int backOut) {
		this.backOut = backOut;
		this.backIn = backIn;
	}

	public void setCurrentView(ActivityWidget view) {
		currentView = view;
		getAnimator().addView(currentView.asView());
	}

	public void setNextAnimations(int nextIn, int nextOut) {
		this.nextOut = nextOut;
		this.nextIn = nextIn;
	}

	private void setBackAnimation() {
		getAnimator().setOutAnimation(context(), backOut);
		getAnimator().setInAnimation(context(), backIn);
	}

	private void setNextAnimation() {
		getAnimator().setOutAnimation(context(), nextOut);
		getAnimator().setInAnimation(context(), nextIn);
	}

}