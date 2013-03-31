package cs.android.viewbase;

import cs.android.HasActivity;
import cs.java.lang.Factory;

public interface IsActivityBase extends HasActivity, Factory<ViewController> {

	ViewController getPresenter();

}
