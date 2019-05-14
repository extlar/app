package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models.Servicios;

import static gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ProcesoPagoAdapter.conLectorInt;
import static gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ProcesoPagoAdapter.longitudReferencia;
import static gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.adapters.ProcesoPagoAdapter.topeLongitudReferencia;

/**
 * Created by Qualtop on 10/02/2018.
 */

public class DataBaseInfoServicios extends DataBaseManagerInfoServicos {
    private static final String NOMBRE_TABLA = "servicios",
            COL_ID = "_id",
            COL_SERVICIO = "servicioAPagar",
            COL_SKU = "sku",
            COL_TRANSTYPE = "transtype_netpay",
            COL_SERVICEID = "service_ID",
            COL_PAGOS_PARCIALES = "acepta_pagos_parciales",
            COL_PAGOS_VENCIDOS = "acepta_pagos_vencidos",
            COL_MONTO_MINIMO = "monto_minimo",
            COL_MONTO_MAXIMO = "mono_maximo",
            COL_ACEPTA_CODIGO_DE_BARRAS = "acepta_codigo_de_barras",
            COL_ACEPTA_REFERENCIA = "acepta_referencia",
            COL_TIPO_REFERENCIA = "tipo_referencia",
            COL_MULTIPLE_REFERENCIA = "multiple_longitud_referencia",
            COL_LONGITUD_REFERENCIA = "longitud_referencia",
            COL_TOPE_REFERENCIA = "longitud_tope_referencia",
            COL_COMISION = "comision_al_usuario",
            COL_IVA_COMISION = "iva_comision",
            COL_TOTAL_COMISION = "total_comision",
            COL_Leyenda = "leyenda_aclaracion";

    public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ("
            + COL_ID + " integer PRIMARY KEY AUTOINCREMENT, "
            + COL_SERVICIO + " text NOT NULL, "
            + COL_SKU + " text NOT NULL, "
            + COL_TRANSTYPE + " text NOT NULL, "
            + COL_SERVICEID + " text NOT NULL, "
            + COL_PAGOS_PARCIALES + " integer NOT NULL, "
            + COL_PAGOS_VENCIDOS + " integer NOT NULL, "
            + COL_MONTO_MINIMO + " integer, "
            + COL_MONTO_MAXIMO + " integer, "
            + COL_ACEPTA_CODIGO_DE_BARRAS + " integer NOT NULL, "
            + COL_ACEPTA_REFERENCIA + " integer NOT NULL, "
            + COL_TIPO_REFERENCIA + " text NOT NULL, "
            + COL_MULTIPLE_REFERENCIA + " real, "
            + COL_LONGITUD_REFERENCIA + " integer NOT NULL, "
            + COL_TOPE_REFERENCIA + " integer, "
            + COL_COMISION + " real NOT NULL, "
            + COL_IVA_COMISION + " real NOT NULL, "
            + COL_TOTAL_COMISION + " real NOT NULL, "
            + COL_Leyenda + " text NOT NULL"
            + ");";

    public DataBaseInfoServicios(Context ctx) {
        super(ctx);
    }

    @Override
    public void cerrarDB(){
        super.getDb().close();
    }

    private ContentValues generarContentValues(String id, String servicio, String sku, String transtype, String serviceId,
                                               int pagosParciales, int pagosVencidos, int minimo, int maximo,
                                               int conLector, int conReferencia, String tipoReferencia, int multipleReferencia,
                                               int referenciaLenght, int topeReferenciaLenght, float comision, float ivaComision, float totalComision,
                                               String leyenda){
            ContentValues valores = new ContentValues();
            valores.put(COL_ID, id);
        valores.put(COL_SERVICIO, servicio);
        valores.put(COL_SKU, sku);
        valores.put(COL_TRANSTYPE, transtype);
        valores.put(COL_SERVICEID, serviceId);
        valores.put(COL_PAGOS_PARCIALES, pagosParciales);
        valores.put(COL_PAGOS_VENCIDOS, pagosVencidos);
        valores.put(COL_MONTO_MINIMO, minimo);
        valores.put(COL_MONTO_MAXIMO, maximo);
        valores.put(COL_ACEPTA_CODIGO_DE_BARRAS, conLector);
        valores.put(COL_ACEPTA_REFERENCIA, conReferencia);
        valores.put(COL_TIPO_REFERENCIA, tipoReferencia);
        valores.put(COL_MULTIPLE_REFERENCIA, multipleReferencia);
        valores.put(COL_LONGITUD_REFERENCIA, referenciaLenght);
        valores.put(COL_TOPE_REFERENCIA, topeReferenciaLenght);
        valores.put(COL_COMISION, comision);
        valores.put(COL_IVA_COMISION, ivaComision);
        valores.put(COL_TOTAL_COMISION, totalComision);
        valores.put(COL_Leyenda, leyenda);
        return valores;
    }

    @Override
    public void insertar_nuevo_servicio(String id, String servicio, String sku, String transtype, String serviceId,
                                            int pagosParciales, int pagosVencidos, int minimo, int maximo,
                                            int conLector, int conReferencia, String tipoReferencia, int multipleReferencia,
                                            int referenciaLenght, int topeReferenciaLenght, float comision, float ivaComision,
                                            float totalComision, String leyenda) {
        Log.d("insertar_servicio", super.getDb().insert(NOMBRE_TABLA, null, generarContentValues(id, servicio, sku, transtype, serviceId, pagosParciales, pagosVencidos, minimo, maximo, conLector, conReferencia, tipoReferencia, multipleReferencia, referenciaLenght, topeReferenciaLenght, comision, ivaComision, totalComision, leyenda)) + " ");

    }

    @Override
    public void eliminar(String id) {
        super.getDb().delete(NOMBRE_TABLA, COL_ID + "=?", new String[]{id});

    }

    @Override
    public void eliminarTODO() {
        super.getDb().execSQL("DELETE FROM " + NOMBRE_TABLA + ";");
        Log.d("eliminar_servicios", "Datos borrados");

    }

    @Override
    public Cursor cargarCursor() {
        String[] columnas = new String[]{
                COL_ID,
                COL_SERVICIO,
                COL_SKU,
                COL_TRANSTYPE,
                COL_SERVICEID,
                COL_PAGOS_PARCIALES,
                COL_PAGOS_VENCIDOS,
                COL_MONTO_MINIMO,
                COL_MONTO_MAXIMO,
                COL_ACEPTA_CODIGO_DE_BARRAS,
                COL_ACEPTA_REFERENCIA,
                COL_TIPO_REFERENCIA,
                COL_MULTIPLE_REFERENCIA,
                COL_LONGITUD_REFERENCIA,
                COL_TOPE_REFERENCIA,
                COL_COMISION,
                COL_IVA_COMISION,
                COL_TOTAL_COMISION,
                COL_Leyenda
        };
        return super.getDb().query(NOMBRE_TABLA, columnas, null, null, null, null, null);
    }

    public List<Servicios> getServiciosList(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            servicios.setId(c.getString(0));
            servicios.setServicio(c.getString(1));
            servicios.setSku(c.getString(2));
            servicios.setTranstype(c.getString(3));
            servicios.setServiceId(c.getString(4));
            servicios.setPagosParciales(c.getInt(5));
            servicios.setPagosVencidos(c.getInt(6));
            servicios.setMinimo(c.getInt(7));
            servicios.setMaximo(c.getInt(8));
            servicios.setConLector(c.getInt(9));
            servicios.setConReferencia(c.getInt(10));
            servicios.setTipoReferencia(c.getString(11));
            servicios.setMultipleReferencia(c.getInt(12));
            servicios.setReferenciaLenght(c.getInt(13));
            servicios.setTopeReferenciaLenght(c.getInt(14));
            servicios.setComision(c.getFloat(15));
            servicios.setIvaComision(c.getFloat(16));
            servicios.setTotalComision(c.getFloat(17));
            servicios.setLeyenda(c.getString(18));
            listaParametrosTransaccion.add(servicios);

        }
        return listaParametrosTransaccion;
    }

