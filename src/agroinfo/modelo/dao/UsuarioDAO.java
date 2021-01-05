package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Usuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends ConexionBD {

    public UsuarioDAO(){
    }

    public void crear(Usuario usuario){

        this.abrirConexion();

        try {

            String sentencia = "INSERT into usuarios (nombre_usuario, contrasenya, tipo, id_almacen)" +
                    "VALUES (?, ?, ?, 1)";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setString(1, usuario.getNombreUsuario());
            pSentencia.setString(2, usuario.getContrasenya());
            pSentencia.setString(3, usuario.getTipo().toString());
            pSentencia.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();
    }

    public void eliminar(Usuario usuario){

        this.abrirConexion();

        try {

            String sentencia = "DELETE FROM usuarios WHERE nombre_usuario = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setString(1, usuario.getNombreUsuario());
            pSentencia.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasenya){

        Usuario u = this.buscar(nombreUsuario);

        //TODO encriptacion y comparacion encriptada

        if(u == null || !u.getContrasenya().equals(contrasenya)){
            return null;
        }else {
            return u;
        }

    }

    public List<Usuario> listar(){

        List<Usuario> lista = new ArrayList<>();

        this.abrirConexion();

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM usuarios");

            while (rs.next()){
                lista.add(new Usuario(rs.getString(1),
                        rs.getString(2),
                        Usuario.TipoUsuario.valueOf(rs.getString(3))));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public Usuario buscar(String nombreUsuario){

        Usuario usuario = null;

        this.abrirConexion();

        try {
            String sentencia = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setString(1, nombreUsuario);
            ResultSet rs = pSentencia.executeQuery();
            usuario = new Usuario(rs.getString(1), rs.getString(2), Usuario.TipoUsuario.valueOf(rs.getString(3)));

        } catch (SQLException throwables) {
            return usuario;
        }

        this.cerrarConexion();

        return usuario;
    }
}
