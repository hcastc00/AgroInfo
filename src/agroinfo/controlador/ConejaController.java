package agroinfo.controlador;

import agroinfo.modelo.dao.ConejaDAO;
import agroinfo.modelo.vo.Coneja;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConejaController {

    private String userName = LoginController.getUsuarioActual().getNombreUsuario();

    @FXML
    private JFXTextField idAlta;

    ConejaDAO conejaDAO = new ConejaDAO();

    @FXML
    private Label error;

    @FXML
    private Label id;

    @FXML
    private JFXButton botonGuardar;

    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    private void guardar(ActionEvent event) {
        if(!idAlta.getText().isBlank() && idAlta.getText().matches("^[0-9]*$")){
            conejaDAO.crear(new Coneja((Integer.parseInt(idAlta.getText()))), userName);
            this.close(event);
        }else{
            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }

    @FXML
    private void borrar(ActionEvent event) {
        conejaDAO.eliminar(Integer.parseInt(id.getText()), userName);
    }
}
