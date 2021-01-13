package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Parcela;
import agroinfo.modelo.vo.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParcelaDAO extends ConexionBD {
    public ParcelaDAO(){
    }


    public void crear(Parcela parcela, String usuario_identificador) throws SQLException {

        this.abrirConexion();

        String sentencia = "INSERT into parcelas (identificador, latitud, longitud, tamanyo, produccion, excedente," +
                " tipo_parcela, tipo_cultivo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

        pSentencia.setInt   (1, parcela.getId());
        pSentencia.setDouble(2, parcela.getLatitud());
        pSentencia.setDouble(3, parcela.getLongitud());
        pSentencia.setDouble(4, parcela.getTam());
        pSentencia.setDouble(5, parcela.getProduccion());
        pSentencia.setDouble(6, parcela.getExcedente());
        pSentencia.setString(7, parcela.getTipoParcela().toString());
        pSentencia.setString(8, parcela.getTipoCultivo().toString());
        pSentencia.execute();

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha dado de alta una nueva parcela con id: " + parcela.getId(),
                "Creacion de parcela");

        this.cerrarConexion();
    }

    public void modificar(Parcela parcela, String usuario_identificador) {

        this.abrirConexion();

        String sentencia = "UPDATE parcelas SET "+
                "latitud    = ?," +
                "longitud       = ?, " +
                "tamanyo        = ?, " +
                "produccion     = ?, " +
                "excedente      = ?, " +
                "tipo_parcela   = ?, " +
                "tipo_cultivo   = ? " +
                "WHERE identificador = ?";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setDouble(1, parcela.getLatitud());
            pSentencia.setDouble(2, parcela.getLongitud());
            pSentencia.setDouble(3, parcela.getTam());
            pSentencia.setDouble(4, parcela.getProduccion());
            pSentencia.setDouble(5, parcela.getExcedente());
            pSentencia.setString(6, parcela.getTipoParcela().toString());
            pSentencia.setString(7, parcela.getTipoCultivo().toString());
            pSentencia.setInt   (8, parcela.getId());
            pSentencia.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha dado modificado la parcela con id: " + parcela.getId(),
                "Modificacion de parcela");

        this.cerrarConexion();

    }

    public void eliminar(int id, String usuario_identificador) {

        this.abrirConexion();

        String sentencia = "DELETE FROM parcelas WHERE identificador = ?";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setInt(1, id);
            pSentencia.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha dado de baja la parcela con id: " + id,
                "Eliminacion de parcela");

        this.cerrarConexion();
    }

    public List<Parcela> listar() {

        List<Parcela> lista = new ArrayList<>();

        this.abrirConexion();

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM parcelas");

            while (rs.next()){
                Parcela p = new Parcela(rs.getInt("identificador"),
                        rs.getDouble("latitud"),
                        rs.getDouble("longitud"),
                        rs.getDouble("tamanyo"),
                        Parcela.TipoParcela.valueOf(rs.getString("tipo_parcela")),
                        Parcela.TipoCultivo.valueOf(rs.getString("tipo_cultivo")));

                p.setProduccion(rs.getDouble("produccion"));

                lista.add(p);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public Parcela buscar(int id) {

        Parcela parcela = null;

        this.abrirConexion();

        String sentencia = "SELECT * FROM parcelas WHERE identificador = ?";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setInt(1, id);
            ResultSet rs = pSentencia.executeQuery();
            rs.next();

            parcela = new Parcela(rs.getInt("identificador"),
                    rs.getDouble("latitud"),
                    rs.getDouble("longitud"),
                    rs.getDouble("tamanyo"),
                    Parcela.TipoParcela.valueOf(rs.getString("tipo_parcela")),
                    Parcela.TipoCultivo.valueOf(rs.getString("tipo_cultivo")));

            parcela.setProduccion(rs.getDouble("produccion"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return parcela;
    }

}
