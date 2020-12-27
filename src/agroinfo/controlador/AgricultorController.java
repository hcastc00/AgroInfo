package agroinfo.controlador;

import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.*;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Date;

public class AgricultorController {
    private ParcelaDAO parcelaDAO;
    private GastoDAO gastoDAO;
    private VentaDAO ventaDAO;
    private MaquinariaDAO maquinariaDAO;
    private EventoDAO eventoDAO;
    private AlmacenDAO almacenDAO;

    @FXML
    private JFXButton exitButton;

    @FXML
    private Pane loginPane;

    @FXML
    void salir(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    void altaParcela(ActionEvent event) {
        Parcela parcela = new Parcela(1,2,3,4, Parcela.TipoParcela.Regadio, Parcela.TipoCultivo.Barbecho);
        this.almacenDAO.getAlmacen().setExcedenteTotal(2);
    }

    @FXML
    void bajaParcela(ActionEvent event) {

    }

    @FXML
    void altaMaquinaria(ActionEvent event) {
        Maquinaria maquinaria = new Maquinaria("4366BDR","Audi 80");
    }

    @FXML
    void bajaMaquinaria(ActionEvent event) {

    }

    @FXML
    void crearVenta(ActionEvent event) {
        Venta venta = new Venta(1,5,"Cosecha");
    }

    @FXML
    void eliminarVenta(ActionEvent event) {
    }


    @FXML
    void crearGasto(ActionEvent event) {
        Gasto gasto = new Gasto(1,"Gasto", Gasto.TipoGasto.Agricultura,"juan");
    }

    @FXML
    void eliminarGasto(ActionEvent event) {

    }

    @FXML
    void modificarGasto(ActionEvent event) {

    }

    @FXML
    void crearEventoMaquinaria(ActionEvent event) {
        Evento evento = new Evento("La matricula", new Date("10/12/2000"),"Fiesta en La Cueva");
    }

    @FXML
    void eliminarEventoMaquinaria(ActionEvent event) {

    }

    @FXML
    void modificarEventoMaquinaria(ActionEvent event) {

    }

    @FXML
    void crearEventoParcela(ActionEvent event) {

    }

    @FXML
    void eliminarEventoParcela(ActionEvent event) {

    }

    @FXML
    void modificarEventoParcela(ActionEvent event) {

    }




}
