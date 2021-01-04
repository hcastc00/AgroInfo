package agroinfo.modelo.vo;

public class Parcela {
    public enum TipoParcela {Secano, Regadio}
    public enum TipoCultivo {Barbecho, Trigo, Cebada, Maiz, Remolacha}

    private int id;
    private double longitud;
    private double latitud;
    private double tam;
    private TipoParcela tipoParcela;
    private TipoCultivo tipoCultivo;
    private double produccion;
    private double excedente;

    //CONTRUCTOR
    public Parcela(int id, double longitud, double latitud, double tam, TipoParcela tipoParcela, TipoCultivo tipoCultivo) {
        this.id = id;
        this.longitud = longitud;
        this.latitud = latitud;
        this.tam = tam;
        this.tipoParcela = tipoParcela;
        this.tipoCultivo = tipoCultivo;
        this.produccion = 0;
        this.excedente = 0;
    }

    //TODO implementado este constructor para facilitar el listar de ParcelaDAO
    public Parcela(int id, double longitud, double latitud, double tam, double produccion, double excedente, TipoParcela tipoParcela, TipoCultivo tipoCultivo) {
        this.id = id;
        this.longitud = longitud;
        this.latitud = latitud;
        this.tam = tam;
        this.tipoParcela = tipoParcela;
        this.tipoCultivo = tipoCultivo;
        this.produccion = produccion;
        this.excedente = excedente;
    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getTam() {
        return tam;
    }

    public TipoParcela getTipoParcela() {
        return tipoParcela;
    }

    public TipoCultivo getTipoCultivo() {
        return tipoCultivo;
    }

    public double getProduccion() {
        return produccion;
    }

    public double getExcedente() {
        return excedente;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setTam(double tam) {
        this.tam = tam;
    }

    public void setTipoParcela(TipoParcela tipoParcela) {
        this.tipoParcela = tipoParcela;
    }

    public void setTipoCultivo(TipoCultivo tipoCultivo) {
        this.tipoCultivo = tipoCultivo;
    }

    public void setProduccion(double produccion) {
        this.produccion = produccion;
        this.excedente = 0.9 * produccion;
        //TODO aqui se calcula el excedente
    }

    public void modificar(double longitud, double latitud, double tam, TipoParcela tipoParcela, TipoCultivo tipoCultivo){
        this.setLongitud(longitud);
        this.setLatitud(latitud);
        this.setTam(tam);
        this.setTipoParcela(tipoParcela);
        this.setTipoCultivo(tipoCultivo);
    }
}

