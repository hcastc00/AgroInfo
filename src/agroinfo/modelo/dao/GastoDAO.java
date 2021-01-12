package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Gasto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GastoDAO extends ConexionBD {
    public GastoDAO(){
    }

    public void crear(Gasto gasto, String usuario_identificador) throws SQLException {
        this.abrirConexion();

        //Existe ID en el parametro del constructor, pero lo omitimos porque es un valor autoincremental
        String sentencia = "INSERT into gastos(importe, descripcion, tipo, usuario_registrador)"
                + "VALUES (?, ?, ?, ?)";

        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

        //El id al ser incremental, no se settea nada en el primer parametro
        pSentencia.setDouble(1, gasto.getImporte());
        pSentencia.setString(2, gasto.getDescripcion());
        pSentencia.setString(3, gasto.getTipoGasto().toString());
        pSentencia.setString (4, gasto.getUsuarioRegistrador());
        pSentencia.execute();

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha registrado gasto",
                "Creacion de registro de gasto");


        this.cerrarConexion();
    }

    public void eliminar(Gasto gasto, String usuario_identificador) throws SQLException {

        this.abrirConexion();

        String sentencia = "DELETE FROM gastos WHERE id = ?";
        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
        pSentencia.setInt(1, gasto.getId());
        pSentencia.execute();

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha eliminado un registro de gasto",
                "Eliminacion de registro de gasto");

        this.cerrarConexion();
    }

    public void modificar(Gasto gasto, String usuario_identificador) throws SQLException {

        this.abrirConexion();

        String sentencia = "UPDATE gastos SET " +
                " importe = ?," +
                " descripcion = ?," +
                " tipo = ?" +
                " WHERE id = ?";

        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
        pSentencia.setDouble (1, gasto.getImporte());
        pSentencia.setString(2, gasto.getDescripcion());
        pSentencia.setString(3, gasto.getTipoGasto().toString());
        pSentencia.setInt(4, gasto.getId());
        pSentencia.executeUpdate();

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha modificado un registro de gasto",
                "Modificacion de registro de gasto");


        this.cerrarConexion();
    }

    public List<Gasto> listar(){

        this.abrirConexion();

        List<Gasto> lista = new ArrayList<>();

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM gastos");
            while(rs.next()){
                Gasto g = new Gasto(
                        rs.getInt("importe"),
                        rs.getString("descripcion"),
                        Gasto.TipoGasto.valueOf(rs.getString("tipo")),
                        rs.getString("usuario_registrador")
                );

                g.setId(rs.getInt("id"));
                lista.add(g);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public Gasto buscar(int id){

        Gasto g = null;

        this.abrirConexion();

        try {
            String sentencia = "SELECT * FROM gastos WHERE id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setInt(1, id);
            ResultSet rs = pSentencia.executeQuery();
            rs.next();

            g = new Gasto(
                    rs.getInt("importe"),
                    rs.getString("descripcion"),
                    Gasto.TipoGasto.valueOf(rs.getString("tipo")),
                    rs.getString("usuario_registrador")
            );

            g.setId(rs.getInt("id"));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return g;
    }


}
