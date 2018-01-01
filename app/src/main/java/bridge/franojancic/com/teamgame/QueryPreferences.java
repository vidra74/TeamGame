package bridge.franojancic.com.teamgame;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Korisnik on 1.1.2018..
 */

public class QueryPreferences {
    private static final String TEST_PREF = "test_preference_storage";

    public static void setTestPref(Context pContext, String test){
        PreferenceManager.getDefaultSharedPreferences(pContext)
                .edit()
                .putString(TEST_PREF, test)
                .apply();
    }

    public static String getTestPref(Context pContext){
        return PreferenceManager.getDefaultSharedPreferences(pContext)
                .getString(TEST_PREF, null);
    }
}
