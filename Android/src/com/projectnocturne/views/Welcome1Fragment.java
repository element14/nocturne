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
package com.projectnocturne.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;
import com.projectnocturne.datamodel.DbMetadata.RegistrationStatus;
import com.projectnocturne.datamodel.User;

public class Welcome1Fragment extends NocturneFragment {
	public static final String LOG_TAG = Welcome1Fragment.class.getSimpleName() + "::";

	private boolean readyFragment;
	private Button btnSubscribe;
	private TextView txtWelcomeScr1PersonNameFirst;
	private TextView txtWelcomeScr1PersonNameLast;
	private TextView txtWelcomeScr1MobilePhoneNbr;
	private TextView txtWelcomeScr1HomePhoneNbr;
	private TextView txtWelcomeScr1EmailAddress;

	TextWatcher textChangedWtchr = new TextWatcher() {
		@Override
		public void afterTextChanged(final Editable s) {
			Welcome1Fragment.this.enableSubscribeButton();
		}

		@Override
		public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
		}
	};

	private void enableSubscribeButton() {
		if ((this.txtWelcomeScr1PersonNameFirst.getText().length() > 0)
				&& (this.txtWelcomeScr1PersonNameLast.getText().length() > 0)
				&& (this.txtWelcomeScr1MobilePhoneNbr.getText().length() > 0)
				&& (this.txtWelcomeScr1HomePhoneNbr.getText().length() > 0)
				&& (this.txtWelcomeScr1EmailAddress.getText().length() > 0)) {
			this.btnSubscribe.setEnabled(true);
		} else {
			this.btnSubscribe.setEnabled(false);
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.update();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.activity_welcome_1, container, false);

		this.txtWelcomeScr1PersonNameFirst = (TextView) v.findViewById(R.id.welcomeScr1PersonNameFirst);
		this.txtWelcomeScr1PersonNameLast = (TextView) v.findViewById(R.id.welcomeScr1PersonNameLast);
		this.txtWelcomeScr1MobilePhoneNbr = (TextView) v.findViewById(R.id.welcomeScr1MobilePhoneNbr);
		this.txtWelcomeScr1HomePhoneNbr = (TextView) v.findViewById(R.id.welcomeScr1HomePhoneNbr);
		this.txtWelcomeScr1EmailAddress = (TextView) v.findViewById(R.id.welcomeScr1EmailAddress);
		this.btnSubscribe = (Button) v.findViewById(R.id.welcomeScr1BtnSubscribe);

		this.txtWelcomeScr1PersonNameFirst.addTextChangedListener(this.textChangedWtchr);
		this.txtWelcomeScr1PersonNameLast.addTextChangedListener(this.textChangedWtchr);
		this.txtWelcomeScr1MobilePhoneNbr.addTextChangedListener(this.textChangedWtchr);
		this.txtWelcomeScr1HomePhoneNbr.addTextChangedListener(this.textChangedWtchr);
		this.txtWelcomeScr1EmailAddress.addTextChangedListener(this.textChangedWtchr);

		this.btnSubscribe.setEnabled(false);
		this.btnSubscribe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final User usr = new User();
				usr.name_first = Welcome1Fragment.this.txtWelcomeScr1PersonNameFirst.getText().toString();
				usr.name_last = Welcome1Fragment.this.txtWelcomeScr1PersonNameLast.getText().toString();
				usr.phone_mbl = Welcome1Fragment.this.txtWelcomeScr1MobilePhoneNbr.getText().toString();
				usr.phone_home = Welcome1Fragment.this.txtWelcomeScr1HomePhoneNbr.getText().toString();
				usr.email1 = Welcome1Fragment.this.txtWelcomeScr1EmailAddress.getText().toString();
				usr.username = usr.email1;
				Welcome1Fragment.this.sendSubscriptionMessage(usr);
			}
		});
		this.readyFragment = true;

		this.update();
		return v;
	}

	protected void sendSubscriptionMessage(final User usr) {
		NocturneApplication.getInstance().getServerComms().sendSubscriptionMessage(this.getActivity(), usr);
		NocturneApplication.getInstance().getDataModel().addUser(usr);
		NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_SENT);
	}

	public void update() {
		if (!this.readyFragment) {
			Log.i(NocturneApplication.LOG_TAG, Welcome1Fragment.LOG_TAG + "update() not ready");
			return;
		}
		Log.i(NocturneApplication.LOG_TAG, Welcome1Fragment.LOG_TAG + "update() ready");

	}
}
