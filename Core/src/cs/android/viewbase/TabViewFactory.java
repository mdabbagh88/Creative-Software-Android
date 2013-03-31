package cs.android.viewbase;

public interface TabViewFactory {
	ViewController createView(TabHostView parent, int index);
}
