package cs.android.viewbase;

public interface TabViewFactory {
	ViewController createView(TabHostViewBase parent, int index);
}
