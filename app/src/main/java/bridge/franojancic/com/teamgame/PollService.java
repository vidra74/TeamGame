package bridge.franojancic.com.teamgame;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by Korisnik on 26.12.2017..
 */

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    public static Intent newIntent(Context context){
        return new Intent(context, PollService.class);
    }

    public PollService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent pIntent) {
        if (isNetworkAvailableAndConnected()){
            Log.i(TAG, "Nema interneta za intent: " + pIntent);
            QueryPreferences.setTestPref(getApplicationContext(), "Nema interneta za intent: " + pIntent);
        } else {
            Log.i(TAG, "Primio intent: " + pIntent);
            QueryPreferences.setTestPref(getApplicationContext(), "Primio intent: " + pIntent);
        }
        Resources r = getResources();
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, DeviceListActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setTicker("Ticker text")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Content Title")
                .setContentText(QueryPreferences.getTestPref(getApplicationContext()))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

    public static void setServiceAlarm(Context pContext, boolean isOn){
        Intent i = PollService.newIntent(pContext);
        PendingIntent pi = PendingIntent.getService(pContext, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)pContext.getSystemService(Context.ALARM_SERVICE);

        if (isOn){
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    POLL_INTERVAL_MS,
                    pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context pContext){
        Intent i = PollService.newIntent(pContext);
        PendingIntent pi = PendingIntent.getService(pContext, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
}
