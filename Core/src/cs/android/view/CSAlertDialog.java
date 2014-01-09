package cs.android.view;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import static cs.java.lang.Lang.set;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
import cs.android.viewbase.ContextPresenter;
import cs.android.viewbase.Widget;
import cs.java.lang.Call;

public class CSAlertDialog extends ContextPresenter {

	private Builder _dialog;
	private EditText _inputField;

	public CSAlertDialog(Widget widget) {
		super(widget);
		_dialog = new AlertDialog.Builder(context());
	}

	public CSAlertDialog create(final int title, final int message, final int ok, final int cancel,
			final Call<Integer> call) {
		return create(getString(title), getString(message), ok, cancel, call);
	}

	public CSAlertDialog create(final String title, final String message, final int dialogOk,
			final int dialogCancel, final Call<Integer> call) {
		setText(title, message);
		setButtons(dialogOk, dialogCancel, call);
		return this;
	}

	public CSAlertDialog create(final String title, final String message, final String dialogOk,
			final String dialogCancel, final Call<String> call) {
		setText(title, message);
		setButtons(dialogOk, dialogCancel, call);
		return this;
	}

	public EditText inputField() {
		if (no(_inputField)) _inputField = new EditText(context());
		_dialog.setView(_inputField);
		return _inputField;
	}

	public String inputFieldText() {
		return inputField().getText().toString();
	}

	public void setButtons(final int dialogOk, final int dialogCancel, final Call<Integer> call) {
		_dialog.setPositiveButton(dialogOk, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (is(call)) call.onCall(dialogOk);
			}
		});
		if (set(dialogCancel)) _dialog.setNegativeButton(dialogCancel, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (is(call)) call.onCall(dialogCancel);
			}
		});
	}

	public void setButtons(final String dialogOk, final String dialogCancel, final Call<String> call) {
		_dialog.setPositiveButton(dialogOk, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (is(call)) call.onCall(dialogOk);
			}
		});
		if (set(dialogCancel)) _dialog.setNegativeButton(dialogCancel, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (is(call)) call.onCall(dialogCancel);
			}
		});
	}

	public void setText(final int title, final int message) {
		if (set(title)) _dialog.setTitle(title);
		if (set(message)) _dialog.setMessage(message);
	}

	public CSAlertDialog setText(final String title, final String message) {
		if (set(title)) _dialog.setTitle(title);
		if (set(message)) _dialog.setMessage(message);
		return this;
	}

	public CSAlertDialog show() {
		_dialog.show();
		return this;
	}

	public CSAlertDialog show(final int title, final int message, final int ok, final int cancel,
			final Call<Integer> call) {
		return show(getString(title), getString(message), ok, cancel, call);
	}

	public CSAlertDialog show(final String title, final String message, final int dialogOk,
			final int dialogCancel, final Call<Integer> call) {
		setText(title, message);
		setButtons(dialogOk, dialogCancel, call);
		show();
		return this;
	}

	public CSAlertDialog show(final String title, final String message, final String dialogOk,
			final String dialogCancel, final Call<String> call) {
		setText(title, message);
		setButtons(dialogOk, dialogCancel, call);
		show();
		return this;
	}
}
