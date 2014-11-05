package com.projectnocturne.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.projectnocturne.R;

public class ConnectToUserActivity extends Activity {
    private final String LOG_TAG = ConnectToUserActivity.class.getSimpleName() + "::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_user);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ConnectToUserFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect_to_user, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ConnectToUserFragment extends Fragment {
        private final String LOG_TAG = ConnectToUserFragment.class.getSimpleName() + "::";
        private TextView txtEmailAddr;
        private ToggleButton swtchCarer;
        private Button btnConnect;
        private boolean readyFragment;

        public ConnectToUserFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_connect_to_user, container, false);

            txtEmailAddr = (TextView) rootView.findViewById(R.id.connect_user_email);
            swtchCarer = (ToggleButton) rootView.findViewById(R.id.connect_user_switch_carer);
            btnConnect = (Button) rootView.findViewById(R.id.connect_user_button_connect);

            readyFragment = true;

            return rootView;
        }
    }
}
