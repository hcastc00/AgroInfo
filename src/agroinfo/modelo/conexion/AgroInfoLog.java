package agroinfo.modelo.conexion;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;
import java.io.*;

public class AgroInfoLog{
    Logger log;

    public Logger getAgroinfoLog(String[] args)throws IOException,SQLException{

        if(this.log == null){
            log = Logger.getLogger(AgroInfoLog.class.getName());
            PropertyConfigurator.configure("config/log4j.properties");
        }
        return  log;
    }
}
