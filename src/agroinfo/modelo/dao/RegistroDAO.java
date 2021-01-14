package agroinfo.modelo.dao;
import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Coneja;
import agroinfo.modelo.vo.EventoConeja;
import agroinfo.modelo.vo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroDAO extends ConexionBD{

    public RegistroDAO(){

    }

    public static void registrar(Connection conexion,String usuario_identificador, String mensaje, String tipo) {
        String sentencia = "INSERT into registro(usuario_id, fecha, tipo, mensaje) VALUES (?,NOW(),?,?)" ;

        try {
            PreparedStatement pSentencia = conexion.prepareStatement(sentencia);
            pSentencia.setString(1, usuario_identificador);
            pSentencia.setString(2, tipo);
            pSentencia.setString(3, mensaje);
            pSentencia.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<String[]> listar(){

        ArrayList<String[]> lista = new ArrayList<>();

        this.abrirConexion();

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * " +
                    ",CONVERT_TZ(fecha,'EST','CET') as fechaC FROM registro");

            String[] a;

            while (rs.next()){

                a = new String[4];

                a[0] = rs.getString("usuario_id");
                a[1] = String.valueOf(rs.getTimestamp("fechaC"));
                a[2] = rs.getString("tipo");
                a[3] = rs.getString("mensaje");

                lista.add(a);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }
}
