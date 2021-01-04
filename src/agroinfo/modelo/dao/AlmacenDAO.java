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
            almacen = new Almacen();
            almacen.setConejos(rs.getInt(1));
            almacen.setPiensoLactancia(rs.getDouble(2));
            almacen.setPiensoMedicado(rs.getDouble(3));
            almacen.setPiensoRemate(rs.getDouble(4));
            almacen.setExcedenteTotal(rs.getInt(5));

    } catch (SQLException throwables) {

        throwables.printStackTrace();
    }

        this.cerrarConexion();

        return almacen;
    }
}
