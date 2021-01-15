package agroinfo.controlador;

import agroinfo.modelo.dao.UsuarioDAO;
import agroinfo.modelo.vo.Usuario;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private JFXTextField nombre;

    @FXML
    private JFXTextField contrasenya;

    @FXML
    private JFXComboBox tipo;

    @FXML
    private JFXButton botonGuardar;

    @FXML
    private Label error;

    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    private void guardar(ActionEvent event) {

        boolean nombreError = nombre.getText().isBlank();
        boolean contrasenyaError = contrasenya.getText().isBlank();
        boolean tipoError = tipo.getValue() == null;

        if (!nombreError && !tipoError && !contrasenyaError) {

            try {
                usuarioDAO.crear(new Usuario(nombre.getText(), contrasenya.getText(),
                                Usuario.TipoUsuario.valueOf(tipo.getSelectionModel().getSelectedItem().toString())),
                        LoginController.getUsuarioActual().getNombreUsuario());
                this.close(event);
            } catch (SQLException e) {

                if (e.getClass() == MySQLIntegrityConstraintViolationException.class) {
                    error.setText("El usuario " + nombre.getText() + " ya existe.");
                }

                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();
            }

        } else {

            if (nombreError) {
                error.setText("El nombre no puede estar vacio.");
            } else if (tipoError) {
                error.setText("El tipo no puede estar vacio.");
            } else {
                error.setText("La contraseña no puede estar vacia.");
            }

            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }
}
