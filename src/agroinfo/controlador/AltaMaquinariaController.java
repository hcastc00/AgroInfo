package agroinfo.controlador;

import agroinfo.modelo.dao.MaquinariaDAO;
import agroinfo.modelo.vo.Maquinaria;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AltaMaquinariaController {

    MaquinariaDAO maquinariaDAO = new MaquinariaDAO();

    @FXML
    private JFXTextField matricula;

    @FXML
    private JFXTextField nombre;

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
    private void guardar(ActionEvent event) {

        boolean matriculaError = matricula.getText().isBlank() || !matricula.getText().matches("^[A-Za-z0-9]*$");
        boolean nombreError = nombre.getText().isBlank();

        if(!matriculaError && !nombreError){

            try {
                maquinariaDAO.crear(new Maquinaria(matricula.getText(), nombre.getText()),
                        LoginController.getUsuarioActual().getNombreUsuario());
                this.close(event);
            } catch (SQLException e) {

                if (e.getClass() == MySQLIntegrityConstraintViolationException.class){
                    error.setText("La maquina con la matricula " + matricula.getText() + " ya existe.");
                }

                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();
            }

        }else{

            if (matriculaError)
                error.setText("La matricula debe estar formada por letras y numeros.");
            else
                error.setText("El nombre debe estar formado por letras y/o numeros.");

            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }

    @FXML
    private void borrar(ActionEvent event) {
       /* maquinariaDAO.eliminar(Integer.parseInt(id.getText()),
                LoginController.getUsuarioActual().getNombreUsuario());*/
    }
}
