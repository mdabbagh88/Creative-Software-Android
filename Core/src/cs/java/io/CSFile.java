package cs.java.io;

import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.is;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CSFile extends CSPath {

	private FileInputStream input;
	private FileOutputStream out;

	public CSFile(CSDir parent, String... path) {
		setPath(parent, path);
	}

	public CSFile(java.io.File file) {
		jreFile = file;
		check();
	}

	public CSFile(String... path) {
		setPath(path);
	}

	public void append(String text) {
		if (!exists()) create();
		try {
			FileWriter writer = new FileWriter(jreFile);
			writer.append(text);
			writer.close();
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public void closeIn() {
		if (is(input)) Io.close(input);
		else error("File not opened");
	}

	public void closeOut() {
		if (is(out)) Io.close(out);
		else error("File not opened");
	}

	public void copyTo(CSDir destination) {
		try {
			org.apache.commons.io.FileUtils.copyFileToDirectory(jreFile, destination.toJRE(), true);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public void create() {
		try {
			jreFile.createNewFile();
		} catch (IOException e) {
			throw exception(e, getPath());
		}
	}

	public void delete() {
		jreFile.delete();
	}

	public boolean isDir() {
		return false;
	}

	public List<String> lines() {
		try {
			return org.apache.commons.io.FileUtils.readLines(toJRE());
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public FileInputStream openIn() {
		try {
			return input = new FileInputStream(jreFile);
		} catch (FileNotFoundException ex) {
			throw exception(ex);
		}
	}

	public FileOutputStream openOut() {
		try {
			return out = new FileOutputStream(jreFile);
		} catch (FileNotFoundException ex) {
			throw exception(ex);
		}
	}

	public String read() {
		try {
			return org.apache.commons.io.FileUtils.readFileToString(toJRE());
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public URL toURL() {
		try {
			return jreFile.toURI().toURL();
		} catch (MalformedURLException ex) {
			error(ex);
		}
		return null;
	}

	public void write(String text) {
		clear();
		append(text);
	}

	@Override protected void check() {
		if (jreFile.isDirectory()) throw exception("Not file");
	}
}
