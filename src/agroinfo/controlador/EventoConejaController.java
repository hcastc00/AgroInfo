package agroinfo.controlador;

import agroinfo.modelo.dao.EventoConejaDAO;
import agroinfo.modelo.vo.EventoConeja;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class EventoConejaController {

    private final EventoConejaDAO eventoConejaDAO = new EventoConejaDAO();

    @FXML
    private JFXDatePicker fecha;

    @FXML
    private JFXComboBox tipoEventoConeja;

    @FXML
    private JFXButton botonGuardar;

    @FXML
    private Label error;


    @FXML
    private void guardar(ActionEvent actionEvent) {

        Object evento = botonGuardar.getScene().getUserData();
        boolean fechaError = (fecha.getValue() == null);
        boolean tipoError = (tipoEventoConeja.getValue() == null);

        try {
            if (!fechaError && !tipoError) {
                //Si evento es un String, estamos creando un conejo
                if (evento instanceof String) {
                    eventoConejaDAO.crear(new EventoConeja((Integer.valueOf(evento.toString())),
                                    java.sql.Date.valueOf(fecha.getValue()),
                                    (EventoConeja.TipoEventoConeja) tipoEventoConeja.getValue()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    //Si evento es un EventoConeja, es un modificar
                } else if (evento instanceof EventoConeja) {
                    EventoConeja nuevoEvento = new EventoConeja(
                            ((EventoConeja) evento).getId(),
                            ((EventoConeja) evento).getIdConeja(),
                            java.sql.Date.valueOf(fecha.getValue()),
                            (EventoConeja.TipoEventoConeja) tipoEventoConeja.getValue());
                    eventoConejaDAO.modificar(nuevoEvento, LoginController.getUsuarioActual().getNombreUsuario());
                }

                this.close(actionEvent);
            } else if (fechaError) {
                error.setText("Debe introducir una fecha");
                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();

            } else if (tipoError) {
                error.setText("Debe introducir un tipo de evento");
                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
