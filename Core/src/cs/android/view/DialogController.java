//package cs.android.view;
//
//import static cs.java.lang.Lang.NO;
//import static cs.java.lang.Lang.YES;
//import static cs.java.lang.Lang.is;
//import static cs.java.lang.Lang.no;
//import static cs.java.lang.Lang.set;
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.content.DialogInterface.OnDismissListener;
//import android.graphics.Rect;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.WindowManager.LayoutParams;
//import cs.android.viewbase.ViewController;
//import cs.java.lang.Value;
//
//public class DialogController extends ViewController {
//
//	private Dialog dialog;
//	private ViewController view;
//	private int titleId;
//	private int theme;
//	private DialogHideListener dialogHideListener;
//
//	public DialogController(ViewController hasActivity, int theme) {
//		super(hasActivity);
//		setTheme(theme);
//	}
//
//	public Dialog getDialog() {
//		return dialog;
//	}
//
//	public void hideDialog() {
//		dismissDialog();
//		dissmissActivityView();
//	}
//
//	public void setDialogHideListener(DialogHideListener dialogHideListener) {
//		this.dialogHideListener = dialogHideListener;
//	}
//
//	public void setTheme(int theme) {
//		this.theme = theme;
//	}
//
//	public void setTitle(int titleId) {
//		this.titleId = titleId;
//	}
//
//	public void showDialog(ViewController view) {
//		if (is(this.view)) return;
//		this.view = view;
//		showDialog();
//		view.onInitialize(null);
//	}
//
//	private void dismissDialog() {
//		if (no(dialog)) return;
//		dialog.dismiss();
//		dialog = null;
//	}
//
//	private void dissmissActivityView() {
//		if (no(view)) return;
//		view.onDeinitialize(getState());
//		view = null;
//	}
//
//	private void initializeDialog() {
//		dialog.setCancelable(YES);
//		Window window = dialog.getWindow();
//		window.setGravity(Gravity.BOTTOM);
//
//		window.setLayout(LayoutParams.MATCH_PARENT, getDisplayHeight() - getStatusBarHeight());
//
//		window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
//
//		WindowManager.LayoutParams wlp = window.getAttributes();
//
//		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//		window.setAttributes(wlp);
//
//		dialog.setOnDismissListener(new OnDismissListener() {
//			@Override public void onDismiss(DialogInterface dialog) {
//				onDialogDismiss();
//			}
//		});
//		dialog.setOnCancelListener(new OnCancelListener() {
//			@Override public void onCancel(DialogInterface dialog) {
//				onDialogCancel();
//			}
//		});
//		view.setDialog(dialog);
//	}
//
//	private void onDialogCancel() {
//		if (set(dialogHideListener)) dialogHideListener.onDialogHide(this);
//	}
//
//	private void onDialogDismiss() {
//		dissmissActivityView();
//		dialog = null;
//	}
//
//	private void showDialog() {
//		final DialogController _this = this;
//		if (set(theme)) {
//			dialog = new Dialog(context(), theme) {
//				@Override public void onBackPressed() {
//					if (no(view)) super.onBackPressed();
//					Value<Boolean> goBack = new Value<Boolean>(true);
//					view.onBackPressed(goBack);
//					if (goBack.get()) {
//						super.onBackPressed();
//					}
//				}
//
//				@Override public boolean onCreateOptionsMenu(Menu menu) {
//					return view.onCreateOptionsMenu(menu);
//				}
//
//				public boolean onOptionsItemSelected(MenuItem item) {
//					return view.onOptionsItemSelected(item);
//				}
//
//				public boolean onPrepareOptionsMenu(Menu menu) {
//					return view.onPrepareOptionsMenu(menu);
//				}
//			};
//			dialog.setContentView(view.asView());
//
//			dialog.setTitle(null);
//			// dialog.setCancelable(true);
//
//			if (set(titleId)) dialog.setTitle(titleId);
//		} else {
//			Builder builder = new Builder(context());
//			builder.setView(view.asView());
//			if (set(titleId)) builder.setTitle(titleId);
//			dialog = builder.create();
//		}
//
//		initializeDialog();
//		onDialogCreated(dialog);
//		dialog.show();
//	}
//
//	private int getStatusBarHeight() {
//		Rect rectgle = new Rect();
//		Window window = activity().getWindow();
//		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
//		int statusBarHeight = rectgle.top;
//		int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
//		int titeBarHeight = contentViewTop - statusBarHeight;
//		return titeBarHeight + statusBarHeight;
//	}
//
//	@Override public void onBackPressed(Value<Boolean> goBack) {
//		if (is(dialog)) {
//			dialog.dismiss();
//			goBack.set(NO);
//		}
//		super.onBackPressed(goBack);
//	}
//
//	@Override protected void onCreate() {
//		super.onCreate();
//		if (is(dialog)) dismissDialog();
//	}
//
//	@Override public void onDestroy() {
//		super.onDestroy();
//		if (is(dialog)) dismissDialog();
//	}
//
//	protected void onDialogCreated(Dialog dialog2) {
//	}
//
//	@Override public void onResume() {
//		super.onResume();
//		if (is(dialog))
//			dialog.show();
//		else if (is(view)) showDialog();
//	}
//
// }
