package com.projectnocturne;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.projectnocturne.datamodel.User;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
				final Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
			final TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	public static class WelcomeScreen1Fragment extends Fragment {

		private boolean readyWelcomeScreen1Fragment;
		private Button btnSubscribe;
		private TextView txtWelcomeScr1PersonNameFirst;
		private TextView txtWelcomeScr1PersonNameLast;
		private TextView txtWelcomeScr1MobilePhoneNbr;
		private TextView txtWelcomeScr1HomePhoneNbr;
		private TextView txtWelcomeScr1EmailAddress;

		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			update();
		}

		@Override
		public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
				final Bundle savedInstanceState) {
			final View v = inflater.inflate(R.layout.welcome_screen_1, container, false);

			txtWelcomeScr1PersonNameFirst = (TextView) v.findViewById(R.id.welcomeScr1PersonNameFirst);
			txtWelcomeScr1PersonNameLast = (TextView) v.findViewById(R.id.welcomeScr1PersonNameLast);
			txtWelcomeScr1MobilePhoneNbr = (TextView) v.findViewById(R.id.welcomeScr1MobilePhoneNbr);
			txtWelcomeScr1HomePhoneNbr = (TextView) v.findViewById(R.id.welcomeScr1HomePhoneNbr);
			txtWelcomeScr1EmailAddress = (TextView) v.findViewById(R.id.welcomeScr1EmailAddress);
			btnSubscribe = (Button) v.findViewById(R.id.welcomeScr1BtnSubscribe);

			btnSubscribe.setEnabled(false);
			btnSubscribe.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					final User usr = new User();
					usr.name_first = txtWelcomeScr1PersonNameFirst.getText().toString();
					usr.name_last = txtWelcomeScr1PersonNameLast.getText().toString();
					usr.phone_mbl = txtWelcomeScr1MobilePhoneNbr.getText().toString();
					usr.phone_home = txtWelcomeScr1HomePhoneNbr.getText().toString();
					usr.email1 = txtWelcomeScr1EmailAddress.getText().toString();
					sendSubscriptionMessage(usr);
				}
			});

			readyWelcomeScreen1Fragment = true;

			update();
			return v;
		}

		protected void sendSubscriptionMessage(final User usr) {
			// TODO Auto-generated method stub

		}

		public void update() {
			if (!readyWelcomeScreen1Fragment) {
				Log.i(MainActivity.LOG_TAG, "WelcomeScreen1Fragment::update() not ready");
				return;
			}
			Log.i(MainActivity.LOG_TAG, "WelcomeScreen1Fragment::update() ready");

		}
	}

	WelcomeScreen1Fragment welcomeScreen1Fragment = new WelcomeScreen1Fragment();

	private NocturneApplication myApp;
	public static final String LOG_TAG = MainActivity.class.getSimpleName();

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

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
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(final int itemPosition, final long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.

		switch (itemPosition) {
		case 0:
			getSupportFragmentManager().beginTransaction().replace(R.id.container, welcomeScreen1Fragment).commit();
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
			final Fragment fragment = new DummySectionFragment();
			final Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, itemPosition + 1);
			fragment.setArguments(args);
			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		}

		return true;
	}

	@Override
	public void onRestoreInstanceState(final Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

}
