package cs.android.view;

import static cs.java.lang.Lang.is;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import cs.android.IActivityWidget;
import cs.android.lang.AndroidLang;
import cs.android.viewbase.ViewController;
import cs.java.lang.Factory;

public class ErrorDialogPresenter extends ViewController implements Factory<Dialog> {

	private final DialogPresenter errorDialogPresenter = new DialogPresenter(this);
	private int dialogTitleId;
	private String email_text;
	private final String supportEmail;
	private final int emailTitle;
	private final int mailActivityChooserTitle;
	private final int sendButtonText;

	public ErrorDialogPresenter(IActivityWidget hasActivity, int sendButtonText, String supportMail,
			int emailTitle, int mailActivityChooserTitle) {
		super(hasActivity);
		errorDialogPresenter.setDialogFactory(this);
		this.sendButtonText = sendButtonText;
		supportEmail = supportMail;
		this.emailTitle = emailTitle;
		this.mailActivityChooserTitle = mailActivityChooserTitle;
	}

	 @Override
	public Dialog create() {
		Builder builder = new Builder(activity());
		builder.setMessage(dialogTitleId);
		builder.setPositiveButton(sendButtonText, new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				onSendButonClick();
			}
		});
		return builder.create();
	}

	public void showErrorDialog(int messageTitleId, String error_message, String error_details) {
		if (is(error_message)) email_text += "\n\n Message: " + error_message;
		if (is(error_details)) email_text += "\n\n Details:" + error_details;
		if (errorDialogPresenter.isDialogVisible()) return;
		dialogTitleId = messageTitleId;
		errorDialogPresenter.setTitleId(dialogTitleId);
		errorDialogPresenter.showDialog();
	}

	@Override protected void onCreateRestore(Bundle state) {
		super.onCreateRestore(state);
		errorDialogPresenter.setTitleId(dialogTitleId);
	}

	private void onSendButonClick() {
		sendMailToSupport();
	}

	private void sendMailToSupport() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { supportEmail });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(emailTitle) + " "
				+ AndroidLang.getAplication().getApplicationName());
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, email_text);
		startActivity(Intent.createChooser(emailIntent, getString(mailActivityChooserTitle)));
	}

}
