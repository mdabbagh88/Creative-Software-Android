package cs.android;

import static cs.java.lang.Lang.no;

import java.io.File;

import cs.android.lang.Application;
import cs.android.lang.CSLogger;
import cs.android.viewbase.ContextPresenter;
import cs.java.lang.Lang;

public abstract class ApplicationBase extends ContextPresenter implements Application {

	private CSLogger _logger;

	public ApplicationBase() {
		super(CSApplication.getContext());
		Lang.setApplication(this);
		new File(cacheDir()).mkdirs();
	}

	public String cacheDir() {
		return context().getExternalCacheDir() + File.separator + name();
	}

	public CSLogger logger() {
		return no(_logger) ? (_logger = new CSLoggerImpl(this)) : _logger;
	}
}
