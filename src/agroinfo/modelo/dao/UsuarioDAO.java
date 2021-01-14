package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Usuario;

import org.apache.commons.codec.digest.DigestUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends ConexionBD {

    public UsuarioDAO(){
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasenya) {

        Usuario u = this.buscar(nombreUsuario);

        //if(u == null || !u.getContrasenya().equals(DigestUtils.md5Hex(contrasenya))){
        if(u == null || !u.getContrasenya().equals(contrasenya)){
            return null;
        }else {

            this.abrirConexion();
            RegistroDAO.registrar(this.getConnection(), nombreUsuario,
                    "El usuario ha iniciado sesion",
                    "Inicio de sesion");
            this.cerrarConexion();
            return u;
        }
    }

    public void cerrarSesion(String nombreUsuario) {

        this.abrirConexion();
        RegistroDAO.registrar(this.getConnection(), nombreUsuario,
                "El usuario ha cerrado sesion",
                "Fin de sesion");
        this.cerrarConexion();
    }

    public void crear(Usuario usuario,String usuario_identificador) throws SQLException {

        this.abrirConexion();

        String sentencia = "INSERT into usuarios (nombre_usuario, contrasenya, tipo, id_almacen)" +
                "VALUES (?, ?, ?, 1)";
        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

        pSentencia.setString(1, usuario.getNombreUsuario());
        pSentencia.setString(2, DigestUtils.md5Hex(usuario.getContrasenya()));
        pSentencia.setString(3, usuario.getTipo().toString());
        pSentencia.execute();

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha creado un nuevo usuario",
                "Creacion de usuario");

        this.cerrarConexion();
    }

    public void eliminar(String nombre_usuario, String usuario_identificador) {

        this.abrirConexion();

        String sentencia = "DELETE FROM usuarios WHERE nombre_usuario = ?";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setString(1, nombre_usuario);
            pSentencia.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha eliminado un usuario",
                "Eliminacion de usuario");

        this.cerrarConexion();
    }

    public ArrayList<String[]> listar(){

        ArrayList<String[]> lista = new ArrayList<>();

        this.abrirConexion();

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement("SELECT MAX(fecha) FROM registro " +
                    "GROUP BY usuario_id ORDER BY usuario_id");
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM usuarios " +
                    "ORDER BY nombre_usuario");
            ResultSet rs1 = pSentencia.executeQuery();

            String[] a;

            while (rs.next()){

                a = new String[5];

                a[0] = rs.getString("nombre_usuario");
                a[1] = rs.getString("contrasenya");
                a[2] = rs.getString("tipo");
                a[3] = rs.getString("id_almacen");
                a[4] = String.valueOf(rs1.getTimestamp("fecha"));

                lista.add(a);
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public Usuario buscar(String nombreUsuario) {

        Usuario usuario = null;

        this.abrirConexion();

        String sentencia = "SELECT * FROM usuarios WHERE nombre_usuario = ?";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setString(1, nombreUsuario);
            ResultSet rs = pSentencia.executeQuery();

            if (rs.next())
                usuario = new Usuario(rs.getString(1), rs.getString(2), Usuario.TipoUsuario.valueOf(rs.getString(3)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return usuario;
    }
}
