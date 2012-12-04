package cs.android;

import cs.android.lang.AndroidLang;
import cs.android.lang.Application;
import cs.android.viewbase.ContextPresenter;

public abstract class ApplicationBase extends ContextPresenter implements Application {

	public ApplicationBase() {
		super(ApplicationContext.getContext());
		AndroidLang.setApplication(this);
	}

}
