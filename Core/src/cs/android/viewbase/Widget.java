package cs.android.viewbase;

import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import static cs.java.lang.Lang.set;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.widget.ViewAnimator;
import cs.android.HasContext;
import cs.android.aq.CSQuery;
import cs.java.collections.List;
import cs.java.common.Point;

public class Widget<T extends View> extends ContextPresenter implements IsView {

	public static <T extends View> Widget<T> load(View view) {
		try {
			return (Widget<T>) view.getTag();
		} catch (ClassCastException e) {
			return null;
		}
	}

	protected static LayoutId layout(int id) {
		return new LayoutId(id);
	}

	private View view;

	private CSQuery aq;

	public Widget(HasContext hascontext) {
		super(hascontext);
	}

	public Widget(HasContext hascontext, LayoutId layoutId) {
		this(hascontext);
		setInflateView(layoutId.value);
	}

	public Widget(int layoutId) {
		setInflateView(layoutId);
	}

	public Widget(final View view) {
		super(view.getContext());
		setView(view);
	}

	public Widget(final View parent, LayoutId layoutId) {
		super(new HasContext() {
			public Context context() {
				return parent.getContext();
			}
		});
		setInflateView(layoutId.value);
	}

	public Widget(Widget<?> parent, int viewId) {
		this(parent.getView(viewId));
	}

	Widget() {
	}

	public CSQuery aq() {
		if (no(aq)) aq = new CSQuery(asView());
		return aq;
	}

	public AbsListView asAbsListView() {
		return (AbsListView) asView();
	}

	public FrameLayout asFrame() {
		return (FrameLayout) asView();
	}

	public GridView asGridView() {
		return (GridView) asView();
	}

	public ImageView asImageView() {
		return (ImageView) asView();
	}

	public ListView asListView() {
		return (ListView) asView();
	}

	public ProgressBar asProgressBar() {
		return (ProgressBar) asView();
	}

	@SuppressWarnings("unchecked") public T asView() {
		return (T) view;
	}

	public Point center() {
		return new Point(getLeft() + getWidth() / 2, getTop() + getHeight() / 2);
	}

	public AlphaAnimation fadeIn() {
		return fadeIn(asView());
	}

	public AlphaAnimation fadeIn(int view) {
		return fadeIn(getView(view));
	}

