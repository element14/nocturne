package com.bime.nocturne.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bime.nocturne.R;

import org.androidannotations.annotations.EFragment;

/**
 * A placeholder fragment containing a simple view.
 */
@EFragment
public class UserRegistrationActivityFragment extends Fragment {

    public UserRegistrationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_registration, container, false);
    }
}
