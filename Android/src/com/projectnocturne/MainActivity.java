/**
 * <p>
 * <u><b>Copyright Notice</b></u>
 * </p><p>
 * The copyright in this document is the property of 
 * Bath Institute of Medical Engineering.
 * </p><p>
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 *  </p><p>
 *  <b><i>Copyright 2013-2014 Bath Institute of Medical Engineering.</i></b>
 * --------------------------------------------------------------------------
 * 
 */
package com.projectnocturne;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

import com.projectnocturne.datamodel.DbMetadata.RegistrationStatus;
import com.projectnocturne.services.PollingService;
import com.projectnocturne.views.AlertDetectedFragment;
import com.projectnocturne.views.Status1Fragment;
import com.projectnocturne.views.Welcome1Fragment;
import com.projectnocturne.views.Welcome2Fragment;

/**
 * <p>
 * This is the main activity for the android app.
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

	Welcome1Fragment welcome1Fragment = null;
	Welcome2Fragment welcome2Fragment = null;
	Status1Fragment status1Fragment = null;
	AlertDetectedFragment alertDetectedFragment = null;

	private NocturneApplication myApp = null;

	public static final String LOG_TAG = MainActivity.class.getSimpleName() + "::";

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
				// Specify a SpinnerAdapter to populate the dropdown
				// list.
				new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] { getString(R.string.title_status),
								getString(R.string.title_connect), getString(R.string.title_connection_requests) }),
				this);

		startSensorTagService();

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
			welcome1Fragment = new Welcome1Fragment();
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
			status1Fragment = new Status1Fragment();
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
		if (savedInstanceState.containsKey(MainActivity.STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(MainActivity.STATE_SELECTED_NAVIGATION_ITEM));
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
		outState.putInt(MainActivity.STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	/**
	 * 
	 */
	private void showScreen() {
		final RegistrationStatus currentRegStatus = NocturneApplication.getInstance().getDataModel()
				.getRegistrationStatus();
		switch (currentRegStatus) {
		case NOT_STARTED:
			welcome1Fragment = new Welcome1Fragment();
			getFragmentManager().beginTransaction().replace(R.id.container, welcome1Fragment).commit();
			break;
		case REQUEST_SENT:
			welcome2Fragment = new Welcome2Fragment();
			getFragmentManager().beginTransaction().replace(R.id.container, welcome2Fragment).commit();
			break;
		case REQUEST_ACCEPTED:
			status1Fragment = new Status1Fragment();
			getFragmentManager().beginTransaction().replace(R.id.container, status1Fragment).commit();
			break;
		case REQUEST_DENIED:
			break;
		}

	}

	private void startSensorTagService() {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG
				+ "startSensorTagService() starting sensor tag polling service.");
		final Intent longSvc = new Intent(this, PollingService.class);
		startService(longSvc);
	}

}