    public int getTopeReferencia(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            topeLongitudReferencia = c.getInt(14);
            listaParametrosTransaccion.add(servicios);

        }
        return topeLongitudReferencia;
    }

    public boolean getMultipleReferencia(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        boolean multipleReferencia = false;

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            conLectorInt = c.getInt(12);
            if(conLectorInt == 1){
                multipleReferencia = true;
            }
            listaParametrosTransaccion.add(servicios);

        }
        return multipleReferencia;
    }

    public String getPieTicket(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        String comision = "";

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            comision = c.getString(18);
            listaParametrosTransaccion.add(servicios);

        }
        return comision;
    }

    public String getServicio(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        String servicioAPagar = "";

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            servicioAPagar = c.getString(1);
            listaParametrosTransaccion.add(servicios);

        }
        return servicioAPagar;
    }

    public float getTotalComision(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        float comisionTotal = 0.00f;

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            comisionTotal = c.getFloat(17);
            listaParametrosTransaccion.add(servicios);

        }
        return comisionTotal;
    }

    public float getComision(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        float comision = 0.00f;

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            comision = c.getFloat(15);
            listaParametrosTransaccion.add(servicios);

        }
        return comision;
    }

    public float getIvaComision(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        float ivaComision = 0.00f;

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            ivaComision = c.getFloat(16);
            listaParametrosTransaccion.add(servicios);

        }
        return ivaComision;
    }

    public String getSku(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        String sku = "";

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            sku = c.getString(2);
            listaParametrosTransaccion.add(servicios);

        }
        return sku;
    }

    public int getServiceID(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        int serviceId = 1;

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            serviceId = c.getInt(4);
            listaParametrosTransaccion.add(servicios);

        }
        return serviceId;
    }

    public int getMontoMaximo(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        int montoMaximo = 1;

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            montoMaximo = c.getInt(8);
            listaParametrosTransaccion.add(servicios);

        }
        return montoMaximo;
    }

    public int getMontoMinimo(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        int montoMinimo = 1;

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            montoMinimo = c.getInt(7);
            listaParametrosTransaccion.add(servicios);

        }
        return montoMinimo;
    }

    public int getLongitudReferencia(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
             longitudReferencia = c.getInt(13);
            listaParametrosTransaccion.add(servicios);

        }
        return longitudReferencia;
    }

    public boolean getConLector(String servicio){
        List<Servicios> listaParametrosTransaccion = new ArrayList<>();
        String datosPago[] = {servicio};
        boolean conLector = false;

        Cursor c = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        while (c.moveToNext()){
            Servicios servicios = new Servicios();
            conLectorInt = c.getInt(9);
            if(conLectorInt == 1){
                conLector = true;
            }
            listaParametrosTransaccion.add(servicios);

        }
        return conLector;
    }

    /*
    @Override
    public Cursor compruebaServicio(String servicio) {
        String datosPago[] = {servicio};
        Cursor resultSet = super.getDb().rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + COL_SERVICIO + "= ?" , datosPago);
        //getServiciosList();
        return resultSet;

    }*/

}
