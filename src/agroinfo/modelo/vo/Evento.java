package agroinfo.modelo.vo;

public class Evento {

    private int id;
    private String matricula = "";
    private int identificadorParcela = Integer.MIN_VALUE;
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

    //Para Maquinaria con id
    public Evento(int id, String matricula, java.sql.Date fecha, String descripcion) {
        this.id = id;
        this.matricula = matricula;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    //Para Parcela con id
    public Evento(int id, int identificadorParcela, java.sql.Date fecha, String descripcion) {
        this.id = id;
        this.identificadorParcela = identificadorParcela;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdentificadorParcela() {
        return identificadorParcela;
    }

    public String getMatricula() {
        return matricula;
    }

    public java.sql.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
