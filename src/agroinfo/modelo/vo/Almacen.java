package agroinfo.modelo.vo;

public class Almacen {
    private double piensoLactancia;
    private double piensoMedicado;
    private double piensoRemate;
    private int conejos;
    private int excedente_trigo;
    private int excedente_maiz;
    private int excedente_remolacha;
    private int excedente_cebada;

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

    public int getExcedenteTrigo() {
        return excedente_trigo;
    }

    public void setExcedenteTrigo(int excedente_trigo) {
        this.excedente_trigo = excedente_trigo;
    }

    public int getExcedenteMaiz() {
        return excedente_maiz;
    }

    public void setExcedenteMaiz(int excedente_maiz) {
        this.excedente_maiz = excedente_maiz;
    }

    public int getExcedenteRemolacha() {
        return excedente_remolacha;
    }

    public void setExcedenteRemolacha(int excedente_remolacha) { this.excedente_remolacha = excedente_remolacha; }

    public int getExcedenteCebada() {
        return excedente_cebada;
    }

    public void setExcedenteCebada(int excedente_cebada) {
        this.excedente_cebada = excedente_cebada;
    }

}
