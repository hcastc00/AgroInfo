package agroinfo.modelo.vo;

import agroinfo.modelo.dao.EventoDAO;
import java.util.Date;

public class Evento {

    private int id;
    private String matricula;
    private int identificadorParcela;
    private java.sql.Date fecha;
    private String descripcion;

    //Para Maquinaria
    public Evento(String matricula, java.sql.Date fecha, String descripcion) {
        this.matricula = matricula;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    //Para Parcela
    public Evento(int identificadorParcela, java.sql.Date fecha, String descripcion) {
        this.identificadorParcela = identificadorParcela;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public int getIdentificadorParcela() { return identificadorParcela; }

    public String getMatricula() { return matricula;}

    public java.sql.Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(int id) { this.id = id; }

    public void setFecha(java.sql.Date fecha) {this.fecha = fecha;}

    public void setDescripcion(String descripcion) { this.descripcion = descripcion;}


}
