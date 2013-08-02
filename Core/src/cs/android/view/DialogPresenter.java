package cs.android.view;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.set;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import cs.android.viewbase.ViewController;
import cs.java.lang.Call;
import cs.java.lang.Factory;

public class DialogPresenter extends ViewController {

	public static DialogPresenter create(final ViewController view, final int title,
			final int message, final int ok, final int cancel, final Call<Integer> call) {

		return create(view, view.getString(title), view.getString(message), ok, cancel, call);
	}

	public static DialogPresenter create(final ViewController view, final String title,
			final String message, final int dialogOk, final int dialogCancel, final Call<Integer> call) {
		return new DialogPresenter(view) {
			protected android.app.Dialog createDialog() {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context());
				if (set(title)) alertDialogBuilder.setTitle(title);
				if (set(message)) alertDialogBuilder.setMessage(message);
				alertDialogBuilder.setPositiveButton(dialogOk, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (is(call)) call.onCall(dialogOk);
					}
				});
				if (set(dialogCancel))
					alertDialogBuilder.setNegativeButton(dialogCancel, new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (is(call)) call.onCall(dialogCancel);
						}
					});
				return alertDialogBuilder.create();
			};
		};
	}

	private Dialog dialog;
	private Call<Dialog> dialogCreateListener;
	private int titleId;
	private int viewId;
	private Factory<Dialog> dialogFactory;
	private LayoutParams layoutParams;
	private Factory<View> viewFactory;
	private boolean isVisible;

	public DialogPresenter(ViewController hasActivity) {
		super(hasActivity);
	}

	public DialogPresenter(ViewController hasActivity, int viewId) {
		super(hasActivity);
		this.viewId = viewId;
	}

	public void hideDialog() {
		if (!isVisible) return;
		isVisible = false;
		dialog.dismiss();
	}

	public boolean isDialogVisible() {
		return isVisible;
	}

	@Override public void onPause() {
		super.onPause();
		if (is(dialog)) dialog.dismiss();
	}

	public void setDialogCreateListener(Call<Dialog> dialogCreateListener) {
		this.dialogCreateListener = dialogCreateListener;
	}

	public void setDialogFactory(Factory<Dialog> dialogFactory) {
		this.dialogFactory = dialogFactory;
	}

	public void setLayoutParams(LayoutParams layoutParams) {
		this.layoutParams = layoutParams;

	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public void setViewFactory(Factory<View> viewFactory) {
		this.viewFactory = viewFactory;
	}

	public void showDialog() {
		dialog = createDialog();
		if (is(dialogCreateListener)) dialogCreateListener.onCall(dialog);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override public void onDismiss(DialogInterface dialog) {
				onDialogDismiss();
			}
		});
		dialog.show();
		isVisible = true;
	}

	private Dialog createDialog(View view) {
		setView(view);
		Builder builder = new Builder(activity());
		if (is(layoutParams)) view.setLayoutParams(layoutParams);
		builder.setView(view);
		builder.setCancelable(true);
		if (set(titleId)) builder.setTitle(titleId);
		return builder.create();
	}

	private void onDialogDismiss() {
		dialog = null;
		setView(null);
	}

	protected Dialog createDialog() {
		if (set(viewId))
			return createDialog(inflateLayout(viewId));
		else if (is(viewFactory))
			return createDialog(viewFactory.create());
		else return dialogFactory.create();
	}

	@Override protected void onCreateRestore(Bundle state) {
		super.onCreateRestore(state);
		if (isVisible) showDialog();
	}

	@Override public void onDestroy() {
		super.onDestroy();
		dialog = null;
	}
}
