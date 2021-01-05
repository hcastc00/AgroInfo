package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Venta;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO extends ConexionBD {
    public VentaDAO(){
    }

    public void crear(Venta venta){
        this.abrirConexion();

        try {

            //Existe ID en el parametro del constructor, pero lo omitimos porque es un valor autoincremental
            String sentencia = "INSERT into ventas(cantidad, precio_unitario, usuario_registrador, descripcion)"
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            //El id al ser incremental, no se settea nada en el primer parametro
            pSentencia.setDouble(1, venta.getCantidad());
            pSentencia.setDouble(2, venta.getPrecioUnitario());
            pSentencia.setString(3, venta.getUsuarioRegistrador());
            pSentencia.setString(4, venta.getDescripcion());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();
    }

    public void eliminar(Venta venta){

        this.abrirConexion();

        try {
            String sentencia = "DELETE FROM ventas WHERE id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);
            pSentencia.setInt(1, venta.getId());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.cerrarConexion();
    }

    public List<Venta> listar(){

        this.abrirConexion();

        List<Venta> lista = new ArrayList<>();

        try {
            ResultSet rs = this.getConnection().createStatement().executeQuery("SELECT * FROM ventas");
            while(rs.next()){
                Venta v = new Venta(
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getString("usuario_registrador"),
                        rs.getString("descripcion")
                );

                v.setId(rs.getInt("id"));
                lista.add(v);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return lista;
    }

    public Venta buscar(int id){

        Venta v = null;

        this.abrirConexion();

        try {
            String sentencia = "SELECT * FROM ventas WHERE id = ?";
            PreparedStatement pSentencia = this.getConnection().prepareStatement(sentencia);

            pSentencia.setInt(1, id);
            ResultSet rs = pSentencia.executeQuery();
            rs.next();

            v = new Venta(
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getString("usuario_registrador"),
                    rs.getString("descripcion")
            );

            v.setId(rs.getInt("id"));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.cerrarConexion();

        return v;
    }
}
