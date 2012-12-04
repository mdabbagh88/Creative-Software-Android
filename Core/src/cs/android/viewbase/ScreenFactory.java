package cs.android.viewbase;

public interface ScreenFactory {
	ActivityWidget createView(ScreenHostView parent, String screenId);
}
