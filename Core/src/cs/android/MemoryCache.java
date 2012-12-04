package cs.android;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.map;
import static cs.java.lang.Lang.no;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import cs.android.lang.ServerRequest;
import cs.android.rpc.BitmapRequest;
import cs.android.rpc.OnDone;
import cs.android.rpc.OnSuccess;
import cs.android.viewbase.ActivityWidget;
import cs.android.viewbase.ContextPresenter;
import cs.java.collections.Map;

public class MemoryCache extends ContextPresenter {

	private LruCache<String, Bitmap> mMemoryCache;
	private DiskLruImageCache diskCache;
	private final Map<String, BitmapRequest> requests = map();

	public void add(final BitmapRequest requestToAdd, final String key) {
		Bitmap image = getBitmap(key);
		if (is(image))
			requestToAdd.successWithouRun(image);
		else {
			BitmapRequest runningRequest = getRequest(key);
			if (is(runningRequest))
				// requestToAdd.stop();
				new OnSuccess<BitmapRequest>(runningRequest) {
					@Override
					public void run() {
						requestToAdd.successWithouRun(request.getBitmap());
					}
				};
			else {
				requests.put(key, requestToAdd);
				new OnSuccess<BitmapRequest>(requestToAdd) {
					@Override
					public void run() {
						putBitmap(key, request.getBitmap());
					}
				};
				new OnDone<ServerRequest>(requestToAdd) {
					 @Override
					public void run() {
						requests.remove(key);
					}
				};
			}
		}
	}

	public Bitmap getBitmap(String key) {
		Bitmap bitmap = getCache().get(key);
		if (no(bitmap)) return diskCache.getBitmap(key);
		return bitmap;
	}

	public BitmapRequest getRequest(String imageId) {
		return requests.value(imageId);
	}

	public void putBitmap(String key, Bitmap bitmap) {
		getCache().put(key, bitmap);
		diskCache.put(key, bitmap);
	}

	protected LruCache<String, Bitmap> getCache() {
		if (no(mMemoryCache)) {
			final int deviceMemoryClass = ActivityWidget.root.getActivityManager().getMemoryClass();
			final int cacheSize = 1024 * 1024 * deviceMemoryClass / 8;// 1024 * 1024 *
			mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};
			diskCache = new DiskLruImageCache();
		}
		return mMemoryCache;
	}

}
