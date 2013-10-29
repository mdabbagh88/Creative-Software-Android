package cs.java.io;

import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.string;

public abstract class CSPath {
	static String SEPARATOR = java.io.File.separator;

	public static CSPath create(String path) {
		java.io.File file = new java.io.File(path);
		if (file.isFile()) return new CSFile(file);
		return new CSDir(file);
	}

	protected java.io.File jreFile;

	public CSDir asDir() {
		if (isDir()) return (CSDir) this;
		throw exception("Not a direcotry");
	}

	public CSFile asFile() {
		if (!isDir()) return (CSFile) this;
		throw exception("Not a file");
	}

	public void clear() {
		delete();
		create();
	}

	public abstract void create();

	public abstract void delete();

	public boolean exists() {
		return jreFile.exists();
	}

	public String getFullPath() {
		return jreFile.getAbsolutePath();
	}

	public String getName() {
		return jreFile.getName();
	}

	public CSDir getParent() {
		return new CSDir(jreFile.getParentFile());
	}

	public String getPath() {
		return jreFile.getPath();
	}

	public abstract boolean isDir();

	public boolean isFile() {
		return !isDir();
	}

	public void setPath(String... path) {
		jreFile = new java.io.File(string(SEPARATOR, path));
		check();
	}

	public void setPath(String parent, String... path) {
		jreFile = new java.io.File(parent, string(SEPARATOR, path));
		check();
	}

	public java.io.File toJRE() {
		return jreFile;
	}

	@Override public String toString() {
		return getPath();
	}

	protected abstract void check();

	protected void setPath(CSDir parent, String... path) {
		setPath(parent.getPath(), path);
	}
}
