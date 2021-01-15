package agroinfo.modelo.vo;

public class Gasto {
    private int id;
    private double importe;
    private String descripcion;
    private TipoGasto tipoGasto;
    private final String usuarioRegistrador;
    public Gasto(double importe, String descripcion, TipoGasto tipoGasto, String usuarioRegistrador) {
        this.importe = importe;
        this.descripcion = descripcion;
        this.tipoGasto = tipoGasto;
        this.usuarioRegistrador = usuarioRegistrador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoGasto getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(TipoGasto tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public String getUsuarioRegistrador() {
        return usuarioRegistrador;
    }

    public void modificar(double importe, String descripcion, TipoGasto tipoGasto) {
        this.setImporte(importe);
        this.setDescripcion(descripcion);
        this.setTipoGasto(tipoGasto);
    }

    public enum TipoGasto {Agricultura, Ganaderia}
}
