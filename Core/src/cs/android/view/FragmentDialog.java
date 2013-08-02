//package cs.android.view;
//
//import static cs.java.lang.Lang.is;
//import static cs.java.lang.Lang.no;
//import static cs.java.lang.Lang.set;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.content.DialogInterface.OnDismissListener;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//
//import cs.java.lang.Value;
//
//public class FragmentDialog extends DialogFragment {
//
//	private Context _context;
//
//	public FragmentDialog(ViewController hasActivity, int theme) {
//		_theme = theme;
//		_context = hasActivity.context();
//	}
//
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		Dialog dialog = new Dialog(_context, _theme) {
//			@Override public void onBackPressed() {
//				if (no(view)) super.onBackPressed();
//				Value<Boolean> goBack = new Value<Boolean>(true);
//				view.onBackPressed(goBack);
//				if (goBack.get()) {
//					super.onBackPressed();
//				}
//			}
//
//			// @Override public boolean onCreateOptionsMenu(Menu menu) {
//			// return view.onCreateOptionsMenu(menu);
//			// }
//
//			// public boolean onOptionsItemSelected(MenuItem item) {
//			// return view.onOptionsItemSelected(item);
//			// }
//
//			// public boolean onPrepareOptionsMenu(Menu menu) {
//			// return view.onPrepareOptionsMenu(menu);
//			// }
//		};
//		dialog.setContentView(view.asView());
//		if (set(titleId)) dialog.setTitle(titleId);
//		return dialog;
//	}
//
//	private Dialog dialog;
//	private ViewController view;
//	private int titleId;
//	private int _theme;
//	private DialogHideListener dialogHideListener;
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
//		this._theme = theme;
//	}
//
//	public void setTitle(int titleId) {
//		this.titleId = titleId;
//	}
//
//	public void showDialog(ViewController view) {
//		if (is(this.view)) return;
//		this.view = view;
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
//		initializeDialog();
//		onDialogCreated(dialog);
//		dialog.show();
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
//	protected void onDialogCreated(Dialog dialog) {
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
