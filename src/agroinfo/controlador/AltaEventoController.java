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
import javafx.event.Event;
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
    private void guardar(ActionEvent actionEvent) throws SQLException {

        Object objeto = botonGuardar.getScene().getUserData();

        boolean fechaError = (fecha.getValue() == null);
        boolean descripcionError = (descripcion.getText().isBlank());

        if(objeto instanceof String || objeto instanceof Integer) {
            //GUARDAR
            if (!fechaError && !descripcionError) {
                //Si en correcto es un evento maquinaria
                if (objeto instanceof String) {
                    eventoDAO.crear(new Evento(String.valueOf(objeto), java.sql.Date.valueOf(fecha.getValue()), descripcion.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                } else {
                    eventoDAO.crear(new Evento((Integer) objeto, java.sql.Date.valueOf(fecha.getValue()), descripcion.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                }
                this.close(actionEvent);
            } else comprobar(fechaError, descripcionError);

        }else{
            //MODIFICAR
            if (!fechaError && !descripcionError) {
                //Si en correcto es un evento maquinaria
                Evento e = (Evento) objeto;
                if (e.getIdentificadorParcela() == Integer.MIN_VALUE) {
                    eventoDAO.modificar(new Evento(e.getId(),e.getMatricula(), java.sql.Date.valueOf(fecha.getValue()), descripcion.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                } else {
                    eventoDAO.modificar(new Evento(e.getId(), e.getIdentificadorParcela(), java.sql.Date.valueOf(fecha.getValue()), descripcion.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                }
                this.close(actionEvent);
            } else {
                comprobar(fechaError, descripcionError);
            }

        }




    }

    private void comprobar(boolean fechaError, boolean descripcionError) {
        if (fechaError) {
            error.setText("Debe introducir una fecha");
            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();

        } else if (descripcionError) {
            error.setText("Debe introducir una descripción");
            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();

        }
    }

    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
