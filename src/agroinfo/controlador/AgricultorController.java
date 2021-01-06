package agroinfo.controlador;

import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Maquinaria;
import agroinfo.modelo.vo.Parcela;
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
    private JFXTextField buscar;

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

        List<Parcela> parcelas = parcelaDAO.listar();
        Node[] nodes = new Node[parcelas.size()];
        this.pintaParcela(parcelas, nodes);
    }

    @FXML
    void maquinariaAction() {
        this.panel = 1;
        this.listaMaquinaria.getChildren().clear();
        this.panelParcelas.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelMaquinaria.setVisible(true);

        ArrayList<String[]> lista = maquinariaDAO.listarConEventos();
        Node[] nodes = new Node[lista.size()];
        this.pintaMaquinaria(lista, nodes);
    }

    @FXML
    void ventaAction() {
        this.panel = 2;
        this.listaVentas.getChildren().clear();
        this.panelMaquinaria.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelParcelas.setVisible(false);
        this.panelVentas.setVisible(true);

        List<Venta> ventas = ventaDAO.listar();
        Node[] nodes = new Node[ventas.size()];

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

    @FXML
    void gastoAction(){
        this.panel = 3;
        this.listaGastos.getChildren().clear();
        this.panelMaquinaria.setVisible(false);
        this.panelParcelas.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelGastos.setVisible(true);

        List<Gasto> gastos = gastoDAO.listar();
        Node[] nodes = new Node[gastos.size()];

        for (int i = 0; i < nodes.length; i++) {
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
    void buscar(KeyEvent event){

        if(event.getCode() == KeyCode.ENTER) {

            switch(this.panel){
                case 0:
                    this.listaParcelas.getChildren().clear();
                    String busqueda = this.buscar.getText();
                    if(busqueda != "") {
                        Parcela parcela = parcelaDAO.buscar(Integer.parseInt(busqueda));
                        List<Parcela> parcelas = new ArrayList<Parcela>(){{add(parcela);}};
                        Node node = null;
                        try {
                            node = FXMLLoader.load(this.getClass().getResource("../vista/parcela.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Node[] nodes = {node};
                        this.pintaParcela(parcelas, nodes);
                    }else{
                        this.parcelaAction();
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
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
                if(s[2]!= "null")
                    evento.setText(s[2]);

            }catch (IOException e){
                e.printStackTrace();
            }

            i++;
        }
        listaMaquinaria.getChildren().addAll(nodes);

    }

    private void pintaVenta(){

    }

    private void pintaGasto(){

    }

    @FXML
    void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent agricultor = FXMLLoader.load(getClass().getResource("../vista/login.fxml"));
        thisStage.setScene(new Scene(agricultor, 1200  , 750));

    }
}
