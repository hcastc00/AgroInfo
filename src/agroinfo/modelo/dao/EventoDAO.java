package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Coneja;
import agroinfo.modelo.vo.Evento;
import agroinfo.modelo.vo.EventoConeja;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO extends ConexionBD {
    public EventoDAO(){
    }

    public void crear(Evento evento){

        this.abrirConexion();

        try {

            //Si el evento no tiene matrícula, el evento es de Parcela
            if(evento.getMatricula() == null){
                //Existe ID en el parametro del constructor, pero lo omitimos porque es un valor autoincremental
                String sentencia = "INSERT into eventos(identificador_parcela, fecha, descripcion) "
                        + "VALUES (?, ?, ?)";

                PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

                //El id al ser incremental, no se settea nada en el primer parametro
                pSentencia.setInt(1, evento.getIdentificadorParcela());
                pSentencia.setDate  (2, (Date) evento.getFecha());
                pSentencia.setString(3, evento.getDescripcion());

            //Si el evento tiene matricula, el evento es de Maquinaria
            }else{
                //Existe ID en el parametro del constructor, pero lo omitimos porque es un valor autoincremental
                String sentencia = "INSERT into eventos(matricula, fecha, descripcion) "
                        + "VALUES (?, ?, ?)";

                PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

                //El id al ser incremental, no se settea nada en el primer parametro
                pSentencia.setString(1, evento.getMatricula());
                pSentencia.setDate  (2, (Date) evento.getFecha());
                pSentencia.setString(3, evento.getDescripcion());

            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();
    }


    public void eliminar(Evento evento){

        this.abrirConexion();

        try {
            String sentencia = "DELETE FROM eventos WHERE evento_id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, evento.getId());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.cerrarConexion();
    }

    public void modificar(Evento evento){

        this.abrirConexion();

       try {
           //Si el evento no tiene matrícula, el evento es de Parcela
           if(evento.getMatricula() == null) {
               String sentencia = "UPDATE eventos SET " +
                       " identificador_parcela = ?," +
                       " fecha = ?," +
                       " descripcion = ?" +
                       " WHERE evento_id = ?";

               PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
               pSentencia.setInt   (1, evento.getIdentificadorParcela());
               pSentencia.setDate  (2, (Date) evento.getFecha());
               pSentencia.setString(3, evento.getDescripcion());
               pSentencia.setInt   (4, evento.getId());

               pSentencia.executeUpdate();

            //Si el evento tiene matricula, el evento es de maquinaria
            }else{

               String sentencia = "UPDATE eventos SET " +
                       " matricula = ?," +
                       " fecha = ?," +
                       " descripcion = ?" +
                       " WHERE evento_id = ?";

               PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
               pSentencia.setString(1, evento.getMatricula());
               pSentencia.setDate  (2, (Date) evento.getFecha());
               pSentencia.setString(3, evento.getDescripcion());
               pSentencia.setInt   (4, evento.getId());

               pSentencia.executeUpdate();

           }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Evento> listarEventosMaquinaria(String matricula){

        this.abrirConexion();

        List<Evento> lista = new ArrayList<>();

        try {
            String sentencia = "SELECT * FROM eventos WHERE matricula = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setString(1, matricula);
            ResultSet rs = pSentencia.executeQuery();
            while(rs.next()){
                lista.add(new Evento(rs.getString("matricula"),
                        rs.getDate("fecha"),
                        rs.getString("descripcion"))
                        );
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public List<Evento> listarEventosParcela(int identificadorParcela){

        this.abrirConexion();

        List<Evento> lista = new ArrayList<>();

        try {
            String sentencia = "SELECT * FROM eventos WHERE identificador_parcela = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, identificadorParcela);
            ResultSet rs = pSentencia.executeQuery();
            while(rs.next()){
                lista.add(new Evento(rs.getString("identificador_parcela"),
                        rs.getDate("fecha"),
                        rs.getString("descripcion"))
                );
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public Evento buscar(int id){

        Evento e = null;

        this.abrirConexion();

        try {
            String sentencia = "SELECT * FROM eventos WHERE evento_id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, id);
            ResultSet rs = pSentencia.executeQuery();

            //Si no tiene matricula, el evento es de Parcela
            if(rs.getString("matricula") == null){
                e = new Evento(rs.getInt("identificadorParcela"),
                        rs.getDate("fecha"),
                        rs.getString("descripcion"));
            }

            e.setId(rs.getInt("evento_id"));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return e;
    }

}
