package cs.android.viewbase;

public interface ScreenFactory {
	ViewController createView(ScreenHostView parent, String screenId);
}
