package agroinfo.modelo.vo;

import agroinfo.modelo.dao.EventoConejaDAO;

import java.util.Date;

public class EventoConeja {
    public enum TipoEventoConeja {Inseminacion, Parto}

    private int id;
    private int idConeja;
    private java.sql.Date fecha;
    private TipoEventoConeja tipoEventoConeja;

    public EventoConeja(int idConeja, java.sql.Date fecha, TipoEventoConeja tipoEventoConeja) {
        this.idConeja = idConeja;
        this.fecha = fecha;
        this.tipoEventoConeja = tipoEventoConeja;
    }

    public int getId() {
        return id;
    }

    public int getIdConeja() {
        return idConeja;
    }

    public java.sql.Date getFecha() {
        return fecha;
    }

    public TipoEventoConeja getTipoEventoConeja() {
        return tipoEventoConeja;
    }

    public void setId(int id) { this.id = id; }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    public void setTipoEventoConeja(TipoEventoConeja tipoEventoConeja) {
        this.tipoEventoConeja = tipoEventoConeja;
    }
}
