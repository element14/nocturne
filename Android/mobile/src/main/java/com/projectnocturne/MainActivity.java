package com.projectnocturne;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.projectnocturne.alarmreceivers.BedAlarmReceiver;
import com.projectnocturne.datamodel.DbMetadata;
import com.projectnocturne.views.ConnectionRequestActivity;
import com.projectnocturne.views.HelpActivity;
import com.projectnocturne.views.Status1Fragment;
import com.projectnocturne.views.Welcome1Fragment;
import com.projectnocturne.views.Welcome2Fragment;


public class MainActivity extends Activity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName() + "::";

    private NocturneApplication myApp = null;
    private Status1Fragment status1Fragment = null;
    private Welcome1Fragment welcome1Fragment = null;
    private Welcome2Fragment welcome2Fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myApp = (NocturneApplication) getApplication();
        startSensorTagService();
        showScreen();
    }

    /**
     *
     */
    public void showScreen() {
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "showScreen()");
        final DbMetadata.RegistrationStatus currentRegStatus = NocturneApplication.getInstance().getDataModel()
                .getRegistrationStatus();
        switch (currentRegStatus) {
            case NOT_STARTED:
                welcome1Fragment = new Welcome1Fragment();
                getFragmentManager().beginTransaction().replace(R.id.container, welcome1Fragment).commit();
                break;
//            case REQUEST_SENT:
//                welcome2Fragment = new Welcome2Fragment();
//                getFragmentManager().beginTransaction().replace(R.id.container, welcome2Fragment).commit();
//                break;
            case REQUEST_ACCEPTED:
                status1Fragment = new Status1Fragment();
                getFragmentManager().beginTransaction().replace(R.id.container, status1Fragment).commit();
                break;
            case REQUEST_DENIED:
                // FIXME : ????
                break;
        }
    }

    private void startSensorTagService() {
        NocturneApplication.logMessage(Log.INFO, LOG_TAG
                + "startSensorTagService() starting sensor tag polling service.");

        // Set the alarm here.
        final AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final Intent alrmIntent = new Intent(this, BedAlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alrmIntent, 0);

        final long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES / 30;
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "startSensorTagService() setting alarm for [" + interval
                / 1000 + "] seconds.");

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, interval, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                final Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.action_connect:
                final Intent cnctReq = new Intent(this, ConnectionRequestActivity.class);
                startActivity(cnctReq);
                break;
            case R.id.action_help:
                final Intent help = new Intent(this, HelpActivity.class);
                startActivity(help);
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}
