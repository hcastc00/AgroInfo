package agroinfo.modelo.vo;

public class Venta {
    private int id;
    private int cantidad;
    private double precioUnitario;
    private String descripcion;
    private String usuarioRegistrador;
    private String tipo;

    public Venta(int cantidad, double precioUnitario, String usuarioRegistrador, String descripcion, String tipo) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.usuarioRegistrador = usuarioRegistrador;
        this.descripcion = descripcion;
        this.tipo = tipo;
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

    public String getTipo(){ return  tipo ;}

    public void setId(int id) {this.id = id;}

}
