package agroinfo.modelo.vo;

public class Almacen {
    private double piensoLactancia;
    private double piensoMedicado;
    private double piensoRemate;
    private int conejos;
    private final int excedente_trigo;
    private final int excedente_maiz;
    private final int excedente_remolacha;
    private final int excedente_cebada;

    public Almacen(int conejos, double piensoLactancia, double piensoMedicado, double piensoRemate, int excedente_trigo, int excedente_maiz, int excedente_remolacha, int excedente_cebada) {
        this.piensoLactancia = piensoLactancia;
        this.piensoMedicado = piensoMedicado;
        this.piensoRemate = piensoRemate;
        this.conejos = conejos;
        this.excedente_trigo = excedente_trigo;
        this.excedente_maiz = excedente_maiz;
        this.excedente_remolacha = excedente_remolacha;
        this.excedente_cebada = excedente_cebada;
    }

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

    public int getExcedenteMaiz() {
        return excedente_maiz;
    }

    public int getExcedenteRemolacha() {
        return excedente_remolacha;
    }

    public int getExcedenteCebada() {
        return excedente_cebada;
    }


    //Falta por colocar y comentar

    /*
    public void sumarConejos(int conejos){
        this.conejos += conejos;
    }

    public void restarConejos(int conejos){
        this.conejos -= conejos;
    }

    public void sumarPiensoLactancia(double piensoLactancia){
        this.piensoLactancia += piensoLactancia;
    }

    public void restarPiensoLactancia(double piensoLactancia){
        this.piensoLactancia -= piensoLactancia;
    }

    public void sumarPiensoMedicado(double piensoMedicado){
        this.piensoMedicado += piensoMedicado;
    }

    public void restarPiensoMedicado(double piensoMedicado){
        this.piensoMedicado -= piensoMedicado;
    }

    public void sumarPiensoRemate(double piensoRemate){
        this.piensoRemate += piensoRemate;
    }

    public void restarPiensoRemate(double piensoRemate){
        this.piensoRemate -= piensoRemate;
    }


     */


}
