package cs.android.view;

import static cs.java.lang.Lang.is;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import cs.android.IActivityWidget;
import cs.android.viewbase.ActivityWidget;
import cs.android.viewbase.IsView;

public class ViewDialog extends ActivityWidget {

	private Dialog dialog;

	public ViewDialog(IActivityWidget hasActivity) {
		super(hasActivity);
	}

	public Dialog showDialog(IsView view) {
		Builder builder = new Builder(context());
		builder.setView(view.asView());
		dialog = builder.create();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				onDialogDissmiss();
			}
		});
		dialog.show();
		return dialog;
	}

	protected void onDialogDissmiss() {
		dialog = null;
	}

	@Override protected void onPause() {
		super.onPause();
		if (is(dialog)) {
			dialog.setOnDismissListener(null);
			dialog.dismiss();
			dialog = null;
		}
	}

}
