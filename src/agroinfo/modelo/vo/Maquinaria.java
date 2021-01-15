package agroinfo.modelo.vo;

public class Maquinaria {
    private final String matricula;
    private final String nombre;

    public Maquinaria(String matricula, String nombre) {
        this.matricula = matricula;
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNombre() {
        return nombre;
    }

}
