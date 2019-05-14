package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setUseName(String usename) {
        prefs.edit().putString("usename", usename).commit();
    }

    public String getUseName() {
        String usename = prefs.getString("usename","");
        return usename;
    }
}
