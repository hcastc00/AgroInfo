package agroinfo.modelo.dao;
import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Coneja;
import agroinfo.modelo.vo.EventoConeja;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroDAO extends ConexionBD{

    public RegistroDAO(){

    }

    public static void registrar(Connection conexion,String usuario_identificador, String mensaje, String tipo) throws SQLException {
        String sentencia = "INSERT into registro(usuario_id, fecha, tipo, mensaje) VALUES (?,NOW(),?,?)" ;
        try {
            PreparedStatement pSentencia = conexion.prepareStatement(sentencia);
            pSentencia.setString(1, usuario_identificador);
            pSentencia.setString(2, tipo);
            pSentencia.setString(3, mensaje);
            pSentencia.execute();

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<String[]> buscar(String usuario_id){
        return null;
    }

    public ArrayList<String[]> buscar(String usuario_id, String tipo){
        return null;
    }

    public ArrayList<String[]> listar(){
        return null;
    }
}
