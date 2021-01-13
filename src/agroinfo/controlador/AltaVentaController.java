package agroinfo.controlador;

import agroinfo.modelo.dao.VentaDAO;
import agroinfo.modelo.vo.Parcela;
import agroinfo.modelo.vo.Venta;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.SQLException;

public class AltaVentaController {

    VentaDAO ventaDAO = new VentaDAO();

    @FXML
    JFXTextArea descripcion;

    @FXML
    JFXTextField cantidad;

    @FXML
    JFXTextField precio_unitario;

    @FXML
    JFXButton botonGuardar;

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

        boolean decripcionError = descripcion.getText().isBlank();
        boolean cantidadError = cantidad.getText().isBlank() || !cantidad.getText().matches("^[0-9]*$");
        boolean precioUnitarioError = precio_unitario.getText().isBlank() || !precio_unitario.getText().matches("^(0|([1-9][0-9]*))(\\.[0-9]+)?$");


        if(!decripcionError && !cantidadError && !precioUnitarioError){

            try {
                ventaDAO.crear(new Venta(Integer.parseInt(cantidad.getText()),
                        Double.parseDouble(precio_unitario.getText()),
                        LoginController.getUsuarioActual().getNombreUsuario(),
                        descripcion.getText(), (Venta.TipoVenta) botonGuardar.getScene().getUserData()));

                this.close(event);

            } catch (CommunicationsException e) {

                error.setText("Ha ocurrido un error al conectarse con la base de datos.");

                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }else{

            if (cantidadError)
                error.setText("La cantidad debe ser un número positivo");
            else if (decripcionError)
                error.setText("La descripcion no puede estar vacía");
            else if (precioUnitarioError)
                error.setText("La cantidad debe ser un número positivo");

            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }
}
