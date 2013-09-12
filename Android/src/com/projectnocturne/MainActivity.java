package com.projectnocturne;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

import com.projectnocturne.views.AlertDetectedFragment;
import com.projectnocturne.views.Status1Fragment;
import com.projectnocturne.views.Welcome1Fragment;
import com.projectnocturne.views.Welcome2Fragment;

/**
 * <p>
 * This activity is the main activity that is launched.
 * </p>
 * it is launched in response to the following:<br/>
 * <ul>
 * <li>The user selects the app from the launcher</li>
 * <li>The app detects an alert</li>
 * </ul>
 * 
 * @author aspela
 */
public class MainActivity extends Activity implements ActionBar.OnNavigationListener {

	Welcome1Fragment welcome1Fragment = new Welcome1Fragment();
	Welcome2Fragment welcome2Fragment = new Welcome2Fragment();
	Status1Fragment status1Fragment = new Status1Fragment();
	AlertDetectedFragment alertDetectedFragment = new AlertDetectedFragment();

	private NocturneApplication myApp = null;

	public static final String LOG_TAG = MainActivity.class.getSimpleName();

	/**
	 * The serialisation (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myApp = (NocturneApplication) getApplication();

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] { getString(R.string.title_section1),
								getString(R.string.title_section2), getString(R.string.title_section3), }), this);

		showScreen();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.ActionBar.OnNavigationListener#onNavigationItemSelected(int,
	 * long)
	 */
	@Override
	public boolean onNavigationItemSelected(final int itemPosition, final long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.

		switch (itemPosition) {
		case 0:
			getFragmentManager().beginTransaction().replace(R.id.container, welcome1Fragment).commit();
			break;
		case 1:
			// getSupportFragmentManager().beginTransaction().replace(R.id.container,
			// welcomeScreen2Fragment).commit();
			// break;
		case 2:
			// getFragmentManager().beginTransaction().replace(R.id.container,
			// wrkoutHistryFrgmnt).commit();
			// break;
		default:
			getFragmentManager().beginTransaction().replace(R.id.container, status1Fragment).commit();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	public void onRestoreInstanceState(final Bundle savedInstanceState) {
		// Restore the previously serialised current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(final Bundle outState) {
		// Serialise the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	/**
	 * 
	 */
	private void showScreen() {
		// TODO Auto-generated method stub

	}

}
