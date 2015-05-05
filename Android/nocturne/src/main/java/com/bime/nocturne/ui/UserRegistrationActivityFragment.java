package com.bime.nocturne.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bime.nocturne.NocturneApplication;
import com.percolate.caffeine.MiscUtils;
import com.bime.nocturne.R;
import com.bime.nocturne.datamodel.User;
import com.percolate.caffeine.ViewUtils;

import org.androidannotations.annotations.EFragment;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
@EFragment
public class UserRegistrationActivityFragment extends Fragment {
    public static final String LOG_TAG = UserRegistrationActivityFragment.class.getSimpleName() + "::";

    public UserRegistrationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_user_registration, container, false);

        txtWelcomeScr1StatusItem1Value = ViewUtils.findViewById(v, R.id.welcomeScr1StatusItem1_value);
        txtWelcomeScr1PersonNameFirst = ViewUtils.findViewById(v, R.id.welcomeScr1PersonNameFirst);
        txtWelcomeScr1PersonNameLast = ViewUtils.findViewById(v, R.id.welcomeScr1PersonNameLast);
        txtWelcomeScr1MobilePhoneNbr = ViewUtils.findViewById(v, R.id.welcomeScr1MobilePhoneNbr);
        txtWelcomeScr1HomePhoneNbr = ViewUtils.findViewById(v, R.id.welcomeScr1HomePhoneNbr);
        txtWelcomeScr1EmailAddress = ViewUtils.findViewById(v, R.id.welcomeScr1EmailAddress);
        txtWelcomeScr1Progress = ViewUtils.findViewById(v, R.id.welcomeScr1Progress);
        txtWelcomeScr1ErrorMessage = ViewUtils.findViewById(v, R.id.welcomeScr1ErrorMessage);
        txtWelcomeScr1ErrorMessageDetail = ViewUtils.findViewById(v, R.id.welcomeScr1ErrorMessageDetail);
        btnSubscribe = ViewUtils.findViewById(v, R.id.welcomeScr1BtnSubscribe);

        txtWelcomeScr1PersonNameFirst.addTextChangedListener(textChangedWtchr);
        txtWelcomeScr1PersonNameLast.addTextChangedListener(textChangedWtchr);
        txtWelcomeScr1MobilePhoneNbr.addTextChangedListener(textChangedWtchr);
        txtWelcomeScr1HomePhoneNbr.addTextChangedListener(textChangedWtchr);
        txtWelcomeScr1EmailAddress.addTextChangedListener(textChangedWtchr);

        txtWelcomeScr1ErrorMessage.setVisibility(View.INVISIBLE);
        txtWelcomeScr1ErrorMessageDetail.setVisibility(View.INVISIBLE);
        txtWelcomeScr1Progress.setVisibility(View.INVISIBLE);

        btnSubscribe.setEnabled(false);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                txtWelcomeScr1ErrorMessage.setVisibility(View.INVISIBLE);
                txtWelcomeScr1ErrorMessageDetail.setVisibility(View.INVISIBLE);
                txtWelcomeScr1Progress.setVisibility(View.VISIBLE);
                userDbObj.setName_first(txtWelcomeScr1PersonNameFirst.getText().toString());
                userDbObj.setName_last(txtWelcomeScr1PersonNameLast.getText().toString());
                userDbObj.setPhone_mbl(txtWelcomeScr1MobilePhoneNbr.getText().toString());
                userDbObj.setPhone_home(txtWelcomeScr1HomePhoneNbr.getText().toString());
                userDbObj.setEmail1(txtWelcomeScr1EmailAddress.getText().toString());
                UserRegistrationActivityFragment.this.sendRegistrationMessage(userDbObj);
            }
        });
        readyFragment = true;

        setHasOptionsMenu(true);
        this.update();
        return v;
    }
    protected void sendRegistrationMessage(final User usr) {
        if (usr.getUniqueId() == "") {
            userDbObj = NocturneApplication.getInstance().getDataModel().addUser(usr);
        } else {
            userDbObj = NocturneApplication.getInstance().getDataModel().updateUser(usr);
        }
        //NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_SENT);
        NocturneApplication.getInstance().getServerComms().sendSubscriptionMessage(getActivity(), handler, usr);
    }

    public void update() {
        if (!readyFragment) {
            NocturneApplication.logMessage(Log.INFO, LOG_TAG + "update() not ready");
            return;
        }
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "update() ready");
        final List<User> users = NocturneApplication.getInstance().getDataModel().getUsers();
        if (users.size() == 1) {
            userDbObj = users.get(0);
            txtWelcomeScr1PersonNameFirst.setText(userDbObj.getName_first());
            txtWelcomeScr1PersonNameLast.setText(userDbObj.getName_last());
            txtWelcomeScr1MobilePhoneNbr.setText(userDbObj.getPhone_mbl());
            txtWelcomeScr1HomePhoneNbr.setText(userDbObj.getPhone_home());
            txtWelcomeScr1EmailAddress.setText(userDbObj.getEmail1());
        } else {
            userDbObj = new User();
        }
        serverConnectionTask.execute();
    }


    TextWatcher textChangedWtchr = new TextWatcher() {
        @Override
        public void afterTextChanged(final Editable s) {
            UserRegistrationActivityFragment.this.enableSubscribeButton();
        }

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        }
    };

    private TextView txtWelcomeScr1StatusItem1Value;
    private Button btnSubscribe;
    private boolean readyFragment;
    private EditText txtWelcomeScr1EmailAddress;
    private EditText txtWelcomeScr1HomePhoneNbr;
    private EditText txtWelcomeScr1MobilePhoneNbr;
    private EditText txtWelcomeScr1PersonNameFirst;
    private EditText txtWelcomeScr1PersonNameLast;
    private User userDbObj = null;
    private TextView txtWelcomeScr1ErrorMessage;
    private ProgressBar txtWelcomeScr1Progress;
    private TextView txtWelcomeScr1ErrorMessageDetail;

    private void enableSubscribeButton() {
        if (txtWelcomeScr1PersonNameFirst.getText().length() > 0 &&
                txtWelcomeScr1PersonNameLast.getText().length() > 0 &&
                txtWelcomeScr1MobilePhoneNbr.getText().length() > 0 &&
                txtWelcomeScr1HomePhoneNbr.getText().length() > 0 &&
                txtWelcomeScr1EmailAddress.getText().length() > 0 &&
                MiscUtils.isValidEmail(txtWelcomeScr1EmailAddress.getText().toString())) {
            btnSubscribe.setEnabled(true);
        } else {
            btnSubscribe.setEnabled(false);
        }
    }
}
