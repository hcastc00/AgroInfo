package agroinfo.modelo.vo;

public class Venta {
    private int id;
    private int cantidad;
    private double precioUnitario;
    private String descripcion;
    private String usuarioRegistrador;

    public Venta(int cantidad, double precioUnitario, String usuarioRegistrador, String descripcion) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.usuarioRegistrador = usuarioRegistrador;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public String getUsuarioRegistrador() {return usuarioRegistrador;}

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(int id) {this.id = id;}

}
