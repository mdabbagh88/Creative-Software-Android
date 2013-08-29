package cs.java.io;

import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.string;

import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import cs.java.collections.List;

public class CSDir extends CSPath {

	public CSDir(CSDir... dirs) {
		setPath(toPathArray(dirs));
	}

	public CSDir(CSDir parent, String... path) {
		setPath(parent, path);
	}

	public CSDir(java.io.File file) {
		jreFile = file;
		check();
	}

	public CSDir(String... path) {
		setPath(path);
	}

	public CSDir(String parent, CSDir... path) {
		setPath(parent, toPathArray(path));
	}

	public void copyTo(CSDir destination) {
		try {
			destination.create();
			FileUtils.copyDirectory(jreFile, destination.toJRE(), true);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public void copyTo(CSDir destination, FileFilter filter) {
		try {
			destination.create();
			FileUtils.copyDirectory(jreFile, destination.toJRE(), filter);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public void create() {
		if (jreFile.exists()) return;
		jreFile.mkdirs();
		if (!jreFile.exists()) throw exception("Not created", this);
	}

	public void delete() {
		try {
			org.apache.commons.io.FileUtils.deleteDirectory(jreFile);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public List<CSPath> getChilds() {
		List<CSPath> list = list();
		for (String path : jreFile.list())
			list.add(CSPath.create(getPath() + SEPARATOR + path));
		return list;
	}

	public boolean isDir() {
		return true;
	}

	public Iterator<CSPath> iterator() {
		mustExist();
		List<CSPath> list = list();
		for (String path : jreFile.list())
			list.add(CSPath.create(getPath() + SEPARATOR + path));
		return list.iterator();
	}

	private void mustExist() {
		if (!exists()) throw exception("Dir not existing", getFullPath());
	}

	private String[] toPathArray(CSDir... dirs) {
		return string(",", dirs).split(",");
	}

	@Override protected void check() {
		if (jreFile.isFile()) throw exception("Not dir");
	}

}
