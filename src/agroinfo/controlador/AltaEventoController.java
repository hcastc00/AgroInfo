package agroinfo.controlador;

import agroinfo.modelo.dao.EventoDAO;
import agroinfo.modelo.vo.Evento;
import agroinfo.modelo.vo.Gasto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.log.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class AltaEventoController {

    private EventoDAO eventoDAO = new EventoDAO();
    @FXML
    private JFXButton botonGuardar;

    @FXML
    private JFXDatePicker fecha;

    @FXML
    private JFXTextArea descripcion;

    @FXML
    private void guardar(ActionEvent actionEvent){

        Object objeto = botonGuardar.getScene().getUserData();

        try {
            if(objeto instanceof String){
                eventoDAO.crear(new Evento((String)objeto, java.sql.Date.valueOf(fecha.getValue()), descripcion.getText()),
                        LoginController.getUsuarioActual().getNombreUsuario());
            }else{
                eventoDAO.crear(new Evento((Integer)objeto,  java.sql.Date.valueOf(fecha.getValue()), descripcion.getText()),
                        LoginController.getUsuarioActual().getNombreUsuario());
            }

        } catch (SQLException ignored) {          }
       this.close(actionEvent);

    }

    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
