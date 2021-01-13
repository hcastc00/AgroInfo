package agroinfo.controlador;

import agroinfo.modelo.conexion.ConexionSensor;
import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.EventoConeja;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Venta;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;

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
    private final UsuarioDAO usuarioActual = new UsuarioDAO();
    private final ConexionSensor sensor = new ConexionSensor();

    @FXML
    private VBox listaVentas;

    @FXML
    private VBox listaGastos;

    @FXML
    private Pane panelVentas;

    @FXML
    private Pane panelConejas;

    @FXML
    private Pane panelGastos;

    @FXML
    private Pane panelAlmacen;

    @FXML
    private VBox listaConejas;

    @FXML
    private AnchorPane root;
    
    @FXML
    private JFXButton temp;

    @FXML
    private JFXTextField buscarConejas;

    @FXML
    private JFXTextField buscarVenta;

    @FXML
    private JFXTextField buscarGasto;

    //Lista de conejas
    private List<String[]> conejas;
    private Node[] nodesC;

    // Lista de las ventas
    private List<Venta> ventas;
    private Node[] nodesV;

    // Lista de los gastos
    private List<Gasto> gastos;
    private Node[] nodesG;

    /*
     * Esta variable indica en que vista esta el programa para facilitar metodos
     *      - El 0 es para Coneja
     *      - El 1 es para Almacen
     *      - El 2 es para Ventas
     *      - El 3 es para Gastos
     */
    private int panel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.moverVentana();
        this.getTemperatura();
        this.mostrarConejas();
    }

    @FXML
    private void mostrarConejas(){
        this.panel = 0;
        this.panelAlmacen.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelConejas.setVisible(true);

        if (this.gastos == null || this.conejas.isEmpty()) {
            this.conejas = conejaDAO.listarConEventos();
            this.nodesC = new Node[conejas.size()];
            this.pintaConejas();
        }
    }

    @FXML
    private void mostrarAlmacen(){
        this.panel = 1;
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelConejas.setVisible(false);
        this.panelAlmacen.setVisible(true);
    }

    @FXML
    private void mostrarGastos(){
        this.panel = 2;
        this.panelAlmacen.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelConejas.setVisible(false);
        this.panelGastos.setVisible(true);

        if (this.gastos == null || this.gastos.isEmpty()) {
            this.gastos = gastoDAO.listar();
            this.nodesG = new Node[gastos.size()];
            this.pintaGasto();
        }

    }

    @FXML
    private void mostrarVentas() {
        this.panel = 3;
        this.panelAlmacen.setVisible(false);
        this.panelConejas.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(true);

        if (this.ventas == null || this.ventas.isEmpty()) {
            this.ventas = this.ventaDAO.listar(LoginController.getUsuarioActual().getTipo().toString());
            this.nodesV = new Node[ventas.size()];
            this.pintaVenta();
        }
    }
    
    @FXML
    private void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent ganadero = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        Scene scene = new Scene(ganadero, 1200, 750);
        scene.getStylesheets().add("css/darkGreen.css");
        thisStage.setScene(scene);

        usuarioActual.cerrarSesion(LoginController.getUsuarioActual().getNombreUsuario());
    }

    @FXML
    private void buscar() {
        switch (this.panel) {
            case 0 -> {
                //Limpio y vuelvo a meter todos los nodos para evitar duplicados
                listaConejas.getChildren().clear();
                listaConejas.getChildren().addAll(nodesC);

                //Predicate para la busqueda
                listaConejas.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    return !id.getText().matches(buscarConejas.getText() + ".*");
                });
            }
            case 1 -> {

            }

            case 2 -> {
                //Limpio y vuelvo a meter todos los nodos para evitar duplicados
                listaVentas.getChildren().clear();
                listaVentas.getChildren().addAll(nodesV);

                //Predicate para la busqueda
                listaVentas.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    return !id.getText().matches(buscarVenta.getText() + ".*");
                });
            }

            case 3 -> {
                //Limpio y vuelvo a meter todos los nodos para evitar duplicados
                listaGastos.getChildren().clear();
                listaGastos.getChildren().addAll(nodesG);

                //Predicate para la busqueda
                listaGastos.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    return !id.getText().matches(buscarGasto.getText() + ".*");
                });
            }
        }
    }
    
    @FXML
    private void altaConeja(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaConejas.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage.setOnHidden(windowEvent -> {
            this.recargar();
        });
    }

    @FXML
    private void bajaConeja(ActionEvent event) {


    }

    @FXML
    public void altaGasto(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaGasto.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.setUserData(Gasto.TipoGasto.Ganaderia);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage.setOnHidden(windowEvent -> {
            this.recargar();
        });
    }

    @FXML
    private void eliminarGasto(ActionEvent event) {

    }

    @FXML
    private void modificarGasto(ActionEvent event) {

    }

    @FXML
    private void crearEventoConeja(ActionEvent event) {
        EventoConeja eventoConeja = new EventoConeja(1, new Date("Hoy"), EventoConeja.TipoEventoConeja.Inseminacion);
    }

    @FXML
    private void eliminarEventoConeja(ActionEvent event) {

    }

    @FXML
    private void modificarEventoConeja(ActionEvent event) {

    }

    @FXML
    public void altaVenta(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaVenta.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage.setOnHidden(windowEvent -> {
            this.recargar();
        });
    }

    @FXML
    private void eliminarVenta(ActionEvent event) {
    }

    @FXML
    private void getTemperatura() {
        temp.setText((sensor.getTemperatura()) + "ºC");
    }

    @FXML
    private void recargar() {
        switch (this.panel) {
            case 0:
                this.listaConejas.getChildren().clear();
                this.conejas = conejaDAO.listarConEventos();
                this.nodesC = new Node[conejas.size()];
                pintaConejas();
                break;
            case 1:
                break;
            case 2:
                this.listaVentas.getChildren().clear();
                this.ventas = ventaDAO.listar(LoginController.getUsuarioActual().getTipo().toString());
                this.nodesV = new Node[ventas.size()];
                this.pintaVenta();
                break;
            case 3:
                this.listaGastos.getChildren().clear();
                this.gastos = gastoDAO.listar();
                this.nodesG = new Node[gastos.size()];
                this.pintaGasto();
                break;
        }
    }

    //METODOS AUXILIARES
    
    private void pintaConejas() {
        this.listaConejas.getChildren().clear();
        int i = 0;
        for (String[] s : conejas) {
            try {
                nodesC[i] = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/coneja.fxml"));

                Label id = (Label) nodesC[i].lookup("#id");
                id.setText(s[0]);

                Label inseminacion = (Label) nodesC[i].lookup("#inseminacion");
                if (s[1] != null)
                    inseminacion.setText(s[1]);

                Label parto = (Label) nodesC[i].lookup("#parto");
                if (s[2] != null)
                    parto.setText(s[2]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
        listaConejas.getChildren().addAll(nodesC);
    }

    private void pintaVenta() {
        this.listaVentas.getChildren().clear();
        for (int i = 0; i < nodesV.length; i++) {
            try {
                nodesV[i] = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/venta.fxml"));

                //Id
                Label id = (Label) nodesV[i].lookup("#id");
                id.setText(String.valueOf(ventas.get(i).getId()));

                //Cantidad
                Label cant = (Label) nodesV[i].lookup("#cantidad");
                cant.setText(String.valueOf(ventas.get(i).getCantidad()));

                //Precio Unitario
                Label pu = (Label) nodesV[i].lookup("#pu");
                pu.setText(String.valueOf(ventas.get(i).getPrecioUnitario()));

                //Total
                Label total = (Label) nodesV[i].lookup("#total");
                total.setText(String.valueOf((ventas.get(i).getCantidad()) * (ventas.get(i).getPrecioUnitario())));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.listaVentas.getChildren().addAll(nodesV);
    }

    private void pintaGasto() {
        this.listaGastos.getChildren().clear();
        for (int i = 0; i < gastos.size(); i++) {
            try {
                nodesG[i] = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/gasto.fxml"));

                //Id
                Label id = (Label) nodesG[i].lookup("#id");
                id.setText(String.valueOf(gastos.get(i).getId()));

                //Importe
                Label importe = (Label) nodesG[i].lookup("#importe");
                importe.setText(String.valueOf(gastos.get(i).getImporte()));

                //TipoGasto
                Label tGasto = (Label) nodesG[i].lookup("#tipoGasto");
                tGasto.setText(String.valueOf(gastos.get(i).getTipoGasto()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.listaGastos.getChildren().addAll(nodesG);
    }

    private void moverVentana() {
        root.setOnMousePressed(pressEvent -> {
            root.setOnMouseDragged(dragEvent -> {
                if (pressEvent.getSceneY() < 75) {
                    root.getScene().getWindow().setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                    root.getScene().getWindow().setY(dragEvent.getScreenY() - pressEvent.getSceneY());
                }
            });
        });
    }

    //TODO en el crear pasar GanaderoController.getUsuarioActual.getNombreActual()

}
