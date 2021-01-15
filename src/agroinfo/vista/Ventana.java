package agroinfo.vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Ventana extends Application {
    private static final String verde = "css/greenTheme.css";
    private static final String crema = "css/creamTheme.css";
    public static String color = crema;
    private double x, y;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        primaryStage.setTitle("AgroInfo");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(color);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);

        //Quitar barra
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        //Icono
        primaryStage.getIcons().add(new Image("/img/ico.png"));

        primaryStage.show();
    }
}