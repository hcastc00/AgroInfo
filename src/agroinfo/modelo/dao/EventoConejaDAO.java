package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.EventoConeja;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventoConejaDAO extends ConexionBD {
    public EventoConejaDAO() {
    }

    public void crear(EventoConeja evento, String usuario_identificador) throws SQLException {

        this.abrirConexion();

        //Existe ID en el parametro del constructor, pero lo omitimos porque es un valor autoincremental
        String sentencia = "INSERT into evento_conejas(identificador_coneja, fecha, tipo)"
                + "VALUES (?, ?, ?)";

        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

        //El id al ser incremental, no se settea nada en el primer parametro
        pSentencia.setDouble(1, evento.getIdConeja());
        pSentencia.setDate(2, evento.getFecha());
        pSentencia.setString(3, evento.getTipoEventoConeja().toString());
        pSentencia.execute();

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha creado un evento para la coneja " + evento.getIdConeja(),
                "Creacion de evento de coneja");

        this.cerrarConexion();
    }

    public void eliminar(EventoConeja evento, String usuario_identificador) {

        this.abrirConexion();

        try {
            String sentencia = "DELETE FROM evento_conejas WHERE evento_id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, evento.getId());
            pSentencia.execute();

            RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                    "El usuario ha elininado un evento de la coneja " + evento.getIdConeja(),
                    "Eliminacion de evento de coneja");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        this.cerrarConexion();
    }

    public void modificar(EventoConeja evento, String usuario_identificador) {

        this.abrirConexion();

        String sentencia = "UPDATE evento_conejas SET " +
                " identificador_coneja = ?," +
                " fecha = ?," +
                " tipo = ?" +
                " WHERE evento_id = ?";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, evento.getIdConeja());
            pSentencia.setDate(2, evento.getFecha());
            pSentencia.setString(3, evento.getTipoEventoConeja().toString());
            pSentencia.setInt(4, evento.getId());
            pSentencia.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha modificado un evento para la coneja " + evento.getIdConeja(),
                "Modificacion de evento de coneja");

    }

    public List<EventoConeja> listar(int id) {

        this.abrirConexion();

        List<EventoConeja> lista = new ArrayList<>();

        try {

            String sentencia = "SELECT * FROM evento_conejas WHERE identificador_coneja = ?";

            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, id);
            pSentencia.execute();

            crearLista(lista, pSentencia);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public List<EventoConeja> listar(int id, EventoConeja.TipoEventoConeja tipo) {

        this.abrirConexion();

        List<EventoConeja> lista = new ArrayList<>();

        try {

            String sentencia = "SELECT * FROM evento_conejas WHERE identificador_coneja = ? AND tipo = ?";

            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, id);
            pSentencia.setString(2, tipo.toString());

            pSentencia.execute();

            crearLista(lista, pSentencia);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    private void crearLista(List<EventoConeja> lista, PreparedStatement pSentencia) {


        try {
            ResultSet rs = pSentencia.executeQuery();

            while (rs.next()) {
                EventoConeja ec = new EventoConeja(
                        rs.getInt("evento_id"),
                        rs.getInt("identificador_coneja"),
                        rs.getDate("fecha"),
                        EventoConeja.TipoEventoConeja.valueOf(rs.getString("tipo"))
                );

                lista.add(ec);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public EventoConeja buscar(int id) {
        EventoConeja ec = null;

        this.abrirConexion();

        try {
            String sentencia = "SELECT * FROM evento_conejas WHERE evento_id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setInt(1, id);
            ResultSet rs = pSentencia.executeQuery();
            rs.next();

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
