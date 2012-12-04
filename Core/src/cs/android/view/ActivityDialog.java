package cs.android.view;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.set;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import cs.android.IActivityWidget;
import cs.android.viewbase.ActivityWidget;
import cs.java.lang.Value;

public class ActivityDialog extends ActivityWidget {

	private Dialog dialog;
	private IActivityWidget view;
	private int titleId;
	private int theme;
	private DialogHideListener dialogHideListener;

	public ActivityDialog(IActivityWidget hasActivity) {
		super(hasActivity);
	}

	public ActivityDialog(IActivityWidget hasActivity, int theme) {
		this(hasActivity);
		setTheme(theme);
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void hideDialog() {
		if (is(dialog)) dismissDialog();
		if (is(view)) dissmissActivityView();
	}

	public void setTheme(int theme) {
		this.theme = theme;
	}

	public void setTitle(int titleId) {
		this.titleId = titleId;
	}

	public void showDialog(IActivityWidget view) {
		if (is(this.view)) return;
		this.view = view;
		showDialog();
		view.onInitialize(null);
	}

	@Override protected void onCreate() {
		super.onCreate();
		if (is(dialog)) dismissDialog();
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		if (is(dialog)) dismissDialog();
	}

	@Override protected void onResume() {
		super.onResume();
		if (is(dialog))
			dialog.show();
		else if (is(view)) showDialog();
	}

	private void dismissDialog() {
		dialog.dismiss();
		dialog = null;
	}

	private void dissmissActivityView() {
		view.onDeinitialize(getState());
		view = null;
	}

	private void onCancelDialog() {
		dissmissActivityView();
		dialog = null;
	}

	public void setDialogHideListener(DialogHideListener dialogHideListener) {
		this.dialogHideListener = dialogHideListener;
	}

	private void showDialog() {
		if (set(theme)) {
			dialog = new Dialog(context(), theme) {
				@Override public void onBackPressed() {
					Value<Boolean> goBack = new Value<Boolean>(true);
					view.onBackPressed(goBack);
					if (goBack.get()) super.onBackPressed();
				}
			};
			dialog.setContentView(view.asView());
			if (set(titleId)) dialog.setTitle(titleId);
		} else {
			Builder builder = new Builder(context());
			builder.setView(view.asView());
			if (set(titleId)) builder.setTitle(titleId);
			dialog = builder.create();
		}
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				onDialogDismiss();
			}
		});
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onCancelDialog();
			}
		});
		onDialogCreated(dialog);
		dialog.show();
	}

	protected void onDialogCreated(Dialog dialog2) {
	}

	private void onDialogDismiss() {
		if (set(dialogHideListener)) dialogHideListener.onDialogHide(this);
	}

}
