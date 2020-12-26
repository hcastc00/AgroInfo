package agroinfo.modelo.vo;

public class Gasto {
    public enum TipoGasto {Agricultura, Ganaderia}

    private int id;
    private double importe;
    private String descripcion;
    private TipoGasto tipoGasto;

    public Gasto(double importe, String descripcion, TipoGasto tipoGasto) {
        //this.id = TODO getNextId()
        this.importe = importe;
        this.descripcion = descripcion;
        this.tipoGasto = tipoGasto;
    }

    public int getId() {
        return id;
    }

    public double getImporte() {
        return importe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoGasto getTipoGasto() {
        return tipoGasto;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipoGasto(TipoGasto tipoGasto) {
        this.tipoGasto = tipoGasto;
    }
}
