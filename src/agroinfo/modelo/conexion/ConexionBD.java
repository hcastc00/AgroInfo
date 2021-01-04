package agroinfo.modelo.conexion;

import java.sql.*;

public class ConexionBD {

    private String url;
    private String driverName;
    private String user;
    private String pass;
    private Connection connection;

    public ConexionBD(){

        this.url = "jdbc:mysql://bj85kxmab6wnixickgpm-mysql.services.clever-cloud.com:3306/bj85kxmab6wnixickgpm";
        this.driverName = "com.mysql.jdbc.Driver";
        this.user = "ugnqzgofv28azyoj";
        this.pass = "W6ivPw6VjI2fNHD1hB66";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    //TODO quitar los try catch y propagar la excepcion para que se controle en la vista
    public void abrirConexion() {

        if (this.url.isBlank() || this.driverName.isBlank() || this.user.isBlank() || this.pass.isBlank()) {

            System.out.println("Al menos uno de los valores de la base de datos esta en blanco, asegurese de que ");
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al abrir la conexion con la base de datos: " + e);
        }
    }


    public void cerrarConexion(){

        try {
            this.connection.close();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al cerrar la conexion con la base de datos: " + e);
        }
    }
}
