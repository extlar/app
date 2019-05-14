package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.dbs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by El Tona on 22/01/2018.
 */

public abstract class DataBaseManagerInfoServicos {
    private DBHelperInfoServicios helper;
    private SQLiteDatabase db;

    public DataBaseManagerInfoServicos(Context ctx) {
        helper = new DBHelperInfoServicios(ctx);
        db = helper.getWritableDatabase();
    }

    public DataBaseManagerInfoServicos() {

    }

    public void cerrarDB(){
        db.close();
    }

    abstract void insertar_nuevo_servicio(String id, String servicio, String sku, String transtype, String serviceId,
                                          int pagosParciales, int pagosVencidos, int minimo, int maximo,
                                          int conLector, int conReferencia, String tipoReferencia, int multipleReferencia,
                                          int referenciaLenght, int topeReferenciaLenght, float comision, float ivaComision,
                                          float totalComision, String leyenda);

    abstract public void eliminar(String id);
    abstract public void eliminarTODO();
    abstract public Cursor cargarCursor();
    //abstract public Cursor compruebaServicio(String servicio);

    public DBHelperInfoServicios getHelper() {
        return helper;
    }

    public void setHelper(DBHelperInfoServicios helper) {
        this.helper = helper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }
}
