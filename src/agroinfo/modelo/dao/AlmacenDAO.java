package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Almacen;
import agroinfo.modelo.vo.Coneja;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlmacenDAO extends ConexionBD {

    public AlmacenDAO() {
    }

    public Almacen getAlmacen(){

        Almacen almacen = null;

        this.abrirConexion();


        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM almacen WHERE id = 1");
            while(rs.next()) {
                almacen = new Almacen();
                almacen.setConejos(rs.getInt("conejos"));
                almacen.setPiensoLactancia(rs.getDouble("pienso_lactancia"));
                almacen.setPiensoMedicado(rs.getDouble("pienso_medicado"));
                almacen.setPiensoRemate(rs.getDouble("pienso_remate"));
                almacen.setExcedenteTrigo(rs.getInt("excedente_trigo"));
                almacen.setExcedenteMaiz(rs.getInt("excedente_maiz"));
                almacen.setExcedenteRemolacha(rs.getInt("excedente_remolacha"));
                almacen.setExcedenteCebada(rs.getInt("excedente_cebada"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return almacen;
    }

    public void modificar(Almacen almacen, String usuario_identificador){

        this.abrirConexion();

        String sentencia = "UPDATE almacen SET " +
                " conejos = ?," +
                " pienso_lactancia = ?," +
                " pienso_medicado = ?," +
                " pienso_remate = ?," +
                " excedente_trigo = ?," +
                " excedente_maiz = ?," +
                " excedente_remolacha = ?," +
                " excedente_cebada = ?" +
                " WHERE id = 1";

        try {
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt   (1, almacen.getConejos());
            pSentencia.setDouble(2, almacen.getPiensoLactancia());
            pSentencia.setDouble(3, almacen.getPiensoMedicado());
            pSentencia.setDouble(4, almacen.getPiensoRemate());
            pSentencia.setInt   (5, almacen.getExcedenteTrigo());
            pSentencia.setInt   (6, almacen.getExcedenteMaiz());
            pSentencia.setInt   (7, almacen.getExcedenteRemolacha());
            pSentencia.setInt   (8, almacen.getExcedenteCebada());
            pSentencia.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RegistroDAO.registrar(this.getConnection(), usuario_identificador,
                "El usuario ha modificado elementos del almacen",
                "Modificacion del almacen");

           this.cerrarConexion();
    }
}
