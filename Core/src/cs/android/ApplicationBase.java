package cs.android;

import static cs.java.lang.Lang.no;

import java.io.File;

import android.os.Environment;
import cs.android.lang.Application;
import cs.android.lang.CSLogger;
import cs.android.viewbase.ContextController;
import cs.java.lang.Lang;

public abstract class ApplicationBase extends ContextController implements Application {

	private CSLogger _logger;

	public ApplicationBase() {
		super(CSApplication.getContext());
		Lang.setApplication(this);
		new File(cacheDir()).mkdirs();
	}

	public String cacheDir() {
		String dir = Environment.getExternalStorageDirectory() + File.separator + name();
		new File(dir).mkdirs();
		return dir;
	}

	public CSLogger logger() {
		return no(_logger) ? (_logger = new CSMemoryLoggerImpl(this)) : _logger;
	}
}
