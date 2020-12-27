package agroinfo.controlador;

import agroinfo.modelo.dao.GastoDAO;
import agroinfo.modelo.dao.UsuarioDAO;
import agroinfo.modelo.vo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AdminController {

    private UsuarioDAO usuarioDAO;
    private GastoDAO gastoDAO;

    @FXML
    void salir(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
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


