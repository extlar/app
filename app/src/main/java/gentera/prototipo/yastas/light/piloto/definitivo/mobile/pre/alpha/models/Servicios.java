package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.models;

/**
 * Created by Tonatiuh on 17/07/2017.
 */

public class Servicios {
    private String id, servicio, sku, transtype, serviceId, tipoReferencia, leyenda;
    private int minimo, maximo, multipleReferencia, referenciaLenght, topeReferenciaLenght, pagosParciales, pagosVencidos, conLector, conReferencia;
    private float comision, ivaComision, totalComision;

    public Servicios() {

    }

    public Servicios(String id, String servicio, String sku, String transtype, String serviceId, int pagosParciales,
                     int pagosVencidos, int minimo, int maximo, int conLector, int conReferencia, String tipoReferencia,
                     int multipleReferencia, int referenciaLenght, int topeReferenciaLenght, float comision, float ivaComision,
                     float totalComision, String leyenda) {
        this.id = id;
        this.servicio = servicio;
        this.sku = sku;
        this.transtype = transtype;
        this.serviceId = serviceId;
        this.pagosParciales = pagosParciales;
        this.pagosVencidos = pagosVencidos;
        this.minimo = minimo;
        this.maximo = maximo;
        this.conLector = conLector;
        this.conReferencia = conReferencia;
        this.tipoReferencia = tipoReferencia;
        this.multipleReferencia = multipleReferencia;
        this.referenciaLenght = referenciaLenght;
        this.topeReferenciaLenght = topeReferenciaLenght;
        this.comision = comision;
        this.ivaComision = ivaComision;
        this.totalComision = totalComision;
        this.leyenda = leyenda;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTranstype() {
        return transtype;
    }

    public void setTranstype(String transtype) {
        this.transtype = transtype;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTipoReferencia() {
        return tipoReferencia;
    }

    public void setTipoReferencia(String tipoReferencia) {
        this.tipoReferencia = tipoReferencia;
    }

    public String getLeyenda() {
        return leyenda;
    }

    public void setLeyenda(String leyenda) {
        this.leyenda = leyenda;
    }

    public int getMinimo() {
        return minimo;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }

    public int getMaximo() {
        return maximo;
    }

    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }

    public int getMultipleReferencia() {
        return multipleReferencia;
    }

    public void setMultipleReferencia(int multipleReferencia) {
        this.multipleReferencia = multipleReferencia;
    }

    public int getReferenciaLenght() {
        return referenciaLenght;
    }

    public void setReferenciaLenght(int referenciaLenght) {
        this.referenciaLenght = referenciaLenght;
    }

    public int getTopeReferenciaLenght() {
        return topeReferenciaLenght;
    }

    public void setTopeReferenciaLenght(int topeReferenciaLenght) {
        this.topeReferenciaLenght = topeReferenciaLenght;
    }

    public int getPagosParciales() {
        return pagosParciales;
    }

    public void setPagosParciales(int pagosParciales) {
        this.pagosParciales = pagosParciales;
    }

    public int getPagosVencidos() {
        return pagosVencidos;
    }

    public void setPagosVencidos(int pagosVencidos) {
        this.pagosVencidos = pagosVencidos;
    }

    public int getConLector() {
        return conLector;
    }

    public void setConLector(int conLector) {
        this.conLector = conLector;
    }

    public int getConReferencia() {
        return conReferencia;
    }

    public void setConReferencia(int conReferencia) {
        this.conReferencia = conReferencia;
    }

    public float getComision() {
        return comision;
    }

    public void setComision(float comision) {
        this.comision = comision;
    }

    public float getIvaComision() {
        return ivaComision;
    }

    public void setIvaComision(float ivaComision) {
        this.ivaComision = ivaComision;
    }

    public float getTotalComision() {
        return totalComision;
    }

    public void setTotalComision(float totalComision) {
        this.totalComision = totalComision;
    }
}