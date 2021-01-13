package agroinfo.modelo.vo;

public class Venta {

    public enum TipoVenta {Agricultura, Ganaderia}


    private int id;
    private int cantidad;
    private double precioUnitario;
    private String descripcion;
    private String usuarioRegistrador;
    private TipoVenta tipo;

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

    public TipoVenta getTipo(){ return  tipo ;}

    public void setId(int id) {this.id = id;}

}
