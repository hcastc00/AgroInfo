package agroinfo.modelo.vo;

public class Usuario {
    private final String nombreUsuario;
    private final String contrasenya;
    private final TipoUsuario tipo;
    public Usuario(String nombreUsuario, String contrasenya, TipoUsuario tipo) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenya = contrasenya;
        this.tipo = tipo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public enum TipoUsuario {Administrador, Ganadero, Agricultor}
}
