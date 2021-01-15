package agroinfo.controlador;

import agroinfo.modelo.conexion.ConexionSensor;
import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.Almacen;
import agroinfo.modelo.vo.EventoConeja;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Venta;
import agroinfo.vista.Ventana;
import com.jfoenix.controls.*;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
    private Pane panelAyuda;

    @FXML
    private Pane altaVentaP;

    @FXML
    private Pane ventaP;

    @FXML
    private Pane descripcionGasto;

    @FXML
    private Pane gastosP;

    @FXML
    private Pane almacenP;

    @FXML
    private Pane altaEventoConejaP;

    @FXML
    private Pane eventoConejaP;

    @FXML
    private Pane altaConejasP;

    @FXML
    private Pane descripcionVentaP;

    @FXML
    private Pane altaGastosP;

    @FXML
    private Pane conejasP;

    @FXML
    private TableView<EventoConeja> listaEventos;

    @FXML
    private TableColumn<EventoConeja, String> fecha;

    @FXML
    private TableColumn<EventoConeja, String> tipo;

    @FXML
    private Label idEscondido;

    @FXML
    private EventoConeja eventoSeleccionado;

    @FXML
    private JFXSpinner spinnerEventos;

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
     *      - El 5 es para la Ayuda
     */
    private int panel;

    /*
     * Esta variable indica el panel de la ayuda
     *      - El 0 es para Conejas
     *      - El 1 es para AltaConeja
     *      - El 2 es para EventoConeja
     *      - El 3 es para AltaEventoConeja
     *      - El 4 es para Almacen
     *      - El 5 es para Gastos
     *      - El 6 es para AltaGasto
     *      - El 7 es para DescripcionGasto
     *      - El 8 es para Venta
     *      - El 9 es para AltaVenta
     *      - El 10  es para DescripcionVenta
     */
    private int panelA;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.moverVentana();
        this.getTemperatura();
        this.mostrarConejas();

        this.listaEventos.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                eventoSeleccionado = listaEventos.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2) {
                    modificarEventoConeja();
                }
            }
        });
    }

    @FXML
    private void mostrarConejas() {
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

        if (this.almacen == null) {
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
            if (t.getValue())
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
    private void mostrarGastos() {

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
            if (t.getValue())
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
    private void mostrarEventosConeja(int id) {

        this.panel = 4;
        this.panelConejas.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAlmacen.setVisible(false);
        this.panelEventos.setVisible(true);
        listaEventos.setItems(null);
        spinnerEventos.setVisible(true);

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


            this.pintaEventosConeja(id);
        });

        new Thread(t).start();

    }

    @FXML
    private void mostrarAyuda() {
        this.panel = 5;
        this.panelA = 0;
        this.panelConejas.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAlmacen.setVisible(false);
        this.panelEventos.setVisible(false);
        this.panelAyuda.setVisible(true);
    }

    @FXML
    private void siguiente() {
        switch (panelA) {
            case 0 -> {
                this.conejasP.setVisible(false);
                this.altaConejasP.setVisible(true);
                this.panelA = 1;
            }
            case 1 -> {
                altaConejasP.setVisible(false);
                eventoConejaP.setVisible(true);
                this.panelA = 2;
            }
            case 2 -> {
                eventoConejaP.setVisible(false);
                altaEventoConejaP.setVisible(true);
                this.panelA = 3;
            }
            case 3 -> {
                altaEventoConejaP.setVisible(false);
                almacenP.setVisible(true);
                this.panelA = 4;
            }
            case 4 -> {
                almacenP.setVisible(false);
                gastosP.setVisible(true);
                this.panelA = 5;
            }
            case 5 -> {
                this.gastosP.setVisible(false);
                this.altaGastosP.setVisible(true);
                this.panelA = 6;
            }
            case 6 -> {
                this.altaGastosP.setVisible(false);
                this.descripcionGasto.setVisible(true);
                this.panelA = 7;
            }
            case 7 -> {
                this.descripcionGasto.setVisible(false);
                this.ventaP.setVisible(true);
                this.panelA = 8;
            }
            case 8 -> {
                this.ventaP.setVisible(false);
                this.altaVentaP.setVisible(true);
                this.panelA = 9;
            }
            case 9 -> {
                this.altaVentaP.setVisible(false);
                this.descripcionVentaP.setVisible(true);
                this.panelA = 10;
            }
            case 10 -> {
                this.descripcionVentaP.setVisible(false);
                this.conejasP.setVisible(true);
                this.panelA = 0;
            }
        }
    }

    @FXML
    private void anterior() {
        switch (panelA) {
            case 0 -> {
                this.conejasP.setVisible(false);
                this.descripcionVentaP.setVisible(true);
                this.panelA = 10;
            }
            case 1 -> {
                this.altaConejasP.setVisible(false);
                this.conejasP.setVisible(true);
                this.panelA = 0;
            }
            case 2 -> {
                this.eventoConejaP.setVisible(false);
                this.altaConejasP.setVisible(true);
                this.panelA = 1;
            }
            case 3 -> {
                this.altaEventoConejaP.setVisible(false);
                this.eventoConejaP.setVisible(true);
                this.panelA = 2;
            }
            case 4 -> {
                this.almacenP.setVisible(false);
                this.altaEventoConejaP.setVisible(true);
                this.panelA = 3;
            }
            case 5 -> {
                this.gastosP.setVisible(false);
                this.almacenP.setVisible(true);
                this.panelA = 4;
            }
            case 6 -> {
                this.altaGastosP.setVisible(false);
                this.gastosP.setVisible(true);
                this.panelA = 5;
            }
            case 7 -> {
                this.descripcionGasto.setVisible(false);
                this.altaGastosP.setVisible(true);
                this.panelA = 6;
            }
            case 8 -> {
                this.ventaP.setVisible(false);
                this.descripcionGasto.setVisible(true);
                this.panelA = 7;
            }
            case 9 -> {
                this.altaVentaP.setVisible(false);
                this.ventaP.setVisible(true);
                this.panelA = 9;
            }
            case 10 -> {
                this.descripcionVentaP.setVisible(false);
                this.altaVentaP.setVisible(true);
                this.panelA = 9;
            }
        }
    }

    private void pintaEventosConeja(int id) {

        this.listaEventos.setItems(null);
        this.listaEventos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.fecha.setCellValueFactory(f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getFecha())));
        this.tipo.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getTipoEventoConeja().toString()));

        Task<FilteredList<EventoConeja>> listar = new Task<>() {
            @Override
            protected FilteredList<EventoConeja> call() throws Exception {
                FilteredList<EventoConeja> filteredData = new FilteredList<>(FXCollections.observableArrayList(eventoConejaDAO.listar(id)), p -> true);
                SortedList<EventoConeja> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(listaEventos.comparatorProperty());
                listaEventos.setItems(sortedData);
                return filteredData;
            }

        };

        listar.setOnSucceeded(workerStateEvent1 -> {
            buscarEvento.textProperty().addListener((prop, old, text) -> {
                listar.getValue().setPredicate(eventos -> {
                    if (text == null || text.isEmpty()) return true;

                    String fecha = String.valueOf(eventos.getFecha());
                    String tipo = eventos.getTipoEventoConeja().toString();
                    return fecha.contains(text) || tipo.contains(text);
                });
            });
            spinnerEventos.setVisible(false);
        });


        idEscondido.setText(String.valueOf(id));
        new Thread(listar).start();
    }

    @FXML
    private void salir(ActionEvent event) throws IOException {
        Task<Parent> cargarVista = new Task<>() {
            @Override
            protected Parent call() throws Exception {
                return FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
            }
        };

        cargarVista.setOnSucceeded(workerStateEvent -> {
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(cargarVista.getValue(), 1200, 750);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(Ventana.color);
            thisStage.setScene(scene);
        });

        Task<Boolean> t = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                new UsuarioDAO().cerrarSesion(LoginController.getUsuarioActual().getNombreUsuario());
                return null;
            }
        };

        new Thread(t).start();
        new Thread(cargarVista).start();
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
        scene.getStylesheets().add(Ventana.color);

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
    private void modificarEventoConeja() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaEventoConeja.fxml"));
            Parent root = loader.load();

            EventoConeja eventoConeja = listaEventos.getSelectionModel().getSelectedItem();

            Label titulo = (Label) root.lookup("#titulo");
            titulo.setText("Modificar evento de coneja: " + eventoConeja.getIdConeja());

            JFXDatePicker fecha = (JFXDatePicker) root.lookup("#fecha");
            fecha.setValue((eventoConeja.getFecha()).toLocalDate());

            JFXComboBox tipo = (JFXComboBox) root.lookup("#tipoEventoConeja");
            tipo.getItems().addAll(EventoConeja.TipoEventoConeja.values());
            tipo.setValue(eventoConeja.getTipoEventoConeja());

            Scene scene = new Scene(root);
            scene.setUserData(eventoSeleccionado);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(Ventana.color);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);

            stage.show();
            stage.setOnHidden(windowEvent -> {
                this.recargar();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void altaGasto(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaGasto.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Ventana.color);
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
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setUserData(Venta.TipoVenta.Ganaderia);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Ventana.color);

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
    private void altaEvento(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaEventoConeja.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Ventana.color);

        JFXButton boton = (JFXButton) actionEvent.getSource();
        Label idConeja = (Label) boton.getScene().lookup("#idEscondido");

        JFXComboBox tipoEventoConeja = (JFXComboBox) root.lookup("#tipoEventoConeja");
        tipoEventoConeja.getItems().addAll(EventoConeja.TipoEventoConeja.values());

        scene.setUserData(idConeja.getText());

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
    private void eliminarEvento() {
        eventoConejaDAO.eliminar(eventoSeleccionado, LoginController.getUsuarioActual().getNombreUsuario());
        this.recargar();
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
            case 4:
                this.listaEventos.setItems(null);
                this.pintaEventosConeja((Integer.valueOf(idEscondido.getText())));
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
                borrar.setOnAction(e -> {
                    conejaDAO.eliminar(Integer.parseInt(id.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                //Eventos
                JFXButton gestionarEventos = (JFXButton) nodesC[i].lookup("#botonEventos");
                gestionarEventos.setOnAction(e -> {
                    mostrarEventosConeja(Integer.parseInt(id.getText()));
                });

                nodesC[i].getStyleClass().add(Ventana.color);

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
                borrar.setOnAction(e -> {
                    ventaDAO.eliminar(Integer.parseInt(id.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                //Desc
                JFXButton verDesc = (JFXButton) nodesV[i].lookup("#botonDesc");
                verDesc.setOnAction(e -> {
                    try {
                        Image img = new Image(this.getClass().getClassLoader().getResource("img/venta.png").toURI().toString());
                        pintarDescripcion(ventaDAO.buscar(Integer.parseInt(id.getText())).getDescripcion(), img);
                    } catch (IOException | URISyntaxException ioException) {
                        ioException.printStackTrace();
                    }
                });

                nodesV[i].getStyleClass().add(Ventana.color);


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
                borrar.setOnAction(e -> {
                    gastoDAO.eliminar(Integer.parseInt(id.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                //Desc
                JFXButton verDesc = (JFXButton) nodesG[i].lookup("#botonDesc");
                verDesc.setOnAction(e -> {
                    try {
                        Image img = new Image(this.getClass().getClassLoader().getResource("img/gasto.png").toURI().toString());
                        pintarDescripcion(gastoDAO.buscar(Integer.parseInt(id.getText())).getDescripcion(), img);
                    } catch (IOException | URISyntaxException ioException) {
                        ioException.printStackTrace();
                    }
                });

                nodesG[i].getStyleClass().add(Ventana.color);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.listaGastos.getChildren().addAll(nodesG);
    }

    private void pintarDescripcion(String d, Image i) throws IOException {
        Parent loader = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/descripcion.fxml"));
        Scene scene = new Scene(loader);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Ventana.color);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

        JFXTextArea desc = (JFXTextArea) scene.lookup("#descripcion");
        desc.setText(d);

        ImageView img = (ImageView) scene.lookup("#imagen");
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
