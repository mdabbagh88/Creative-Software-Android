package cs.java.io;

import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.is;
import static java.io.File.separator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.apache.commons.io.IOUtils;

import cs.java.lang.Lang;

public class Io {

	public static void close(InputStream... streams) {
		for (InputStream stream : streams)
			if (is(stream)) try {
				stream.close();
			} catch (IOException ex) {
				throw exception(ex);
			}
	}

	public static void close(OutputStream out) {
		try {
			out.close();
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public static int copy(InputStream input, OutputStream output) {
		try {
			return IOUtils.copy(input, output);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public static java.io.File createFile(Object... path_members) {
		return new java.io.File(Lang.string(separator, path_members));
	}

	public static java.io.FileOutputStream createOutput(File file) {
		try {
			return new java.io.FileOutputStream(file);
		} catch (FileNotFoundException ex) {
			throw exception(ex);
		}
	}

	public static java.io.FileOutputStream createOutput(File file, boolean append) {
		try {
			return new java.io.FileOutputStream(file, append);
		} catch (FileNotFoundException ex) {
			throw exception(ex);
		}
	}

	public static JarOutputStream createOutput(OutputStream out) {
		try {
			return new JarOutputStream(out);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public static JarOutputStream createOutput(OutputStream out, Manifest man) {
		try {
			return new JarOutputStream(out, man);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public static void entryClose(JarOutputStream jar_out) {
		try {
			jar_out.closeEntry();
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public static Collection<File> list(File dir, boolean recursive) {
		return org.apache.commons.io.FileUtils.listFiles(dir, null, recursive);
	}

	public static void nextEntry(JarOutputStream jar_out, ZipEntry ze) {
		try {
			jar_out.putNextEntry(ze);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public static byte[] toBytes(InputStream input) {
		try {
			return IOUtils.toByteArray(input);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}

	public static String toString(InputStream input) {
		try {
			return IOUtils.toString(input);
		} catch (IOException ex) {
			throw exception(ex);
		}
	}
}
