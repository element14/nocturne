package com.projectnocturne.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.projectnocturne.R;
import com.projectnocturne.datamodel.User;

public class Welcome1Fragment extends NocturneFragment {
	public static final String LOG_TAG = Welcome1Fragment.class.getSimpleName();

	private boolean readyFragment;
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
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.activity_welcome_1, container, false);

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

		readyFragment = true;

		update();
		return v;
	}

	protected void sendSubscriptionMessage(final User usr) {
		// TODO Auto-generated method stub

	}

	public void update() {
		if (!readyFragment) {
			Log.i(LOG_TAG, "update() not ready");
			return;
		}
		Log.i(LOG_TAG, "update() ready");

	}
}
