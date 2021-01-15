package agroinfo.controlador;

import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Usuario;
import agroinfo.modelo.vo.Venta;
import agroinfo.vista.Ventana;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
    private Pane panelGastos;

    @FXML
    private Pane panelAuditoria;

    @FXML
    private Pane panelVentas;

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
    private TextField buscar;

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
     */
    private int panel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moverVentana();
        mostrarUsuarios();
    }

    @FXML
    private void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent admin = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        Scene scene = new Scene(admin, 1200, 750);
        scene.getStylesheets().add(Ventana.color);
        thisStage.setScene(scene);

        usuarioDAO.cerrarSesion(LoginController.getUsuarioActual().getNombreUsuario());
    }

    @FXML
    void crearUsuario(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaUsuario.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

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
        this.panel = 2;
        this.panelUsuarios.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAuditoria.setVisible(true);
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
            this.panelVentas.setVisible(true);
        });

        new Thread(t).start();
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

        FilteredList<String[]> filteredData = new FilteredList<>(FXCollections.observableArrayList(registroDAO.listar()), p -> true);
        SortedList<String[]> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(this.listaAuditoria.comparatorProperty());
        this.listaAuditoria.setItems(sortedData);

        buscar.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(logs -> {
                if(text == null || text.isEmpty()) return true;

                String name = logs[0];
                String fecha = logs[1];
                String tipo = logs[2];
                String log = logs[3];
                return name.contains(text) || tipo.contains(text) || fecha.contains(text) || log.contains(text);
            });
        });

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


}


