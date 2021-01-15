package agroinfo.controlador;

import agroinfo.modelo.dao.EventoConejaDAO;
import agroinfo.modelo.vo.EventoConeja;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AltaEventoConejaController{

    private EventoConejaDAO eventoConejaDAO = new EventoConejaDAO();

    @FXML
    private JFXDatePicker fecha;

    @FXML
    private JFXComboBox tipoEventoConeja;

    @FXML
    private JFXButton botonGuardar;

    @FXML
    private Label error;


    @FXML
    private void guardar(ActionEvent actionEvent){

        Object objeto = botonGuardar.getScene().getUserData();
        boolean fechaError = (fecha.getValue() == null);
        boolean tipoError = (tipoEventoConeja.getValue() == null);

        try {
            if(!fechaError && !tipoError) {
                eventoConejaDAO.crear(new EventoConeja((Integer.valueOf(objeto.toString())),
                                java.sql.Date.valueOf(fecha.getValue()),
                                (EventoConeja.TipoEventoConeja) tipoEventoConeja.getValue()),
                                LoginController.getUsuarioActual().getNombreUsuario());
                this.close(actionEvent);
            }else if(fechaError){
                error.setText("Debe introducir una fecha");
                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();

            }else if (tipoError){
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
