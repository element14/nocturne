package com.projectnocturne.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;

public class ConnectionRequestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_request);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ConnectionRequestFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ConnectionRequestFragment extends NocturneFragment {
        private final String LOG_TAG = ConnectionRequestFragment.class.getSimpleName() + "::";
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

}
