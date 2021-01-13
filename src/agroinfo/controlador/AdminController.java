package agroinfo.controlador;

import agroinfo.modelo.dao.GastoDAO;
import agroinfo.modelo.dao.UsuarioDAO;
import agroinfo.modelo.dao.VentaDAO;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Usuario;
import agroinfo.modelo.vo.Venta;
import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final GastoDAO gastoDAO = new GastoDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    @FXML
    public VBox listaGastos;

    @FXML
    public VBox listaVentas;

    @FXML
    public VBox listaUsuarios;

    @FXML
    public VBox listaAuditoria;

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

    //Lista de conejas
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
     *      - El 2 es para Auditoria
     */
    private int panel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mostrarUsuarios();
    }

    @FXML
    private void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent admin = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        Scene scene = new Scene(admin, 1200, 750);
        scene.getStylesheets().add("css/darkGreen.css");
        thisStage.setScene(scene);

        usuarioDAO.cerrarSesion(LoginController.getUsuarioActual().getNombreUsuario());
    }

    @FXML
    void crearUsuario(ActionEvent event){
        Usuario u = new Usuario("pepe","pepe", Usuario.TipoUsuario.Agricultor);

    }

    @FXML
    void borrarUsuario(ActionEvent event){

    }

    @FXML
    void mostrarAuditoria() {
    }

    @FXML
    void mostrarGastos() {
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
    void mostrarUsuarios() {
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
            this.panel = 2;
            this.panelGastos.setVisible(false);
            this.panelVentas.setVisible(false);
            this.panelAuditoria.setVisible(false);
            this.panelUsuarios.setVisible(true);
        });

        new Thread(t).start();
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


