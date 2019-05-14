package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jsanchezg on 24/10/2018.
 */

public class DetalleSeccion {
    String dia;
    String descripcion;
    String monto;

    public DetalleSeccion(JSONObject item) throws JSONException {
        dia=item.getString("operationDate");
        descripcion=item.getString("concept");
        monto="$ "+item.getString("amount");
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
}
