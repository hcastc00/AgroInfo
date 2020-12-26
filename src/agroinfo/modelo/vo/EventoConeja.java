package agroinfo.modelo.vo;

import java.util.Date;

public class EventoConeja {
    public enum TipoEventoConeja {Inseminacion, Parto};

    private int id;
    private int idConeja;
    private Date fecha;
    private TipoEventoConeja tipoEventoConeja;

    public EventoConeja(int idConeja, Date fecha, TipoEventoConeja tipoEventoConeja) {
        //this.id = TODO getNextId()
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

    public Date getFecha() {
        return fecha;
    }

    public TipoEventoConeja getTipoEventoConeja() {
        return tipoEventoConeja;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setTipoEventoConeja(TipoEventoConeja tipoEventoConeja) {
        this.tipoEventoConeja = tipoEventoConeja;
    }
}
