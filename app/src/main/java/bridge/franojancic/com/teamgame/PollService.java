package bridge.franojancic.com.teamgame;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Korisnik on 26.12.2017..
 */

public class PollService extends IntentService {
    private static final String TAG = "PollService";

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
    }

    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }
}
