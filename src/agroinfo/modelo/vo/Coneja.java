package agroinfo.modelo.vo;

public class Coneja {

    private int id;
    private int cantidadConejos;

    public Coneja(int id, int cantidadConejos) {
        this.id = id;
        this.cantidadConejos = cantidadConejos;
    }

    public int getId() {
        return id;
    }

    public int getCantidadConejos() {
        return cantidadConejos;
    }

    public void setCantidadConejos(int cantidadConejos) {
        this.cantidadConejos = cantidadConejos;
    }
}
