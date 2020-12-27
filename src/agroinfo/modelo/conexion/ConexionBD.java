package agroinfo.modelo.conexion;

import java.sql.*;

public class ConexionBD {

    private String url;
    private String driverName;
    private String user;
    private String pass;
    private Connection connection;

    public ConexionBD() throws SQLException, ClassNotFoundException {

        this.url  = "jdbc:mysql://bj85kxmab6wnixickgpm-mysql.services.clever-cloud.com:3306/bj85kxmab6wnixickgpm";
        this.driverName = "com.mysql.jdbc.Driver";
        this.user = "ugnqzgofv28azyoj";
        this.pass = "W6ivPw6VjI2fNHD1hB66";
        this.connection= DriverManager.getConnection( url, user, pass);

        Class.forName("com.mysql.jdbc.Driver");
//Esto es de las queries
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery("select * FROM test");
        while(rs.next())
            System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
        connection.close();
    }
}
