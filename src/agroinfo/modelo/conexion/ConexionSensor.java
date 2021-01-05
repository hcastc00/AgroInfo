package agroinfo.modelo.conexion;

import java.math.BigDecimal;
import java.util.Random;

public class ConexionSensor {
    public String getTemperatura(){
        //Simulamos la conexion con el sensor
        double max = 45;
        double min = 0;
        Random random = new Random();
        double temp = min + (max - min) * random.nextDouble();

        return String.format("%.1f",temp);
    }
}
