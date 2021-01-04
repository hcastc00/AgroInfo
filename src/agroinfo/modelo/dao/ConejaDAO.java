package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Coneja;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConejaDAO extends ConexionBD {

    public ConejaDAO(){
    }

    public void crear(Coneja coneja) {

        this.abrirConexion();

        try {
            String sentencia = "INSERT into conejas(identificador)" + "VALUES (?)";

            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, coneja.getId());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

    }

    public void eliminar(Coneja coneja){

        this.abrirConexion();

        try {
            String sentencia = "DELETE FROM conejas WHERE identificador = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, coneja.getId());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.cerrarConexion();
    }

    /*public void modificar(Coneja coneja, int antiguoIdentificador){

        this.abrirConexion();

        try {

            String sentencia = "UPDATE conejas SET identificador = ? WHERE identificador = ?";

            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, coneja.getId());
            pSentencia.setInt(2, antiguoIdentificador);
            pSentencia.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();
    }

     */

    public List<Coneja> listar(){

        this.abrirConexion();

        List<Coneja> lista = new ArrayList<>();

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM conejas");
            while(rs.next()){
                lista.add(new Coneja(rs.getInt(1)));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public Coneja buscar(int id){

        Coneja coneja = null;

        this.abrirConexion();

        try {
            String sentencia = "SELECT * FROM conejas WHERE identificador = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setInt(1, id);
            ResultSet rs = pSentencia.executeQuery();
            coneja = new Coneja(rs.getInt(1));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return coneja;
    }

}
