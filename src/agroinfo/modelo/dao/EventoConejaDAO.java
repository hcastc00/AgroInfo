package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.EventoConeja;
import agroinfo.modelo.vo.Gasto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventoConejaDAO extends ConexionBD {
    public EventoConejaDAO(){
    }

    public void crear(EventoConeja evento){

        this.abrirConexion();

        try {

            //Existe ID en el parametro del constructor, pero lo omitimos porque es un valor autoincremental
            String sentencia = "INSERT into evento_conejas(identificador_coneja, fecha, tipo)"
                    + "VALUES (?, ?, ?)";

            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            //El id al ser incremental, no se settea nada en el primer parametro
            pSentencia.setDouble(1, evento.getIdConeja());
            pSentencia.setDate  (2, (Date) evento.getFecha());
            pSentencia.setString(3, evento.getTipoEventoConeja().toString());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();
    }

    public void eliminar(EventoConeja evento){

        this.abrirConexion();

        try {
            String sentencia = "DELETE FROM evento_conejas WHERE evento_id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, evento.getId());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.cerrarConexion();
    }

    public void modificar(EventoConeja evento){

        this.abrirConexion();

        try {

            String sentencia = "UPDATE evento_conejas SET " +
                    " identificador_coneja = ?," +
                    " fecha = ?," +
                    " tipo = ?" +
                    " WHERE evento_id = ?";

            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt   (1, evento.getIdConeja());
            pSentencia.setDate  (2, (Date)evento.getFecha()) ;
            pSentencia.setString(3, evento.getTipoEventoConeja().toString());
            pSentencia.setInt   (4, evento.getId());

            pSentencia.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public List<EventoConeja> listar(){

        this.abrirConexion();

        List<EventoConeja> lista = new ArrayList<>();

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM gastos");
            while(rs.next()){
                EventoConeja ec = new EventoConeja(
                        rs.getInt("identificador_coneja"),
                        rs.getDate("fecha"),
                        EventoConeja.TipoEventoConeja.valueOf(rs.getString("tipo"))
                );

                ec.setId(rs.getInt("id"));
                lista.add(ec);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public EventoConeja buscar(int id){
        EventoConeja ec = null;

        this.abrirConexion();

        try {
            String sentencia = "SELECT * FROM evento_conejas WHERE evento_id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setInt(1, id);
            ResultSet rs = pSentencia.executeQuery();
            ec = new EventoConeja(
                    rs.getInt("identificador_coneja"),
                    rs.getDate("fecha"),
                    EventoConeja.TipoEventoConeja.valueOf(rs.getString("tipo"))
            );
            ec.setId(rs.getInt("id"));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return ec;
    }

}
