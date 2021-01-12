package agroinfo.controlador;

import agroinfo.modelo.dao.GastoDAO;
import agroinfo.modelo.dao.UsuarioDAO;
import agroinfo.modelo.vo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private AnchorPane root;
    private UsuarioDAO usuarioDAO;
    private GastoDAO gastoDAO;

    @FXML
    private void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent admin = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        Scene scene = new Scene(admin, 1200, 750);
        scene.getStylesheets().add("css/darkGreen.css");
        thisStage.setScene(scene);
    }

    @FXML
    void crearUsuario(ActionEvent event){
        Usuario u = new Usuario("pepe","pepe", Usuario.TipoUsuario.Agricultor);

    }

    @FXML
    void borrarUsuario(ActionEvent event){

    }

    @FXML
    void verLogs(ActionEvent event){

    }

    @FXML
    void verGastos(ActionEvent event){

    }



}


