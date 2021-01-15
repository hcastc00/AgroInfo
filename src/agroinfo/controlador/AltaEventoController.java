package agroinfo.controlador;

import agroinfo.modelo.dao.EventoDAO;
import agroinfo.modelo.vo.Evento;
import agroinfo.modelo.vo.Gasto;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.log.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class AltaEventoController {
    private EventoDAO eventoDAO = new EventoDAO();

    @FXML
    private JFXButton botonGuardar;

    @FXML
    private JFXDatePicker fecha;

    @FXML
    private JFXTextArea descripcion;

    @FXML
    private Label error;


    @FXML
    private void guardar(ActionEvent actionEvent){

        Object identificador = botonGuardar.getScene().getUserData();

        //TODO
        boolean fechaError = (fecha.getValue() == null);
        boolean descripcionError = (descripcion.getText().isBlank());

        try {
            if(!fechaError && !descripcionError) {
                if (identificador instanceof String) {
                    eventoDAO.crear(new Evento((String) identificador, java.sql.Date.valueOf(fecha.getValue()), descripcion.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                } else {
                    eventoDAO.crear(new Evento((Integer) identificador, java.sql.Date.valueOf(fecha.getValue()), descripcion.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                }
                this.close(actionEvent);
            }else if (fechaError){
                error.setText("Debe introducir una fecha");
                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();

            }else if (descripcionError){
                error.setText("Debe introducir una descripción");
                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();

            }

        } catch (SQLException ignored) {          }



    }

    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
