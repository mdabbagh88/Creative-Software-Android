package cs.android.viewbase;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.MenuInflater;
import cs.android.HasActivity;
import cs.java.lang.Factory;

public interface CSActivity extends HasActivity, Factory<ViewController> {

	ViewController getPresenter();

	Object getSavedInstance();

	ActionBar getSupportActionBar();

	FragmentManager getSupportFragmentManager();

	MenuInflater getSupportMenuInflater();

	void supportInvalidateOptionsMenu();

}
