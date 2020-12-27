package agroinfo.modelo.vo;

public class Venta {
    private int id;
    private int cantidad;
    private double precioUnitario;
    private String descripcion;

    public Venta(int cantidad, double precioUnitario, String descripcion) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
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

    public String getDescripcion() {
        return descripcion;
    }
}
