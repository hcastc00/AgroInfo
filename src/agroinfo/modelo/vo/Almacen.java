package agroinfo.modelo.vo;

public class Almacen {
    private double piensoLactancia;
    private double piensoMedicado;
    private double piensoRemate;
    private int conejos;
    private int excedenteTotal;

    public double getPiensoLactancia() {
        return piensoLactancia;
    }

    public void setPiensoLactancia(double piensoLactancia) {
        this.piensoLactancia = piensoLactancia;
    }

    public double getPiensoMedicado() {
        return piensoMedicado;
    }

    public void setPiensoMedicado(double piensoMedicado) {
        this.piensoMedicado = piensoMedicado;
    }

    public double getPiensoRemate() {
        return piensoRemate;
    }

    public void setPiensoRemate(double piensoRemate) {
        this.piensoRemate = piensoRemate;
    }

    public int getConejos() {
        return conejos;
    }

    public void setConejos(int conejos) {
        this.conejos = conejos;
    }

    public int getExcedenteTotal() {
        return excedenteTotal;
    }

    public void setExcedenteTotal(int excedenteTotal) {
        this.excedenteTotal = excedenteTotal;
    }
}
