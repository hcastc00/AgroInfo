package agroinfo.controlador;

import agroinfo.modelo.conexion.ConexionSensor;
import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.Coneja;
import agroinfo.modelo.vo.EventoConeja;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Venta;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;


public class GanaderoController {
    private final ConejaDAO conejaDAO = new ConejaDAO();
    private final EventoConejaDAO eventoConejaDAO = new EventoConejaDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final GastoDAO gastoDAO = new GastoDAO();
    private final ConexionSensor sensor = new ConexionSensor();


    @FXML
    private JFXListView<?> listaConejas;

    @FXML
    void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent ganadero = FXMLLoader.load(getClass().getResource("../vista/login.fxml"));
        thisStage.setScene(new Scene(ganadero, 1200  , 750));

    }

    @FXML
    void altaConeja(ActionEvent event) {
        ObservableList lista = FXCollections.observableArrayList();
        for(int i = 0; i<100;i++) lista.addAll("Pepe","Juan");
        listaConejas.setItems(lista);
        listaConejas.setCellFactory(ComboBoxListCell.forListView(lista));
        Coneja nuevaConeja = new Coneja(1);

    }

    @FXML
    void bajaConeja(ActionEvent event) {


    }

    @FXML
    void venta(ActionEvent event) {


    }

    @FXML
    void crearGasto(ActionEvent event) {
        Gasto gasto = new Gasto(1,"Pienso", Gasto.TipoGasto.Ganaderia,"pepe");
    }

    @FXML
    void eliminarGasto(ActionEvent event) {

    }

    @FXML
    void modificarGasto(ActionEvent event) {

    }


    @FXML
    void crearEventoConeja(ActionEvent event) {
        EventoConeja eventoConeja = new EventoConeja(1,new Date("Hoy"), EventoConeja.TipoEventoConeja.Inseminacion);
    }

    @FXML
    void eliminarEventoConeja(ActionEvent event) {

    }

    @FXML
    void modificarEventoConeja(ActionEvent event) {

    }

    @FXML
    void crearVenta(ActionEvent event) {
        Venta venta = new Venta(1,60,"El perro");
        this.almacenDAO.getAlmacen().setConejos(10);
    }

    @FXML
    void eliminarVenta(ActionEvent event) {
    }

    @FXML
    void getTemperatura(){
        sensor.getTemperatura();
    }

}
