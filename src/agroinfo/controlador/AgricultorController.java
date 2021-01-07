package agroinfo.controlador;

import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Maquinaria;
import agroinfo.modelo.vo.Parcela;
import agroinfo.modelo.vo.Venta;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AgricultorController implements Initializable {
    private final ParcelaDAO parcelaDAO = new ParcelaDAO();
    private final MaquinariaDAO maquinariaDAO = new MaquinariaDAO();
    private final EventoDAO eventoDAO = new EventoDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final GastoDAO gastoDAO = new GastoDAO();

    @FXML
    private StackPane sp;

    @FXML
    private JFXButton botonParcela;

    @FXML
    private JFXButton botonMaquinaria;

    @FXML
    private JFXButton botonGastos;

    @FXML
    private JFXButton botonVentas;

    @FXML
    private JFXButton botonSalir;

    @FXML
    private JFXTextField buscarP;

    @FXML
    private JFXTextField buscarM;

    @FXML
    private JFXTextField buscarV;

    @FXML
    private JFXTextField buscarG;

    @FXML
    private Pane panelMaquinaria;

    @FXML
    private Pane panelGastos;

    @FXML
    private Pane panelVentas;

    @FXML
    private Pane panelParcelas;

    @FXML
    private JFXButton altaParcela;

    @FXML
    private JFXButton altaMaquinaria;

    @FXML
    private VBox listaMaquinaria;

    @FXML
    private VBox listaParcelas;

    @FXML
    private JFXButton altaGasto;

    @FXML
    private VBox listaGastos;

    @FXML
    private JFXButton altaVenta;

    @FXML
    private VBox listaVentas;

    /*
     * Esta variable indica en que vista esta el programa para facilitar el metodo de buscar()
     *      - El 0 es para Parcelas
     *      - El 1 es para Maquinaria
     *      - El 2 es para Ventas
     *      - El 3 es para Gastos
     */
    private int panel;

    // Lista de las parcelas
    private List<Parcela> parcelas = new ArrayList<Parcela>();

    private List<Parcela> listarP(){
        return parcelaDAO.listar();
    }

    private Node[] nodesP;

    // Lista de las maquinas
    private ArrayList<String[]> lista = new ArrayList<String[]>();

    private ArrayList<String[]> listarM(){
        return maquinariaDAO.listarConEventos();
    }

    private Node[] nodesM;

    // Lista de las ventas
    private List<Venta> ventas = new ArrayList<Venta>();

    private List<Venta> listarV(){
        return ventaDAO.listar();
    }

    private Node[] nodesV;

    // Lista de los gastos
    private List<Gasto> gastos = new ArrayList<Gasto>();

    private List<Gasto> listarG(){
        return gastoDAO.listar();
    }

    private Node[] nodesG;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { parcelaAction(); }

    @FXML
    void altaParcela(ActionEvent event) {

    }

    @FXML
    void parcelaAction() {
        this.panel = 0;
        this.listaParcelas.getChildren().clear();
        this.panelMaquinaria.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelParcelas.setVisible(true);

        if(this.parcelas.isEmpty()) {
            this.parcelas = this.listarP();
            this.nodesP = new Node[parcelas.size()];
        }

        this.pintaParcela(parcelas, nodesP);
    }

    @FXML
    void maquinariaAction() {
        this.panel = 1;
        this.listaMaquinaria.getChildren().clear();
        this.panelParcelas.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelMaquinaria.setVisible(true);

        if(this.lista.isEmpty()) {
            this.lista = this.listarM();
            this.nodesM = new Node[lista.size()];
        }

        this.pintaMaquinaria(lista, nodesM);
    }

    @FXML
    void ventaAction() {
        this.panel = 2;
        this.listaVentas.getChildren().clear();
        this.panelMaquinaria.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelParcelas.setVisible(false);
        this.panelVentas.setVisible(true);

        if(this.ventas.isEmpty()) {
            this.ventas = this.listarV();
            this.nodesV = new Node[ventas.size()];
        }

        this.pintaVenta(ventas, nodesV);
    }

    @FXML
    void gastoAction(){
        this.panel = 3;
        this.listaGastos.getChildren().clear();
        this.panelMaquinaria.setVisible(false);
        this.panelParcelas.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelGastos.setVisible(true);

        if(this.gastos.isEmpty()) {
            this.gastos = this.listarG();
            this.nodesG = new Node[gastos.size()];
        }

        this.pintaGasto(gastos, nodesG);
    }

    @FXML
    void buscar(KeyEvent event){

        switch (this.panel) {
            case 0 -> {
                this.listaParcelas.getChildren().clear();
                this.listaParcelas.getChildren().addAll(nodesP);
                this.listaParcelas.getChildren().removeIf(new Predicate<Node>() {
                    @Override
                    public boolean test(Node node) {
                        Label id = (Label) node.lookup("#id");
                        return !id.getText().matches(buscarP.getText() + ".*");
                    }
                });
            }
            case 1 -> {
                this.listaMaquinaria.getChildren().clear();
                this.listaMaquinaria.getChildren().addAll(nodesM);
                this.listaMaquinaria.getChildren().removeIf(new Predicate<Node>() {
                    @Override
                    public boolean test(Node node) {
                        Label id = (Label) node.lookup("#id");
                        return !id.getText().matches(buscarM.getText() + ".*");
                    }
                });
            }
            case 2 -> {
                this.listaVentas.getChildren().clear();
                this.listaVentas.getChildren().addAll(nodesV);
                this.listaVentas.getChildren().removeIf(new Predicate<Node>() {
                    @Override
                    public boolean test(Node node) {
                        Label id = (Label) node.lookup("#id");
                        return !id.getText().matches(buscarV.getText() + ".*");
                    }
                });
            }
            case 3 -> {
                this.listaGastos.getChildren().clear();
                this.listaGastos.getChildren().addAll(nodesG);
                this.listaGastos.getChildren().removeIf(new Predicate<Node>() {
                    @Override
                    public boolean test(Node node) {
                        Label id = (Label) node.lookup("#id");
                        return !id.getText().matches(buscarG.getText() + ".*");
                    }
                });
            }
        }
    }

    private void pintaParcela(List<Parcela> parcelas, Node[] nodes){

        for (int i = 0; i < nodes.length; i++) {
            try {
                nodes[i] = FXMLLoader.load(this.getClass().getResource("../vista/parcela.fxml"));

                //Id
                Label id = (Label) nodes[i].lookup("#id");
                id.setText(String.valueOf(parcelas.get(i).getId()));

                //Tipo Parcela
                Label tipoParcela = (Label) nodes[i].lookup("#tParcela");
                String tp = parcelas.get(i).getTipoParcela().toString();
                if (tp != null) tipoParcela.setText(tp);

                //Tipo Cultivo
                Label tipoCultivo = (Label) nodes[i].lookup("#tCultivo");
                String tc = parcelas.get(i).getTipoCultivo().toString();
                if (tc != null) tipoCultivo.setText(tc);

                //Produccion
                Label produccion = (Label) nodes[i].lookup("#produccion");
                String pr = String.valueOf(parcelas.get(i).getProduccion());
                produccion.setText(pr);

                //Excedente
                Label excedente = (Label) nodes[i].lookup("#excedente");
                String ex = String.valueOf(parcelas.get(i).getExcedente());
                excedente.setText(ex);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        listaParcelas.getChildren().addAll(nodes);
    }

    private void pintaMaquinaria(ArrayList<String[]> lista, Node[] nodes){

        int i = 0;
        for(String[] s: lista){
            try {
                nodes[i] = FXMLLoader.load(this.getClass().getResource("../vista/maquinaria.fxml"));

                Label id = (Label)nodes[i].lookup("#id");
                id.setText(s[0]);

                Label nombre = (Label)nodes[i].lookup("#nombre");
                if(s[1]!= null)
                    nombre.setText(s[1]);

                Label evento = (Label)nodes[i].lookup("#evento");
                if(!s[2].equals("null"))
                    evento.setText(s[2]);

            }catch (IOException e){
                e.printStackTrace();
            }

            i++;
        }
        listaMaquinaria.getChildren().addAll(nodes);
    }

    private void pintaVenta(List<Venta> ventas, Node[] nodes){

        for (int i = 0; i < nodes.length; i++) {
            try {
                nodes[i] = FXMLLoader.load(this.getClass().getResource("../vista/venta.fxml"));

                //Id
                Label id = (Label) nodes[i].lookup("#id");
                id.setText(String.valueOf(ventas.get(i).getId()));

                //Cantidad
                Label cant = (Label) nodes[i].lookup("#cantidad");
                cant.setText(String.valueOf(ventas.get(i).getCantidad()));

                //Precio Unitario
                Label pu = (Label) nodes[i].lookup("#pu");
                pu.setText(String.valueOf(ventas.get(i).getPrecioUnitario()));

                //Total
                Label total = (Label) nodes[i].lookup("#total");
                total.setText(String.valueOf((ventas.get(i).getCantidad())*(ventas.get(i).getPrecioUnitario())));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        listaVentas.getChildren().addAll(nodes);

    }

    private void pintaGasto(List<Gasto> gastos, Node[] nodes){

        for (int i = 0; i < gastos.size(); i++) {
            try {
                nodes[i] = FXMLLoader.load(this.getClass().getResource("../vista/gasto.fxml"));

                //Id
                Label id = (Label) nodes[i].lookup("#id");
                id.setText(String.valueOf(gastos.get(i).getId()));

                //Importe
                Label importe = (Label) nodes[i].lookup("#importe");
                importe.setText(String.valueOf(gastos.get(i).getImporte()));

                //TipoGasto
                Label tGasto = (Label) nodes[i].lookup("#tipoGasto");
                tGasto.setText(String.valueOf(gastos.get(i).getTipoGasto()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        listaGastos.getChildren().addAll(nodes);
    }

    @FXML
    void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent agricultor = FXMLLoader.load(getClass().getResource("../vista/login.fxml"));
        thisStage.setScene(new Scene(agricultor, 1200  , 750));

    }
}
