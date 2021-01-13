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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AltaParcelaController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       tipoParcela.getItems().addAll(Parcela.TipoParcela.values());
       tipoCultivo.getItems().addAll(Parcela.TipoCultivo.values());
    }

    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    private void guardar(ActionEvent event) {

        boolean idParcelaError = idParcela.getText().isBlank() || !idParcela.getText().matches("^[0-9]*$");
        boolean latitudError =latitud.getText().isBlank() || !latitud.getText().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$");
        boolean longitudError = longitud.getText().isBlank() || !longitud.getText().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$");
        boolean tipoParcelaError = tipoParcela.getValue() == null;
        boolean tamError = tam.getText().isBlank() || !tam.getText().matches("^(0|([1-9][0-9]*))(\\.[0-9]+)?$");
        boolean tipoCultivoError = tipoCultivo.getValue() == null;

        if(!idParcelaError && !latitudError && !longitudError && !tipoParcelaError && !tamError && !tipoCultivoError){

            try {
                parcelaDAO.crear(new Parcela(Integer.parseInt(idParcela.getText()),
                        Double.parseDouble(latitud.getText()),
                        Double.parseDouble(longitud.getText()), Double.parseDouble(tam.getText()),
                        Parcela.TipoParcela.valueOf(tipoParcela.getSelectionModel().getSelectedItem().toString()),
                        Parcela.TipoCultivo.valueOf(tipoCultivo.getSelectionModel().getSelectedItem().toString())),
                        LoginController.getUsuarioActual().getNombreUsuario());

                this.close(event);

            } catch (SQLException e) {

                if (e.getClass() == MySQLIntegrityConstraintViolationException.class){
                    error.setText("La parcela con el id " + idParcela.getText() + " ya existe.");
                }

                if(e.getClass() == CommunicationsException.class){
                    error.setText("Ha ocurrido un error al conectarse con la base de datos.");
                }

                error.setVisible(true);
                new Shake(botonGuardar).play();
                new FadeIn(error).play();
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
            else if(tipoParcelaError)
                error.setText("Debe seleccionar un tipo de cultivo");

            error.setVisible(true);
            new Shake(botonGuardar).play();
            new FadeIn(error).play();
        }
    }



}
