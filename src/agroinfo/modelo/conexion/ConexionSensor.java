package agroinfo.modelo.conexion;

import java.util.Random;

public class ConexionSensor {
    public double getTemperatura(){
        //Simulamos la conexion con el sensor
        double max = 45;
        double min = 0;
        Random random = new Random();

        return min + (max - min) * random.nextDouble();
    }
}
