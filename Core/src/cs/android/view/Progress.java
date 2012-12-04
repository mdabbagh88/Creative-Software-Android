package cs.android.view;

import static cs.java.lang.Lang.no;
import cs.android.IActivityWidget;
import cs.android.lang.ServerRequest;
import cs.android.rpc.OnDone;
import cs.android.viewbase.ActivityWidget;

public class Progress extends ActivityWidget {

	private ServerRequest request;

	public Progress(IActivityWidget parent, int layoutId) {
		super(parent, layoutId);
	}

	public void setRequest(ServerRequest request) {
		this.request = request;
		update();
	}

	@Override protected void onResume() {
		super.onResume();
		update();
	}

	private void onRequestDone() {
		request = null;
		hideView();
	}

	private void update() {
		if (no(request))
			hideView();
		else if (request.isDone())
			onRequestDone();
		else {
			showView();
			new OnDone<ServerRequest>(this, request) {
				@Override
				public void run() {
					onRequestDone();
				}
			};
		}
	}

}
