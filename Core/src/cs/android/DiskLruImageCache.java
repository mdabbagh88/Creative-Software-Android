package cs.android;

import static cs.java.lang.Lang.exception;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import cs.android.lang.AndroidLang;
import cs.android.viewbase.ContextPresenter;

public class DiskLruImageCache extends ContextPresenter {

	private final DiskLruCache mDiskCache;
	private CompressFormat mCompressFormat = CompressFormat.JPEG;
	private int mCompressQuality = 70;
	private static final int APP_VERSION = 1;
	private static final int VALUE_COUNT = 1;
	private final int IO_BUFFER_SIZE = 4 * 1024;

	public DiskLruImageCache() {
		try {
			mDiskCache = DiskLruCache.open(getDiskCacheDir(AndroidLang.getAplication()
					.getApplicationName()), APP_VERSION, VALUE_COUNT, Integer.MAX_VALUE);
		} catch (IOException e) {
			throw exception(e);
		}
	}

	public DiskLruImageCache(String uniqueName, int diskCacheSize, CompressFormat compressFormat,
			int quality) {
		try {
			mDiskCache = DiskLruCache.open(getDiskCacheDir(uniqueName), APP_VERSION, VALUE_COUNT,
					diskCacheSize);
			mCompressFormat = compressFormat;
			mCompressQuality = quality;
		} catch (IOException e) {
			throw exception(e);
		}
	}

	public void clearCache() {
		try {
			mDiskCache.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean containsKey(String key) {
		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			contained = snapshot != null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) snapshot.close();
		}
		return contained;
	}

	public Bitmap getBitmap(String key) {

		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			if (snapshot == null) return null;
			final InputStream in = snapshot.getInputStream(0);
			if (in != null) {
				final BufferedInputStream buffIn = new BufferedInputStream(in, IO_BUFFER_SIZE);
				bitmap = BitmapFactory.decodeStream(buffIn);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) snapshot.close();
		}
		return bitmap;
	}

	public File getCacheFolder() {
		return mDiskCache.getDirectory();
	}

	public void put(String key, Bitmap data) {
		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskCache.edit(key);
			if (editor == null) return;

			if (writeBitmapToFile(data, editor)) {
				mDiskCache.flush();
				editor.commit();
			} else editor.abort();
		} catch (IOException e) {
			try {
				if (editor != null) editor.abort();
			} catch (IOException ignored) {
			}
		}
	}

	private File getDiskCacheDir(String uniqueName) {
		final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ? getExternalCacheDir()
				.getPath() : context().getCacheDir().getPath();
		return new File(cachePath + File.separator + uniqueName);
	}

	private File getExternalCacheDir() {
		final File extCacheDir = new File(Environment.getExternalStorageDirectory(), "/Android/data/"
				+ context().getApplicationInfo().packageName + "/cache/");
		extCacheDir.mkdirs();
		return extCacheDir;
	}

	private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor) throws IOException,
			FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE);
			return bitmap.compress(mCompressFormat, mCompressQuality, out);
		} finally {
			if (out != null) out.close();
		}
	}

}
