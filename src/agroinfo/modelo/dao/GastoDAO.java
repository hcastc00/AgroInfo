package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Gasto;

import java.sql.SQLException;
import java.util.List;

public class GastoDAO extends ConexionBD {
    public GastoDAO() throws SQLException, ClassNotFoundException {
    }

    public void crear(Gasto gasto){

    }

    public void eliminar(Gasto gasto){

    }

    public void modificar(Gasto gasto){

    }

    public List<Gasto> listar(){
        return null;
    }

    public Gasto buscar(int id){
        return null;
    }

    public int getSiguienteId(){
        return 0;
    }
}
