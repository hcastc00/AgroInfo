package agroinfo.controlador;

import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.Evento;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Usuario;
import agroinfo.modelo.vo.Venta;
import agroinfo.vista.Ventana;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import com.mysql.jdbc.log.Log;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final GastoDAO gastoDAO = new GastoDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final RegistroDAO registroDAO = new RegistroDAO();

    @FXML
    public VBox listaGastos;

    @FXML
    public VBox listaVentas;

    @FXML
    public VBox listaUsuarios;

    @FXML
    private VBox panelAdmin;

    @FXML
    private Pane panelUsuarios;

    @FXML
    private Pane panelAyuda;

    @FXML
    private Pane panelGastos;

    @FXML
    private Pane panelAuditoria;

    @FXML
    private Pane panelVentas;

    @FXML
    private Pane usuariosP;

    @FXML
    private Pane altaUsuario;

    @FXML
    private Pane gastosP;

    @FXML
    private Pane ventasP;

    @FXML
    private Pane auditoria;

    @FXML
    private TableView<String[]> listaAuditoria;

    @FXML
    private TableColumn<String[], String> usuario;

    @FXML
    private TableColumn<String[], String> fecha;

    @FXML
    private TableColumn<String[], String> tipo;

    @FXML
    private TableColumn<String[], String> log;

    @FXML
    private TextField buscarAuditoria;

    @FXML
    private TextField buscarGasto;

    @FXML
    private TextField buscarVenta;

    @FXML
    private TextField buscarUsuario;

    @FXML
    private JFXSpinner spinnerEventos;

    //Lista de logs
    private List<String[]> logs;
    private Node[] nodesL;

    //Lista de usuario
    private List<String[]> usuarios;
    private Node[] nodesU;

    // Lista de los gastos
    private List<Gasto> gastos;
    private Node[] nodesG;

    // Lista de las ventas
    private List<Venta> ventas;
    private Node[] nodesV;

    @FXML
    private AnchorPane root;

    /*
     * Esta variable indica en que vista esta el programa para facilitar metodos
     *      - El 0 es para Usuarios
     *      - El 1 es para Gastos
     *      - El 2 es para Ventas
     *      - El 3 es para Auditoria
     *      - El 4 es para Ayuda
     */
    private int panel;

    /*
     * Esta variable indica el panel de la ayuda
     *      - El 0 es para Usuario
     *      - El 1 es para AltaUsuario
     *      - El 2 es para Gastos
     *      - El 3 es para Ventas
     *      - El 4 es para Auditoria
     */
    private int panelA;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moverVentana();
        mostrarUsuarios();
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
                usuarioDAO.cerrarSesion(LoginController.getUsuarioActual().getNombreUsuario());
                return null;
            }
        };

        new Thread(t).start();
        new Thread(cargarVista).start();
    }

    @FXML
    void crearUsuario(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaUsuario.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Ventana.color);

        JFXComboBox tipoParcela = (JFXComboBox)root.lookup("#tipo");
        tipoParcela.getItems().addAll(Usuario.TipoUsuario.values());

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
    void borrarUsuario(ActionEvent event){

    }

    @FXML
    void mostrarAuditoria() {
        this.panel = 3;
        this.panelUsuarios.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAyuda.setVisible(false);
        this.panelAyuda.setVisible(false);
        this.panelAuditoria.setVisible(true);

        listaAuditoria.setItems(null);
        spinnerEventos.setVisible(true);

        this.pintaAuditoria();
    }

    @FXML
    private void mostrarGastos() {
        Task<Boolean> t = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                if (gastos == null || gastos.isEmpty()) {
                    gastos = gastoDAO.listar();
                    nodesG = new Node[gastos.size()];
                    return true;
                }
                return false;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if(t.getValue())
                this.pintaGasto();
            this.panel = 2;
            this.panelUsuarios.setVisible(false);
            this.panelAuditoria.setVisible(false);
            this.panelVentas.setVisible(false);
            this.panelAyuda.setVisible(false);
            this.panelAyuda.setVisible(false);
            this.panelGastos.setVisible(true);
        });

        new Thread(t).start();
    }

    @FXML
    private void mostrarUsuarios() {

        this.panel = 0;
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAuditoria.setVisible(false);
        this.panelAyuda.setVisible(false);
        this.panelAyuda.setVisible(false);
        this.panelUsuarios.setVisible(true);

        if (usuarios == null || usuarios.isEmpty()) {
            usuarios = usuarioDAO.listar();
            nodesU = new Node[usuarios.size()];
            this.pintaUsuario();
        }
    }

    @FXML
    private void mostrarVentas() {

        Task<Boolean> t = new Task<Boolean>() {
            @Override
            protected Boolean call() {

                if (ventas == null || ventas.isEmpty()) {
                    ventas = ventaDAO.listar();
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
            this.panelUsuarios.setVisible(false);
            this.panelGastos.setVisible(false);
            this.panelAuditoria.setVisible(false);
            this.panelAyuda.setVisible(false);
            this.panelAyuda.setVisible(false);
            this.panelVentas.setVisible(true);
        });

        new Thread(t).start();
    }

    @FXML
    private void mostrarAyuda() {
        this.panel = 4;
        this.panelA = 0;
        this.panelUsuarios.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelAuditoria.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAyuda.setVisible(true);
    }

    @FXML
    private void siguiente(){
        switch (panelA) {
            case 0 -> {
                usuariosP.setVisible(false);
                altaUsuario.setVisible(true);
                this.panelA = 1;
            }
            case 1 -> {
                altaUsuario.setVisible(false);
                gastosP.setVisible(true);
                this.panelA = 2;
            }
            case 2 -> {
                gastosP.setVisible(false);
                ventasP.setVisible(true);
                this.panelA = 3;
            }
            case 3 -> {
                ventasP.setVisible(false);
                auditoria.setVisible(true);
                this.panelA = 4;
            }
            case 4 -> {
                auditoria.setVisible(false);
                usuariosP.setVisible(true);
                this.panelA = 0;
            }
        }
    }

    @FXML
    private void anterior(){
        switch (panelA) {
            case 0 -> {
                usuariosP.setVisible(false);
                auditoria.setVisible(true);
                this.panelA = 4;
            }
            case 1 -> {
                altaUsuario.setVisible(false);
                usuariosP.setVisible(true);
                this.panelA = 0;
            }
            case 2 -> {
                gastosP.setVisible(false);
                altaUsuario.setVisible(true);
                this.panelA = 1;
            }
            case 3 -> {
                ventasP.setVisible(false);
                gastosP.setVisible(true);
                this.panelA = 2;
            }
            case 4 -> {
                auditoria.setVisible(false);
                ventasP.setVisible(true);
                this.panelA = 3;
            }
        }
    }

    @FXML
    private void buscar() {
        switch (this.panel) {
            case 0 -> {
                this.listaUsuarios.getChildren().clear();
                this.listaUsuarios.getChildren().addAll(nodesU);
                this.listaUsuarios.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    return !id.getText().matches(buscarUsuario.getText() + ".*");
                });
            }
            case 1 -> {
                this.listaGastos.getChildren().clear();
                this.listaGastos.getChildren().addAll(nodesG);
                this.listaGastos.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    Label tipoGasto = (Label) node.lookup("#tipoGasto");
                    return (!id.getText().matches(buscarGasto.getText() + ".*")) &&
                            !tipoGasto.getText().matches(buscarGasto.getText() + ".*");
                });
            }
            case 2 -> {
                this.listaVentas.getChildren().clear();
                this.listaVentas.getChildren().addAll(nodesV);
                this.listaVentas.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    return !id.getText().matches(buscarVenta.getText() + ".*");
                });
            }
        }
    }

    //METODOS AUXILIARES
    private void pintaUsuario() {

        this.listaUsuarios.getChildren().clear();
        for (int i = 0; i < usuarios.size(); i++) {
            try {
                nodesU[i] = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/usuario.fxml"));

                //Id
                Label id = (Label) nodesU[i].lookup("#id");
                id.setText(usuarios.get(i)[0]);

                //Tipo
                Label tipo = (Label) nodesU[i].lookup("#tipo");
                tipo.setText(usuarios.get(i)[2]);

                //Ultimo inicio de sesion
                Label ultimoInicio = (Label) nodesU[i].lookup("#ultimoInicio");
                if(!usuarios.get(i)[4].equals("null"))
                    ultimoInicio.setText(usuarios.get(i)[4]);

                //Borrar
                JFXButton borrar = (JFXButton) nodesU[i].lookup("#botonBorrar");

                //Evita borrar el usuario MASTER y actual
                if(id.getText().equals(LoginController.getUsuarioActual().getNombreUsuario()) ||
                        id.getText().equals("admin"))
                    borrar.setVisible(false);

                borrar.setOnAction(e ->{
                    usuarioDAO.eliminar(id.getText(),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                //Imagen
                ImageView imagen = (ImageView) nodesU[i].lookup("#imagen");
                switch (Usuario.TipoUsuario.valueOf(usuarios.get(i)[2])){
                    case Ganadero ->
                            imagen.setImage(new Image((this.getClass().getClassLoader().getResource("img/ganadero.png")).toURI().toString()));

                    case Agricultor ->
                            imagen.setImage(new Image((this.getClass().getClassLoader().getResource("img/agricultor.png")).toURI().toString()));

                    case Administrador ->
                            imagen.setImage(new Image((this.getClass().getClassLoader().getResource("img/admin.png")).toURI().toString()));
                }

                nodesU[i].getStyleClass().add(Ventana.color);

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        this.listaUsuarios.getChildren().addAll(nodesU);
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
                borrar.setVisible(false);

                //Ver desc
                JFXButton verDesc = (JFXButton) nodesG[i].lookup("#botonDesc");
                verDesc.setOnAction(e ->{
                    try {
                        Image img = new Image(this.getClass().getClassLoader().getResource("img/gasto.png").toURI().toString());
                        pintarDescripcion(gastoDAO.buscar(Integer.parseInt(id.getText())).getDescripcion(),img);
                    } catch (URISyntaxException | IOException ioException) {
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
                borrar.setVisible(false);

                //Tipo venta
                VBox cajaTipo = (VBox) nodesV[i].lookup("#cajaTipo");
                Label tipo = (Label) nodesV[i].lookup("#tipo");
                tipo.setText(ventas.get(i).getTipo().toString());
                cajaTipo.setVisible(true);

                //Ver desc
                JFXButton verDesc = (JFXButton) nodesV[i].lookup("#botonDesc");
                verDesc.setOnAction(e ->{
                    try {
                        Image img = new Image(this.getClass().getClassLoader().getResource("img/venta.png").toURI().toString());
                        pintarDescripcion(ventaDAO.buscar(Integer.parseInt(id.getText())).getDescripcion(),img);
                    } catch (URISyntaxException | IOException ioException) {
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

    private void pintaAuditoria() {
        this.listaAuditoria.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.usuario.setCellValueFactory(u -> new ReadOnlyStringWrapper(u.getValue()[0]));
        this.fecha.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue()[1]));
        this.tipo.setCellValueFactory(t -> new ReadOnlyStringWrapper(t.getValue()[2]));
        this.log.setCellValueFactory(l -> new ReadOnlyStringWrapper(l.getValue()[3]));

        Task<FilteredList<String[]>> listar = new Task<>() {
            @Override
            protected FilteredList<String[]> call() throws Exception {

                FilteredList<String[]> filteredData = new FilteredList<>(FXCollections.observableArrayList(registroDAO.listar()), p -> true);
                SortedList<String[]> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(listaAuditoria.comparatorProperty());
                listaAuditoria.setItems(sortedData);
                return filteredData;
            };
        };

        listar.setOnSucceeded(workerStateEvent1 -> {
            buscarAuditoria.textProperty().addListener((prop, old, text) -> {
                listar.getValue().setPredicate(logs -> {
                    if (text == null || text.isEmpty()) return true;

                    String name = logs[0];
                    String fecha = logs[1];
                    String tipo = logs[2];
                    String log = logs[3];
                    return name.contains(text) || tipo.contains(text) || fecha.contains(text) || log.contains(text);
                });
            });
            spinnerEventos.setVisible(false);
        });

        new Thread(listar).start();
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

    @FXML
    private void recargar() {
        switch (this.panel) {
            case 0 -> {
                this.listaUsuarios.getChildren().clear();
                this.usuarios = usuarioDAO.listar();
                this.nodesU = new Node[usuarios.size()];
                this.pintaUsuario();
            }
            case 1 -> {
                this.listaGastos.getChildren().clear();
                this.gastos = gastoDAO.listar();
                this.nodesG = new Node[gastos.size()];
                this.pintaGasto();
            }
            case 2 -> {
                this.listaVentas.getChildren().clear();
                this.ventas = ventaDAO.listar();
                this.nodesV = new Node[ventas.size()];
                this.pintaVenta();
            }
            case 3 -> {
                this.pintaAuditoria();
            }
        }
    }

    private void pintarDescripcion(String d, Image i) throws IOException {
        Parent loader =  FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/descripcion.fxml"));
        Scene scene = new Scene(loader);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Ventana.color);

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

}


