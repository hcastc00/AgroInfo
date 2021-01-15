package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Maquinaria;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaquinariaDAO extends ConexionBD {

    public MaquinariaDAO() {
    }

    public void crear(Maquinaria maquinaria, String usuario_identificador) throws SQLException {

        this.abrirConexion();

        String sentencia = "INSERT into maquinaria (matricula, nombre)" +
                "VALUES (?, ?)";
        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

        pSentencia.setString(1, maquinaria.getMatricula());
        pSentencia.setString(2, maquinaria.getNombre());
        pSentencia.execute();
        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha dado de alta una nueva maquina con matricula: " + maquinaria.getMatricula(),
                "Creacion de maquinaria");

        this.cerrarConexion();
    }

    public void eliminar(String matricula, String usuario_identificador) {

        this.abrirConexion();

        String sentencia = "DELETE FROM maquinaria WHERE matricula = ?";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setString(1, matricula);
            pSentencia.execute();

            RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                    "El usuario ha dado de baja la maquina con matricula: " + matricula,
                    "Eliminacion de maquinaria");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();
    }

    public List<Maquinaria> listar() {

        List<Maquinaria> lista = new ArrayList<>();

        this.abrirConexion();

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM maquinaria");

            while (rs.next()) {
                lista.add(new Maquinaria(rs.getString("matricula"),
                        rs.getString("nombre")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public ArrayList<String[]> listarConEventos() {

        this.abrirConexion();

        ArrayList<String[]> lista = new ArrayList<>();

        String sentencia = "SELECT matricula, nombre, MIN(fecha) AS fecha " +
                "FROM ( " +
                "(SELECT m.matricula, nombre, fecha FROM eventos " +
                "RIGHT JOIN maquinaria m on eventos.matricula = m.matricula) " +
                "UNION\n" +
                "(SELECT m.matricula, nombre, fecha FROM eventos " +
                "LEFT JOIN maquinaria m on eventos.matricula = m.matricula " +
                ")) AS sub " +
                "WHERE matricula is not null " +
                "GROUP BY matricula " +
                "ORDER BY matricula";

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery(sentencia);

            String[] a;

            while (rs.next()) {

                a = new String[3];

                a[0] = rs.getString("matricula");
                a[1] = rs.getString("nombre");
                a[2] = String.valueOf(rs.getDate("fecha"));
                lista.add(a);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public ArrayList<String[]> listarConEventos(String matriculaPar) {

        this.abrirConexion();

        ArrayList<String[]> lista = new ArrayList<>();

        String sentencia = "SELECT matricula, nombre, fecha " +
                "FROM ( " +
                "(SELECT m.matricula, nombre, fecha FROM eventos " +
                "RIGHT JOIN maquinaria m on eventos.matricula = m.matricula " +
                "WHERE m.matricula = ?) " +
                "UNION\n" +
                "(SELECT m.matricula, nombre, MIN(fecha) AS fecha FROM eventos " +
                "LEFT JOIN maquinaria m on eventos.matricula = m.matricula " +
                ")) AS sub " +
                "GROUP BY matricula " +
                "ORDER BY matricula";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setString(1, matriculaPar);
            ResultSet rs = pSentencia.executeQuery();

            String[] a;

            while (rs.next()) {

                a = new String[3];

                a[0] = rs.getString("matricula");
                a[1] = rs.getString("nombre");
                a[2] = String.valueOf(rs.getDate("fecha"));
                lista.add(a);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }


    public Maquinaria buscar(String matricula) throws SQLException {

        Maquinaria maquinaria = null;

        this.abrirConexion();

        String sentencia = "SELECT * FROM maquinaria WHERE matricula = ?";
        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

        ResultSet rs = pSentencia.executeQuery();
        rs.next();

        maquinaria = new Maquinaria(rs.getString("matricula"), rs.getString("nombre"));

        this.cerrarConexion();

        return maquinaria;
    }
}
