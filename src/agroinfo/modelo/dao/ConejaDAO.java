package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Coneja;
import agroinfo.modelo.vo.EventoConeja;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConejaDAO extends ConexionBD {

    public ConejaDAO(){
    }

    public void crear(Coneja coneja, String usuario_identificador) throws SQLException {

        this.abrirConexion();

        String sentencia = "INSERT into conejas(identificador) VALUES (?)";

        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
        pSentencia.setInt(1, coneja.getId());
        pSentencia.execute();
        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
        "El usuario ha dado de alta una nueva coneja con id: "+ coneja.getId(),
                "Creacion de coneja");

        this.cerrarConexion();

    }

    public void eliminar(int id, String usuario_identificador){

        this.abrirConexion();

        try {
            String sentencia = "DELETE FROM conejas WHERE identificador = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, id);
            pSentencia.execute();
            RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                    "El usuario ha dado de alta una nueva coneja con id: "+ id,
                    "Creacion de coneja");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        this.cerrarConexion();
    }

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

    public ArrayList<String[]> listarConEventos(){

        this.abrirConexion();

        ArrayList<String[]> lista = new ArrayList<>();


        String sentencia = "SELECT identificador, MIN(fecha) AS fecha, tipo " +
                "FROM ( " +
                "(SELECT c.identificador, fecha, tipo FROM evento_conejas " +
                "RIGHT JOIN conejas c on identificador_coneja = c.identificador) " +
                "UNION " +
                "(SELECT c.identificador, fecha, tipo FROM evento_conejas " +
                "LEFT JOIN conejas c on identificador_coneja = c.identificador " +
                ")) AS sub " +
                "GROUP BY identificador, tipo " +
                "ORDER BY identificador";

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery(sentencia);

            int idAnt = -1;
            int idNuevo;
            String[] a = new String[3];

            while (rs.next()){

                idNuevo = rs.getInt("identificador");

                if(idAnt != idNuevo ){
                    if (rs.getRow() != 1)
                        lista.add(a);
                    a = new String[3];
                    idAnt = idNuevo;
                    a[0] = String.valueOf(idNuevo);
                }

               String tipo = rs.getString("tipo");

                if (tipo!=null) {
                    if (tipo.equals(EventoConeja.TipoEventoConeja.Inseminacion.toString())) {
                        a[1] = rs.getDate("fecha").toString();
                    } else if (tipo.equals(EventoConeja.TipoEventoConeja.Parto.toString())) {
                        a[2] = rs.getDate("fecha").toString();
                    }
                }

                if(rs.isLast()){
                    lista.add(a);
                }
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
            rs.next();

            coneja = new Coneja(rs.getInt(1));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return coneja;
    }

}
