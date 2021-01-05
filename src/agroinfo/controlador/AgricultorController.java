package agroinfo.controlador;

import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.Maquinaria;
import agroinfo.modelo.vo.Parcela;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { parcelaAction(new ActionEvent()); }

    @FXML
    void altaParcela(ActionEvent event) {

    }

    @FXML
    void parcelaAction(ActionEvent event) {

        this.listaParcelas.getChildren().clear();
        this.panelMaquinaria.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelParcelas.setVisible(true);

        List<Parcela> parcelas = parcelaDAO.listar();
        Node[] nodes = new Node[parcelas.size()];

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

    @FXML
    void maquinariaAction(ActionEvent event) {

        this.listaMaquinaria.getChildren().clear();
        this.panelParcelas.setVisible(false);
        this.panelMaquinaria.setVisible(true);

        ArrayList<String[]> lista = maquinariaDAO.listarConEventos();
        Node[] nodes = new Node[lista.size()];

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

    @FXML
    void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent agricultor = FXMLLoader.load(getClass().getResource("../vista/login.fxml"));
        thisStage.setScene(new Scene(agricultor, 1200  , 750));

    }
}
