package cs.android.view.state;

import static cs.java.lang.Lang.is;

import java.io.File;

import android.os.Bundle;
import cs.android.IActivityWidget;
import cs.android.view.ImageViewPresenter;
import cs.android.viewbase.ActivityWidget;

public class StateImageView extends ActivityWidget {

	private static final String IMAGE_FILE_PATH_STATE = "image_path_state";
	private File imagePath;
	private final int viewId;

	public StateImageView(IActivityWidget hasActivity, int viewId) {
		super(hasActivity, viewId);
		this.viewId = viewId;
	}

	public void clear() {
		asImageView().setImageBitmap(null);
		imagePath = null;
	}

	public boolean hasImage() {
		return is(imagePath);
	}

	@Override public void onCreateRestore(Bundle state) {
		super.onCreateRestore(state);
		String imageFilePath = state.getString(viewId + IMAGE_FILE_PATH_STATE);
		if (is(imageFilePath)) showImage(new File(imageFilePath));
	}

	@Override public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		if (is(imagePath)) state.putString(viewId + IMAGE_FILE_PATH_STATE, imagePath.toString());
	}

	public void showImage(File imagePath) {
		this.imagePath = imagePath;
		new ImageViewPresenter(asImageView()).load(imagePath.toString(), (int) toPixel(asImageView()
				.getLayoutParams().height));
	}

	@Override protected void onCreate() {
		super.onCreate();
	}
}