	public AlphaAnimation fadeIn(View view) {
		show(view);
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(300);
		animation.setInterpolator(new AccelerateInterpolator());
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});
		view.startAnimation(animation);
		return animation;
	}

	public void fadeOut() {
		fadeOut(asView());
	}

	public void fadeOut(int view) {
		fadeOut(getView(view));
	}

	public void fadeOut(final View view) {
		if (!isVisible(view)) return;
		AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(300);
		animation.setInterpolator(new AccelerateInterpolator());
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				hide(view);
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});
		view.startAnimation(animation);
	}

	public Button getButton(int id) {
		return (Button) getView(id);
	}

	public CheckBox getCheckBox(int id) {
		return (CheckBox) getView(id);
	}

	public CompoundButton getCompoundButton(int id) {
		return (CompoundButton) getView(id);
	}

	public DatePicker getDatePicker(int id) {
		return (DatePicker) getView(id);
	}

	public Display getDisplay() {
		return ((WindowManager) context().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	}

	@SuppressWarnings("deprecation") public int getDisplayHeight() {
		return getDisplay().getHeight();
	}

	@SuppressWarnings("deprecation") public int getDisplayWidth() {
		return getDisplay().getWidth();
	}

	public EditText getEditText(int id) {
		return (EditText) getView(id);
	}

	public FrameLayout getFrameLayout(int id) {
		return (FrameLayout) getView(id);
	}

	public int getHeight() {
		return asView().getHeight();
	}

	public ImageView getImageView(int id) {
		return (ImageView) getView(id);
	}

	public int getLeft() {
		return asView().getLeft();
	}

	public LinearLayout getLinearLayout(int id) {
		return (LinearLayout) getView(id);
	}

	public ListView getListView(int id) {
		return (ListView) getView(id);
	}

	public Point getLocationOnScreen() {
		int[] location = new int[2];
		asView().getLocationOnScreen(location);
		return new Point(location[0], location[1]);
	}

	public RadioButton getRadioButton(int id) {
		return (RadioButton) getView(id);
	}

	public RadioGroup getRadioGroup(int id) {
		return (RadioGroup) getView(id);
	}

	public ScrollView getScrollView(int id) {
		return (ScrollView) getView(id);
	}

	public SeekBar getSeekBar(int i) {
		return (SeekBar) getView(i);
	}

	public String getTextValue(int id) {
		if (is(getTextView(id))) return getTextView(id).getText().toString();
		return null;
	}

	public TextView getTextView(int id) {
		return (TextView) getView(id);
	}

	public TimePicker getTimePicker(int id) {
		return (TimePicker) getView(id);
	}

	public ToggleButton getToggleButton(int id) {
		return (ToggleButton) getView(id);
	}

	public int getTop() {
		return asView().getTop();
	}

	public View getView() {
		return view;
	}

	public View getView(int id) {
		View child = asView().findViewById(id);
		if (no(child)) throw exception("View not found");
		return child;
	}

	@SuppressWarnings("unchecked") public <V extends View> V getView(int id, Class<V> clazz) {
		return (V) getView(id);
	}

	public ViewAnimator getViewAnimator(int id) {
		return (ViewAnimator) getView(id);
	}

	public ViewGroup getViewGroup(int id) {
		return (ViewGroup) getView(id);
	}

	public WebView getWebView(int id) {
		return (WebView) getView(id);
	}

	public int getWidth() {
		return asView().getWidth();
	}

	public void hide(int viewId) {
		hide(getView(viewId));
	}

	public void hide(int... viewIds) {
		for (int id : viewIds)
			hide(getView(id));
	}

	public void hide(View view) {
		view.setVisibility(View.GONE);
	}

	public void hideSoftInput(int flag) {
		InputMethodManager in = (InputMethodManager) getService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(asView().getWindowToken(), flag);
	}

	public void hideView() {
		hide(asView());
	}

	public View inflateLayout(int layout_id) {
		LayoutInflater inflater = (LayoutInflater) getService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(layout_id, null);
	}

	public boolean isChecked(int id) {
		return getCompoundButton(id).isChecked();
	}

	public boolean isPortraite() {
		return context().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	public boolean isVisible() {
		return asView().getVisibility() == View.VISIBLE;
	}

	public boolean isVisible(int id) {
		return isVisible(getView(id));
	}

	public boolean isVisible(View view) {
		return view.getVisibility() == View.VISIBLE;
	}

	public void setButtonText(int viewId, int text) {
		getButton(viewId).setText(text);
	}

	public void setButtonText(int viewId, String text) {
		getButton(viewId).setText(text);
	}

	public void setChecked(int id, boolean checked) {
		getCompoundButton(id).setChecked(checked);
	}

	public void setImageValue(int imageViewId, int imageResource) {
		getImageView(imageViewId).setImageResource(imageResource);
	}

	public void setInvisible() {
		setVisibility(View.INVISIBLE);
	}

	public void setOnCheckedChangeListener(int id, OnCheckedChangeListener listener) {
		getCompoundButton(id).setOnCheckedChangeListener(listener);
	}

	public void setOnClickListener(int id, OnClickListener onClickListener) {
		getView(id).setOnClickListener(onClickListener);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		asView().setOnClickListener(onClickListener);
	}

	public void setPercentAspectWidth(int viewId, int percent, int minimal, int maximal) {
		float onePercent = getDisplayWidth() / (float) 100;
		float wantedWidth = onePercent * percent;
		if (set(minimal) && wantedWidth < minimal)
			wantedWidth = minimal;
		else if (set(maximal) && wantedWidth > maximal) wantedWidth = maximal;

		View view = getView(viewId);
		float scalingFactor = wantedWidth / view.getLayoutParams().width;
		int scaledHeight = (int) (view.getLayoutParams().height * scalingFactor);

		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = (int) wantedWidth;
		layoutParams.height = scaledHeight;

		view.setLayoutParams(layoutParams);
	}

	public void setPercentWidth(int viewId, int percent, int minimal, int maximal) {
		setPercentWidth(getView(viewId), percent, minimal, maximal);
	}

	public void setPercentWidth(View view, int percent) {
		setPercentWidth(view, percent, 0, 0);
	}

	public void setPercentWidth(View view, int percent, int minimal, int maximal) {
		double onePercent = getDisplayWidth() / 100;
		int wantedSize = (int) (onePercent * percent);
		if (set(minimal) && wantedSize < minimal)
			wantedSize = minimal;
		else if (set(maximal) && wantedSize > maximal) wantedSize = maximal;
		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = wantedSize;
		view.setLayoutParams(layoutParams);
	}

	public void setTextValue(int viewId, int stringId) {
		getTextView(viewId).setText(getString(stringId));
	}

	public void setTextValue(int viewId, int stringId, Object... args) {
		getTextView(viewId).setText(getString(stringId, args));
	}

	public void setTextValue(int viewId, String text) {
		getTextView(viewId).setText(text);
	}

	public void setView(View view) {
		this.view = view;
		if (is(view)) view.setTag(this);
	}

	public void setViewVisible(int viewId, boolean visible) {
		if (visible)
			show(viewId);
		else hide(viewId);
	}

	public void setVisibility(int value) {
		asView().setVisibility(value);
	}

	public void setVisible(boolean visible) {
		if (visible)
			asView().setVisibility(View.VISIBLE);
		else asView().setVisibility(View.GONE);
	}

	public void show(int... viewIds) {
		for (int id : viewIds)
			show(getView(id));
	}

	public void show(View view) {
		view.setVisibility(View.VISIBLE);
	}

	public void showSoftInput(View view, int flag) {
		InputMethodManager in = (InputMethodManager) getService(Context.INPUT_METHOD_SERVICE);
		in.showSoftInput(view, flag);
	}

	public void showView() {
		show(asView());
	}

	public float toDp(float pixel) {
		Resources resources = asView().getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = pixel / (metrics.densityDpi / 160f);
		return dp;
	}

	public float toPixel(float dp) {
		Resources resources = asView().getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	protected Date getDate(DatePicker picker) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
		return calendar.getTime();
	}

	protected Date getDate(int picker) {
		return getDate(getDatePicker(picker));
	}

	protected Spinner getSpinner(int id) {
		return (Spinner) getView(id);
	}

	protected void setImageViewResource(int viewId, int drawable) {
		getImageView(viewId).setImageResource(drawable);
	}

	protected void setInflateView(int layoutId) {
		setView(inflateLayout(layoutId));
	}

	protected void setSpinnerData(Spinner spinner, List<String> strings) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context(),
				android.R.layout.simple_spinner_item, strings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

}
