package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Maquinaria;

import java.sql.SQLException;
import java.util.List;

public class MaquinariaDAO extends ConexionBD {

    public MaquinariaDAO() throws SQLException, ClassNotFoundException {
    }

    public void crear(Maquinaria maquinaria){

    }

    public void eliminar(Maquinaria maquinaria){

    }

    public List<Maquinaria> listar(){
        return null;
    }

    public Maquinaria buscar(String matricula){
        return null;
    }
}
