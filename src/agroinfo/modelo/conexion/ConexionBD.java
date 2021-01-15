package agroinfo.modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private final String url;
    private final String driverName;
    private final String user;
    private final String pass;
    private Connection connection;

    public ConexionBD() {

/*        this.url = "jdbc:mysql://bj85kxmab6wnixickgpm-mysql.services.clever-cloud.com:3306/bj85kxmab6wnixickgpm";
        this.driverName = "com.mysql.jdbc.Driver";
        this.user = "ugnqzgofv28azyoj";
        this.pass = "W6ivPw6VjI2fNHD1hB66";*/

        this.url = "jdbc:mysql://lishowebproject.a2hosted.com:3306/lishoweb_agroinfo";
        this.driverName = "com.mysql.jdbc.Driver";
        this.user = "lishoweb_agroinf";
        this.pass = "agroinfo";
    }

    public String getUrl() {
        return url;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public Connection getConnection() {
        return connection;
    }

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


    public void cerrarConexion() {

        try {
            this.connection.close();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al cerrar la conexion con la base de datos: " + e);
        }
    }
}
