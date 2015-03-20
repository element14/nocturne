package com.projectnocturne.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.projectnocturne.R;

public class ConnectToUserActivity extends Activity implements ConnectToUserFragment.OnUsersConnectedListener {
    private final String LOG_TAG = ConnectToUserActivity.class.getSimpleName() + "::";

    private ConnectToUserFragment cnnctFrgmnt = new ConnectToUserFragment();

    public void usersConnected(){
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_user);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, cnnctFrgmnt).commit();
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

}
