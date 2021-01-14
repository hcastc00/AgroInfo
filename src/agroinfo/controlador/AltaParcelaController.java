package agroinfo.controlador;

import agroinfo.modelo.dao.ParcelaDAO;
import agroinfo.modelo.vo.Coneja;
import agroinfo.modelo.vo.Parcela;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AltaParcelaController {

    ParcelaDAO parcelaDAO = new ParcelaDAO();

    @FXML
    JFXTextField idParcela;

    @FXML
    JFXTextField latitud;

    @FXML
    JFXTextField longitud;

    @FXML
    JFXTextField tam;

    @FXML
    JFXComboBox tipoParcela;

    @FXML
    JFXComboBox tipoCultivo;

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
    public void guardar (ActionEvent event) throws IOException {


        boolean idParcelaError = idParcela.getText().isBlank() || !idParcela.getText().matches("^[0-9]*$");
        boolean latitudError =latitud.getText().isBlank() || !latitud.getText().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$");
        boolean longitudError = longitud.getText().isBlank() || !longitud.getText().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$");
        boolean tipoParcelaError = tipoParcela.getValue() == null;
        boolean tamError = tam.getText().isBlank() || !tam.getText().matches("^(0|([1-9][0-9]*))(\\.[0-9]+)?$");
        boolean tipoCultivoError = tipoCultivo.getValue() == null;

        if(latitudError==false)
            if((Double.parseDouble(latitud.getText() ) > 90) || (Double.parseDouble(latitud.getText()) < -90)){
            latitudError=true;
        }

        if(longitudError==false)
            if((Double.parseDouble(longitud.getText() ) > 180) || (Double.parseDouble(longitud.getText()) < -180)){
            latitudError=true;
        }

        if(!idParcelaError && !latitudError && !longitudError && !tipoParcelaError && !tamError && !tipoCultivoError){
            if(botonGuardar.getScene().getUserData().equals("modificar")){
                modificar(event);
            }else{
                crear(event);
            }
        }else{

            if (idParcelaError)
                error.setText("El ID de la parcela debe ser un número");
            else if (latitudError)
                error.setText("La latitud tiene el formato incorrecto. Ej: 38.950184163856186");
            else if (longitudError)
                error.setText("La longitud tiene el formato incorrecto. Ej: -77.05744303361926");
            else if(tipoParcelaError)
                error.setText("Debe seleccionar un tipo de parcela");
            else if(tamError)
                error.setText("El tamaño de la parcela debe ser un número positivo");
            else if(tipoCultivoError)
                error.setText("Debe seleccionar un tipo de cultivo");

            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }


    }

    private void crear(ActionEvent event) {
        try {
            parcelaDAO.crear(new Parcela(Integer.parseInt(idParcela.getText()),
                            Double.parseDouble(latitud.getText()),
                            Double.parseDouble(longitud.getText()), Double.parseDouble(tam.getText()),
                            Parcela.TipoParcela.valueOf(tipoParcela.getSelectionModel().getSelectedItem().toString()),
                            Parcela.TipoCultivo.valueOf(tipoCultivo.getSelectionModel().getSelectedItem().toString())),
                    LoginController.getUsuarioActual().getNombreUsuario());

            this.close(event);

        } catch (SQLException e) {

            if (e.getClass() == MySQLIntegrityConstraintViolationException.class) {
                error.setText("La parcela con el id " + idParcela.getText() + " ya existe.");
            }

            if (e.getClass() == CommunicationsException.class) {
                error.setText("Ha ocurrido un error al conectarse con la base de datos.");
            }

            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }

    private void modificar(ActionEvent event) {
        try {
            parcelaDAO.modificar(new Parcela(Integer.parseInt(idParcela.getText()),
                            Double.parseDouble(latitud.getText()),
                            Double.parseDouble(longitud.getText()), Double.parseDouble(tam.getText()),
                            Parcela.TipoParcela.valueOf(tipoParcela.getSelectionModel().getSelectedItem().toString()),
                            Parcela.TipoCultivo.valueOf(tipoCultivo.getSelectionModel().getSelectedItem().toString())),
                    LoginController.getUsuarioActual().getNombreUsuario());

            this.close(event);

        } catch (SQLException e) {

            if (e.getClass() == MySQLIntegrityConstraintViolationException.class) {
                error.setText("La parcela con el id " + idParcela.getText() + " no existe.");
            }

            if (e.getClass() == CommunicationsException.class) {
                error.setText("Ha ocurrido un error al conectarse con la base de datos.");
            }

            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }

}
