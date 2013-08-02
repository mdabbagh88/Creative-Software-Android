package cs.android.viewbase;

import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuInflater;

import cs.android.HasActivity;
import cs.java.lang.Factory;

public interface CSActivity extends HasActivity, Factory<ViewController> {

	ViewController getPresenter();

	Object getSavedInstance();

	ActionBar getSupportActionBar();

	MenuInflater getSupportMenuInflater();

	FragmentManager getSupportFragmentManager();

	void supportInvalidateOptionsMenu();

}
