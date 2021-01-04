package agroinfo.modelo.vo;

public class Usuario {
    public enum TipoUsuario {Administrador, Ganadero, Agricultor}

    private String nombreUsuario;
    private String contrasenya;
    private TipoUsuario tipo;

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
}
