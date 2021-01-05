package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Evento;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

      /*  try {
            if(evento.getMatricula() == null) {
                String sentencia = "UPDATE eventos SET " +
                        " identificador_coneja = ?," +
                        " fecha = ?," +
                        " tipo = ?" +
                        " WHERE evento_id = ?";

                PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
                pSentencia.setInt(1, evento.getIdConeja());
                pSentencia.setDate(2, (Date) evento.getFecha());
                pSentencia.setString(3, evento.getTipoEventoConeja().toString());
                pSentencia.setInt(4, evento.getId());

                pSentencia.executeUpdate();

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

       */

    }

    public List<Evento> listar(){
        return null;
    }

    public List<Evento> listar(String referencia){
        return null;
    }

    public Evento buscar(int id){
        return null;
    }

}
