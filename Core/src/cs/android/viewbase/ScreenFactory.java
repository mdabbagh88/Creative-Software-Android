package cs.android.viewbase;

public interface ScreenFactory {
	ViewController createView(ScreenHostViewBase parent, String screenId);
}
