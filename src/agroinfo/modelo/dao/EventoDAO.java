package agroinfo.modelo.dao;

import agroinfo.modelo.conexion.ConexionBD;
import agroinfo.modelo.vo.Evento;

import java.sql.SQLException;
import java.util.List;

public class EventoDAO extends ConexionBD {
    public EventoDAO(){
    }

    public void crear(Evento evento){

    }

    public void eliminar(Evento evento){

    }

    public void modificar(Evento evento){

    }

    public List<Evento> listar(){
        return null;
    }

    public List<Evento> listar(String referencia){
        return null;
    }

    public Evento buscar(int id){
        return null;
    }

    public int getSiguienteId(){
        return 0;
    }
}
