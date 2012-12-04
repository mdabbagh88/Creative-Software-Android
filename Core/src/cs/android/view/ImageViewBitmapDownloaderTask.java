package cs.android.view;

import static cs.java.lang.Lang.set;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import cs.android.http.CSHttpClientImpl;

public class ImageViewBitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	private final String url;
	private final ImageView imageView;
	private int size;

	public ImageViewBitmapDownloaderTask(ImageView imageView, String url) {
		this.imageView = imageView;
		this.url = url;
	}

	public ImageViewBitmapDownloaderTask(ImageView imageView, String url, int size) {
		this.imageView = imageView;
		this.url = url;
		this.size = size;
	}

	@Override protected Bitmap doInBackground(String... params) {
		return downloadBitmap(url);
	}

	@Override protected void onPostExecute(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
	}

	private Bitmap downloadBitmap(String url) {
		CSHttpClientImpl client = new CSHttpClientImpl(url);
		if (set(size)) return client.getResponseBitmap(size);
		return client.responseBitmap();
	}
}