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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


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
        List<Coneja> conejas = conejaDAO.listar();
        Node[] nodes = new Node[conejas.size()];

        for(int i=0; i<nodes.length; i++){
            try {
                nodes[i] = FXMLLoader.load(this.getClass().getResource("../vista/coneja.fxml"));

                //Id
                Label id = (Label)nodes[i].lookup("#id");
                id.setText(String.valueOf(conejas.get(i).getId()));

                //Inseminacion
                Label inseminacion = (Label)nodes[i].lookup("#inseminacion");
                Date ins = this.getProxEvento(conejas.get(i), EventoConeja.TipoEventoConeja.Inseminacion);
                if(ins != null) inseminacion.setText(ins.toString());

                //Parto
                Label parto = (Label)nodes[i].lookup("#parto");
                Date par = this.getProxEvento(conejas.get(i), EventoConeja.TipoEventoConeja.Parto);
                if(par != null) parto.setText(par.toString());

            }catch (IOException e){
                e.printStackTrace();
            }
        }
            listaConejas.getChildren().addAll(nodes);


    }

    private Date getProxEvento(Coneja c, EventoConeja.TipoEventoConeja t){
        List<EventoConeja> listaEventos  = eventoConejaDAO.listar(c.getId(),t);

        if(listaEventos.isEmpty()) return null;

        Date prox = listaEventos.get(0).getFecha();
        for(EventoConeja e : listaEventos){
            if(e.getFecha().before(prox))
                prox = e.getFecha();
        }

        return prox;
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
