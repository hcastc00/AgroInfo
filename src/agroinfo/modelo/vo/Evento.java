package agroinfo.modelo.vo;

import java.util.Date;

public class Evento {

    private int id;
    private String referencia;
    private Date fecha;
    private String descripcion;

    //Para Maquinaria
    public Evento(String referencia, Date fecha, String descripcion) {
        this.referencia = referencia;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    //Para Parcela
    public Evento(int referencia, Date fecha, String descripcion) {
        this.referencia = ((Integer)referencia).toString();
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getReferencia() {
        return referencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
