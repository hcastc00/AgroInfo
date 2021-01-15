package agroinfo.modelo.vo;

public class Parcela {
    private final int id;
    private double longitud;
    private double latitud;
    private double tam;
    private TipoParcela tipoParcela;
    private TipoCultivo tipoCultivo;
    private double produccion;
    private double excedente;
    //CONTRUCTOR
    public Parcela(int id, double latitud, double longitud, double tam, TipoParcela tipoParcela, TipoCultivo tipoCultivo,
                   double produccion) {
        this.id = id;
        this.longitud = longitud;
        this.latitud = latitud;
        this.tam = tam;
        this.tipoParcela = tipoParcela;
        this.tipoCultivo = tipoCultivo;
        this.produccion = produccion;
        this.excedente = 0.9 * produccion;
    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getTam() {
        return tam;
    }

    public void setTam(double tam) {
        this.tam = tam;
    }

    public TipoParcela getTipoParcela() {
        return tipoParcela;
    }

    public void setTipoParcela(TipoParcela tipoParcela) {
        this.tipoParcela = tipoParcela;
    }

    public TipoCultivo getTipoCultivo() {
        return tipoCultivo;
    }

    public void setTipoCultivo(TipoCultivo tipoCultivo) {
        this.tipoCultivo = tipoCultivo;
    }

    public double getProduccion() {
        return produccion;
    }

    public void setProduccion(double produccion) {
        this.produccion = produccion;
        this.excedente = 0.9 * produccion;
        //TODO aqui se calcula el excedente
    }

    public double getExcedente() {
        return excedente;
    }

    public void modificar(double longitud, double latitud, double tam, TipoParcela tipoParcela, TipoCultivo tipoCultivo) {
        this.setLongitud(longitud);
        this.setLatitud(latitud);
        this.setTam(tam);
        this.setTipoParcela(tipoParcela);
        this.setTipoCultivo(tipoCultivo);
    }

    public enum TipoParcela {Secano, Regadio}

    public enum TipoCultivo {Barbecho, Trigo, Cebada, Maiz, Remolacha}
}

