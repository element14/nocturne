package com.projectnocturne.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;


public class ConnectionRequestedFragment extends NocturneFragment {
    private final String LOG_TAG = ConnectionRequestedFragment.class.getSimpleName() + "::";
    private TextView txtEmailAddr;
    private ToggleButton swtchCarer;
    private boolean readyFragment;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        update();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_connection_request, container, false);

        txtEmailAddr = (TextView) v.findViewById(R.id.connect_user_email);
        swtchCarer = (ToggleButton) v.findViewById(R.id.connect_user_switch_carer);

        readyFragment = true;

        update();
        return v;
    }

    public void update() {
        if (!readyFragment) {
            Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "update() not ready");
            return;
        }
        Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "update() ready");

    }
}

