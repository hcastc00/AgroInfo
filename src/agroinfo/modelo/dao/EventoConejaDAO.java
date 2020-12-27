package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.EventoConeja;

import java.sql.SQLException;
import java.util.List;

public class EventoConejaDAO extends ConexionBD {
    public EventoConejaDAO() throws SQLException, ClassNotFoundException {
    }

    public void crear(EventoConeja evento){

    }

    public void eliminar(EventoConeja evento){

    }

    public void modificar(EventoConeja evento){

    }

    public List<EventoConeja> listar(){
        return null;
    }

    public List<EventoConeja> listar(int idConeja){
        return null;
    }

    public EventoConeja buscar(int id){
        return null;
    }

    public int getSiguienteId(){
        return 0;
    }
}
