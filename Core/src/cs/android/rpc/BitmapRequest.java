package cs.android.rpc;

import android.graphics.Bitmap;
import cs.android.lang.ServerRequest;

public interface BitmapRequest extends ServerRequest {
	Bitmap getBitmap();

	void successWithouRun(Bitmap image);
}
