package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.dbs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by El Tona on 22/01/2018.
 */

public class DBHelperInfoServicios extends SQLiteOpenHelper {
    private static final String DB_NOMBRE ="pago.servicios";
    private static int DB_SCHEME_VERSION = 2;

    public DBHelperInfoServicios(Context context) {
        super(context, DB_NOMBRE, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseInfoServicios.CREATE_TABLE);
        //db.execSQL(DataBaseInfoServicios.CARGAR_SERVICIOS);
        //db.execSQL("SELECT * FROM servicios");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVers1ion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "servicios");
        onCreate(db);

    }
    
    
}
