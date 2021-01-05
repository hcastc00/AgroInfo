package agroinfo.controlador;

import agroinfo.modelo.conexion.ConexionSensor;
import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class GanaderoController implements Initializable {
    private final ConejaDAO conejaDAO = new ConejaDAO();
    private final EventoConejaDAO eventoConejaDAO = new EventoConejaDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final GastoDAO gastoDAO = new GastoDAO();
    private final ConexionSensor sensor = new ConexionSensor();

    @FXML
    private VBox listaConejas = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<String[]> lista = conejaDAO.listarConEventos();

        Node[] nodes = new Node[lista.size()];

        int i = 0;
        for(String[] s: lista){
            try {
                nodes[i] = FXMLLoader.load(this.getClass().getResource("../vista/coneja.fxml"));

                Label id = (Label)nodes[i].lookup("#id");
                id.setText(s[0]);

                Label inseminacion = (Label)nodes[i].lookup("#inseminacion");
                if(s[1]!= null)
                    inseminacion.setText(s[1]);

                Label parto = (Label)nodes[i].lookup("#parto");
                if(s[2]!= null)
                    parto.setText(s[2]);

            }catch (IOException e){
                e.printStackTrace();
            }

            i++;
        }
        listaConejas.getChildren().addAll(nodes);
    }

    @FXML
    void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent ganadero = FXMLLoader.load(getClass().getResource("../vista/login.fxml"));
        thisStage.setScene(new Scene(ganadero, 1200  , 750));

    }

    @FXML
    void altaConeja(ActionEvent event) {

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
        //Venta venta = new Venta(1,60,"El perro");
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
