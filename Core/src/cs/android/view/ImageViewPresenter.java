package cs.android.view;

import static cs.android.lang.AndroidLang.fileInput;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import static cs.java.lang.Lang.set;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import cs.android.viewbase.Widget;

public class ImageViewPresenter extends Widget<ImageView> {

	private int requiredSize;

	public ImageViewPresenter(ImageView imageView) {
		super(imageView);
	}

	public ImageViewPresenter(ImageView imageView, String path) {
		super(imageView);
		load(path);
	}

	public void load(String path) {
		if (no(path)) return;
		if (path.startsWith("http"))
			new ImageViewBitmapDownloaderTask(asView(), path).execute();
		else {
			FileDescriptor descriptor = createDescriptor(Uri.parse(path));
			if (is(descriptor))
				loadDescriptorBitmap(descriptor);
			else loadLocalBitmap(path);
		}
	}

	public void load(String path, final int requiredSize) {
		this.requiredSize = requiredSize;
		if (no(path)) return;
		if (path.startsWith("http"))
			new ImageViewBitmapDownloaderTask(asView(), path, requiredSize).execute();
		else {
			FileDescriptor descriptor = createDescriptor(Uri.parse(path));
			if (is(descriptor))
				loadDescriptorBitmap(descriptor);
			else loadLocalBitmap(path);
		}
	}

	public void loadDescriptorBitmap(FileDescriptor descriptor) {
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		if (set(requiredSize)) decodeOptions.inSampleSize = findoutScale(descriptor);
		decodeOptions.inPurgeable = true;
		decodeOptions.inInputShareable = true;
		Bitmap bm = BitmapFactory.decodeFileDescriptor(descriptor, null, decodeOptions);
		asView().setImageBitmap(bm);
	}

	public void loadLocalBitmap(String path) {
		try {
			BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
			if (set(requiredSize)) decodeOptions.inSampleSize = findoutScale(path);
			decodeOptions.inPurgeable = true;
			decodeOptions.inInputShareable = true;

			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(path), null, decodeOptions);
			asView().setImageBitmap(bitmap);
			asImageView().setVisibility(View.VISIBLE);
		} catch (FileNotFoundException e) {
			throw exception(e);
		}
	}

	private FileDescriptor createDescriptor(Uri selectedImage) {
		try {
			return context().getContentResolver().openAssetFileDescriptor(selectedImage, "r")
					.getFileDescriptor();
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	private int findoutScale(FileDescriptor descriptor) {
		BitmapFactory.Options decodeBoundsOptions = new BitmapFactory.Options();
		decodeBoundsOptions.inJustDecodeBounds = true;
		decodeBoundsOptions.inPurgeable = true;
		decodeBoundsOptions.inInputShareable = true;

		BitmapFactory.decodeFileDescriptor(descriptor, null, decodeBoundsOptions);

		int scale = 1;
		while (decodeBoundsOptions.outWidth / scale / 2 >= requiredSize
				&& decodeBoundsOptions.outHeight / scale / 2 >= requiredSize)
			scale *= 2;
		return scale;
	}

	private int findoutScale(String path) {
		BitmapFactory.Options decodeBoundsOptions = new BitmapFactory.Options();
		decodeBoundsOptions.inJustDecodeBounds = true;
		decodeBoundsOptions.inPurgeable = true;
		decodeBoundsOptions.inInputShareable = true;

		BitmapFactory.decodeStream(fileInput(path), null, decodeBoundsOptions);
		int scale = 1;
		while (decodeBoundsOptions.outWidth / scale / 2 >= requiredSize
				&& decodeBoundsOptions.outHeight / scale / 2 >= requiredSize)
			scale *= 2;
		return scale;
	}

}
