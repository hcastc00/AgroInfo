package agroinfo.controlador;

import agroinfo.modelo.dao.ConejaDAO;
import agroinfo.modelo.vo.Coneja;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ConejaController {

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

        boolean idError = idAlta.getText().isBlank() || !idAlta.getText().matches("^[0-9]*$");

        if(!idError){

            try {
                conejaDAO.crear(new Coneja((Integer.parseInt(idAlta.getText()))),
                        LoginController.getUsuarioActual().getNombreUsuario());
                this.close(event);
            } catch (SQLException e) {

                if (e.getClass() == MySQLIntegrityConstraintViolationException.class){
                    error.setText("La coneja con el id " + idAlta.getText() + " ya existe.");
                }

                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();
            }

        }else{
            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }

    @FXML
    private void borrar(ActionEvent event) {
        conejaDAO.eliminar(Integer.parseInt(id.getText()),
                LoginController.getUsuarioActual().getNombreUsuario());
    }
}
