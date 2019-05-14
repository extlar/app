package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by jsanchezg on 22/10/2018.
 */

public class TransaccionReg {
    String descripcion;
    String autorizacion;
    String hora;
    String referencia;
    String status;
    String monto;
    String fecha;
    int serviceId;

    public TransaccionReg() {
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public TransaccionReg(JSONObject tranData) throws JSONException {
        this.autorizacion="Aut. "+tranData.getJSONObject("TransactionControl").getJSONObject("AuthorizationCode").getString("TEXT");
        String[] fechaOperacion=tranData.getJSONObject("TransactionControl").getString("ExecutionDateTime").split("T");
        this.hora=fechaOperacion[1];
        this.monto="$ "+tranData.getJSONObject("PaymentDetail").getJSONObject("PaymentAmount").getString("TEXT");
        //this.descripcion="Pago Avon";

        this.serviceId=tranData.getJSONObject("Service").getInt("ID");
        this.descripcion=tranData.getJSONObject("Service").getString("Description");
        //double refValue = tranData.getJSONObject("PaymentDetail").getDouble("Reference");
        //DecimalFormat df=new DecimalFormat("0");
        //df.setMaximumFractionDigits(4);
        //this.referencia="Ref. "+df.format(refValue);
        this.referencia="Ref. "+ tranData.getJSONObject("PaymentDetail").getString("Reference");
        this.status=tranData.getString("TransactionStatus");
    }

    public TransaccionReg(String descripcion, String monto) {
        this.descripcion = descripcion;
        this.monto = monto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
