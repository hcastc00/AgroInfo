package agroinfo.modelo.vo;

public class EventoConeja {
    private int id;
    private final int idConeja;
    private java.sql.Date fecha;
    private TipoEventoConeja tipoEventoConeja;
    public EventoConeja(int idConeja, java.sql.Date fecha, TipoEventoConeja tipoEventoConeja) {
        this.idConeja = idConeja;
        this.fecha = fecha;
        this.tipoEventoConeja = tipoEventoConeja;
    }

    public EventoConeja(int id, int idConeja, java.sql.Date fecha, TipoEventoConeja tipoEventoConeja) {
        this.id = id;
        this.idConeja = idConeja;
        this.fecha = fecha;
        this.tipoEventoConeja = tipoEventoConeja;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdConeja() {
        return idConeja;
    }

    public java.sql.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    public TipoEventoConeja getTipoEventoConeja() {
        return tipoEventoConeja;
    }

    public void setTipoEventoConeja(TipoEventoConeja tipoEventoConeja) {
        this.tipoEventoConeja = tipoEventoConeja;
    }

    public enum TipoEventoConeja {Inseminacion, Parto}
}
