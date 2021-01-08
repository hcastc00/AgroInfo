package agroinfo.vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Ventana extends Application {
    private double x,y;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        primaryStage.setTitle("AgroInfo");
        primaryStage.setScene(new Scene(root));

        //Quitar barra
        primaryStage.initStyle(StageStyle.UNDECORATED);

        //Icono
        primaryStage.getIcons().add(new Image("/ico.png"));


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}