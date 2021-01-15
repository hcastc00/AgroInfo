package agroinfo.modelo.vo;

public class Venta {

    private int id;
    private final int cantidad;
    private final double precioUnitario;
    private final String descripcion;
    private final String usuarioRegistrador;
    private final TipoVenta tipo;
    public Venta(int cantidad, double precioUnitario, String usuarioRegistrador, String descripcion, TipoVenta tipo) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.usuarioRegistrador = usuarioRegistrador;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public String getUsuarioRegistrador() {
        return usuarioRegistrador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoVenta getTipo() {
        return tipo;
    }

    public enum TipoVenta {Agricultura, Ganaderia}

}
