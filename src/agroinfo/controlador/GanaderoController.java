package agroinfo.controlador;

import agroinfo.modelo.conexion.ConexionSensor;
import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.*;
import agroinfo.vista.Ventana;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    private VBox listaAlmacen;

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

    @FXML
    private JFXTextField buscarEvento;

    @FXML
    private Pane panelEventos;

    @FXML
    private TableView<EventoConeja> listaEventos;

    @FXML
    private TableColumn<EventoConeja, String> fecha;

    @FXML
    private TableColumn<EventoConeja, String> tipo;


    //Lista de conejas
    private List<String[]> conejas;
    private Node[] nodesC;

    // Lista de las ventas
    private List<Venta> ventas;
    private Node[] nodesV;

    // Lista de los gastos
    private List<Gasto> gastos;
    private Node[] nodesG;

    //Cosas de almacen
    private Node nodeA;
    private Almacen almacen;

    //Lista de eventos
    private List<EventoConeja> eventos;

    /*
     * Esta variable indica en que vista esta el programa para facilitar metodos
     *      - El 0 es para Coneja
     *      - El 1 es para Almacen
     *      - El 2 es para Ventas
     *      - El 3 es para Gastos
     *      - El 4 es para Los Eventos de las conejas
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
        this.panelEventos.setVisible(false);
        this.panelConejas.setVisible(true);

        if (this.conejas == null || this.conejas.isEmpty()) {
            this.conejas = conejaDAO.listarConEventos();
            this.nodesC = new Node[conejas.size()];
            this.pintaConejas();
        }
    }

    @FXML
    private void mostrarAlmacen() {
        this.panel = 1;
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelConejas.setVisible(false);
        this.panelEventos.setVisible(false);
        this.panelAlmacen.setVisible(true);

        if(this.almacen == null){
            this.almacen = almacenDAO.getAlmacen();
            this.pintaAlmacen();
        }

    }

    @FXML
    private void mostrarVentas() {

        Task<Boolean> t = new Task<Boolean>() {
            @Override
            protected Boolean call() {

                if (ventas == null || ventas.isEmpty()) {
                    ventas = ventaDAO.listar(Venta.TipoVenta.Ganaderia);
                    nodesV = new Node[ventas.size()];
                    return true;
                }
                return false;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if(t.getValue())
                this.pintaVenta();
            this.panel = 2;
            this.panelAlmacen.setVisible(false);
            this.panelConejas.setVisible(false);
            this.panelGastos.setVisible(false);
            this.panelEventos.setVisible(false);
            this.panelVentas.setVisible(true);
        });

        new Thread(t).start();
    }


    @FXML
    private void mostrarGastos(){

        Task<Boolean> t = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                if (gastos == null || gastos.isEmpty()) {
                    gastos = gastoDAO.listar(Gasto.TipoGasto.Ganaderia);
                    nodesG = new Node[gastos.size()];
                    return true;
                }
                return false;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if(t.getValue())
                this.pintaGasto();
            this.panel = 3;
            this.panelAlmacen.setVisible(false);
            this.panelVentas.setVisible(false);
            this.panelConejas.setVisible(false);
            this.panelEventos.setVisible(false);
            this.panelGastos.setVisible(true);
        });

        new Thread(t).start();
    }

    @FXML
    private void mostrarEventosConeja(int id){

        Task<Boolean> t = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                if (eventos == null || eventos.isEmpty()) {
                    eventos = eventoConejaDAO.listar(id);
                    return true;
                }
                return false;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if(t.getValue())
                this.pintaEventosConeja(id);

            this.panel = 4;
            this.panelConejas.setVisible(false);
            this.panelGastos.setVisible(false);
            this.panelVentas.setVisible(false);
            this.panelAlmacen.setVisible(false);
            this.panelEventos.setVisible(true);
        });

        new Thread(t).start();

    }

    private void pintaEventosConeja(int id) {

        this.listaEventos.setItems(null);
        this.listaEventos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.fecha.setCellValueFactory(f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getFecha())));
        this.tipo.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getTipoEventoConeja().toString()));

        FilteredList<EventoConeja> filteredData = new FilteredList<>(FXCollections.observableArrayList(eventoConejaDAO.listar(id)), p -> true);
        SortedList<EventoConeja> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(this.listaEventos.comparatorProperty());
        this.listaEventos.setItems(sortedData);

        buscarEvento.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(eventos -> {
                if(text == null || text.isEmpty()) return true;

                String fecha = String.valueOf(eventos.getFecha());
                String tipo = eventos.getTipoEventoConeja().toString();
                return  fecha.contains(text) || tipo.contains(text);
            });
        });
    }

    @FXML
    private void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent ganadero = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        Scene scene = new Scene(ganadero, 1200, 750);
        scene.getStylesheets().add(Ventana.color);
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
        Parent root = loader.load();

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
    public void altaVenta(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaVenta.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setUserData(Venta.TipoVenta.Ganaderia);
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
    private void altaEvento(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaEventoConeja.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        JFXButton boton = (JFXButton)actionEvent.getSource();
        Label label = (Label)boton.getScene().lookup("#idEscondido");

        JFXComboBox tipoEventoConeja = (JFXComboBox)root.lookup("#tipoEventoConeja");
        tipoEventoConeja.getItems().addAll(EventoConeja.TipoEventoConeja.values());

        scene.setUserData(1);

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
    private void eliminarEvento(){
/*        eventoDAO.eliminar(eventoSeleccionado, LoginController.getUsuarioActual().getNombreUsuario());
        this.recargar();*/
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
                this.ventas = ventaDAO.listar(Venta.TipoVenta.Ganaderia);
                this.nodesV = new Node[ventas.size()];
                this.pintaVenta();
                break;
            case 3:
                this.listaGastos.getChildren().clear();
                this.gastos = gastoDAO.listar(Gasto.TipoGasto.Ganaderia);
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

                JFXButton borrar = (JFXButton) nodesC[i].lookup("#botonBorrar");
                borrar.setOnAction(e ->{
                    conejaDAO.eliminar(Integer.parseInt(id.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                //Eventos
                JFXButton gestionarEventos = (JFXButton) nodesC[i].lookup("#botonEventos");
                gestionarEventos.setOnAction(e ->{
                    mostrarEventosConeja(Integer.parseInt(id.getText()));
                });

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

                //Borrar
                JFXButton borrar = (JFXButton) nodesV[i].lookup("#botonBorrar");
                borrar.setOnAction(e ->{
                    ventaDAO.eliminar(Integer.parseInt(id.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                //Desc
                JFXButton verDesc = (JFXButton) nodesV[i].lookup("#botonDesc");
                verDesc.setOnAction(e ->{
                    try {
                        Image img = new Image(this.getClass().getClassLoader().getResource("img/venta.png").toURI().toString());
                        pintarDescripcion(ventaDAO.buscar(Integer.parseInt(id.getText())).getDescripcion(),img);
                    } catch (IOException | URISyntaxException ioException) {
                        ioException.printStackTrace();
                    }
                });

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

                //Borrar
                JFXButton borrar = (JFXButton) nodesG[i].lookup("#botonBorrar");
                borrar.setOnAction(e ->{
                    gastoDAO.eliminar(Integer.parseInt(id.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                //Desc
                JFXButton verDesc = (JFXButton) nodesG[i].lookup("#botonDesc");
                verDesc.setOnAction(e ->{
                    try {
                        Image img = new Image(this.getClass().getClassLoader().getResource("img/gasto.png").toURI().toString());
                        pintarDescripcion(gastoDAO.buscar(Integer.parseInt(id.getText())).getDescripcion(),img);
                    } catch (IOException | URISyntaxException ioException) {
                        ioException.printStackTrace();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.listaGastos.getChildren().addAll(nodesG);
    }

    private void pintarDescripcion(String d, Image i) throws IOException {
        Parent loader =  FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/descripcion.fxml"));
        Scene scene = new Scene(loader);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

        JFXTextArea desc = (JFXTextArea)scene.lookup("#descripcion");
        desc.setText(d);

        ImageView img = (ImageView)scene.lookup("#imagen");
        img.setImage(i);

    }

    private void pintaAlmacen() {
        this.listaAlmacen.getChildren().clear();
        try {
            nodeA = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/almacen.fxml"));

            Pane agriPane = (Pane) nodeA.lookup("#agriPane");
            Pane ganPane = (Pane) nodeA.lookup("#ganPane");

            agriPane.setVisible(false);
            ganPane.setVisible(true);

            //Conejos
            Label conejos = (Label) nodeA.lookup("#conejos");
            conejos.setText(String.valueOf(almacen.getConejos()));

            //Pienso Lactancia
            Label pL = (Label) nodeA.lookup("#piensoLactancia");
            pL.setText(String.valueOf(almacen.getPiensoLactancia()));

            //Pienso Medicado
            Label pM = (Label) nodeA.lookup("#piensoMedicado");
            pM.setText(String.valueOf(almacen.getPiensoMedicado()));

            //Pienso Remate
            Label pR = (Label) nodeA.lookup("#piensoRemate");
            pR.setText(String.valueOf(almacen.getPiensoRemate()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.listaAlmacen.getChildren().add(nodeA);
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
}
