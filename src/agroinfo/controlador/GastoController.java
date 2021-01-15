package agroinfo.controlador;

import agroinfo.modelo.dao.GastoDAO;
import agroinfo.modelo.vo.Gasto;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class GastoController {

    GastoDAO gastoDAO = new GastoDAO();

    @FXML
    private JFXTextField importe;

    @FXML
    private JFXTextArea descripcion;

    @FXML
    private Label error;

    @FXML
    private JFXButton botonGuardar;

    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    public void guardar(ActionEvent event) {

        boolean importeError = importe.getText().isBlank() || !importe.getText().matches("^[0-9]+([,.][0-9]?)?$");


        if (!importeError) {

            try {
                gastoDAO.crear(new Gasto(Double.parseDouble(importe.getText()),
                                descripcion.getText(),
                                (Gasto.TipoGasto) botonGuardar.getScene().getUserData(),
                                LoginController.getUsuarioActual().getNombreUsuario()),
                        LoginController.getUsuarioActual().getNombreUsuario());
                this.close(event);

            } catch (SQLException ignored) {
            }

        } else {
            error.setText("El importe debe ser un numero positivo. Ejemplo: 35.24.");

            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }

}
