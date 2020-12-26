package agroinfo.modelo.vo;

public class Venta {
    private int id;
    private int cantidad;
    private double precioUnitario;

    public Venta(int cantidad, double precioUnitario) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
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
}
