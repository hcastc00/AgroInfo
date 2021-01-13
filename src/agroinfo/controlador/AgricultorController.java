package agroinfo.controlador;

import agroinfo.modelo.conexion.ConexionOpenWheatherAPI;
import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.Gasto;
import agroinfo.modelo.vo.Parcela;
import agroinfo.modelo.vo.Usuario;
import agroinfo.modelo.vo.Venta;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AgricultorController implements Initializable {
    private final ParcelaDAO parcelaDAO = new ParcelaDAO();
    private final MaquinariaDAO maquinariaDAO = new MaquinariaDAO();
    private final EventoDAO eventoDAO = new EventoDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final GastoDAO gastoDAO = new GastoDAO();
    private final ConexionOpenWheatherAPI tiempo = new ConexionOpenWheatherAPI();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioActual = LoginController.getUsuarioActual();

    @FXML
    private Pane panelAlmacen;

    @FXML
    private JFXTextField buscarParcelas;

    @FXML
    private JFXTextField buscarMaquinaria;

    @FXML
    private JFXTextField buscarVentas;

    @FXML
    private JFXTextField buscarGastos;

    @FXML
    private Pane panelMaquinaria;

    @FXML
    private Pane panelGastos;

    @FXML
    private Pane panelVentas;

    @FXML
    private Pane panelParcelas;

    @FXML
    private VBox listaMaquinaria;

    @FXML
    private VBox listaParcelas;

    @FXML
    private VBox listaGastos;

    @FXML
    private VBox listaVentas;

    @FXML
    private AnchorPane root;

    /*
     * Esta variable indica en que vista esta el programa para facilitar el metodo de buscar()
     *      - El 0 es para Parcelas
     *      - El 1 es para Maquinaria
     *      - El 2 es para Ventas
     *      - El 3 es para Gastos
     *      - El 4 es para Almacen
     */
    private int panel;

    // Lista de las parcelas
    private List<Parcela> parcelas;
    private Node[] nodesP;

    // Lista de las maquinas
    private List<String[]> maquinas;
    private Node[] nodesM;

    // Lista de las ventas
    private List<Venta> ventas;
    private Node[] nodesV;

    // Lista de los gastos
    private List<Gasto> gastos;
    private Node[] nodesG;

    private List<Parcela> listarParcelas() {
        return parcelaDAO.listar();
    }

    private List<String[]> listarMaquinaria() {
        return maquinariaDAO.listarConEventos();
    }

    private List<Venta> listarVentas() {
        return ventaDAO.listar(Venta.TipoVenta.Agricultura);
    }

    private List<Gasto> listarGastos() {
        return gastoDAO.listar(Gasto.TipoGasto.Agricultura);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moverVentana();
        mostrarParcelas();
    }

    @FXML
    private void altaParcela(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaParcela.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        JFXComboBox tipoParcela = (JFXComboBox)root.lookup("tipoParcela");
        JFXComboBox tipoCultivo = (JFXComboBox)root.lookup("tipoCultivo");

        tipoParcela.getItems().addAll(Parcela.TipoParcela.values());
        tipoCultivo.getItems().addAll(Parcela.TipoCultivo.values());

        stage.show();
        stage.setOnHidden(windowEvent -> {
            this.recargar();
        });
    }

    @FXML
    public void modificarParcela(MouseEvent mouseEvent) throws IOException {

        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaParcela.fxml"));
                Parent root = (Parent) loader.load();

                Node node = (Node)mouseEvent.getSource();
                Label id = (Label)node.lookup("id");
                int idText = Integer.parseInt(id.getText());
                Parcela parcela = parcelaDAO.buscar(idText);

                JFXTextField idParcela = (JFXTextField) root.lookup("idParcela");
                idParcela.setEditable(false);

                JFXTextField latitud = (JFXTextField) root.lookup("latitud");
                JFXTextField longitud = (JFXTextField) root.lookup("longitud");
                JFXTextField tam = (JFXTextField) root.lookup("tam");

                JFXComboBox tipoParcela = (JFXComboBox)root.lookup("tipoParcela");
                JFXComboBox tipoCultivo = (JFXComboBox)root.lookup("tipoCultivo");

                latitud.setText(String.valueOf(parcela.getLatitud()));
                longitud.setText(String.valueOf(parcela.getLongitud()));
                tam.setText(String.valueOf(parcela.getTam()));

                tipoParcela.getItems().addAll(Parcela.TipoParcela.values());
                tipoCultivo.getItems().addAll(Parcela.TipoCultivo.values());

                tipoParcela.setValue(parcela.getTipoParcela());
                tipoCultivo.setValue(parcela.getTipoCultivo());


                Scene scene = new Scene(root);
                scene.setUserData("modificar");
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
        }


    }

    @FXML
    public void altaVenta(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaVenta.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setUserData(Venta.TipoVenta.Agricultura);
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
    public void verDescripcionVenta(ActionEvent actionEvent){

    }

    @FXML
    public void altaGasto(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaGasto.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setUserData(Gasto.TipoGasto.Agricultura);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage.setOnHidden(windowEvent -> {
            this.recargar();
        });
    }

    @FXML
    private void mostrarParcelas() {
        this.panel = 0;
        this.panelMaquinaria.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAlmacen.setVisible(false);
        this.panelParcelas.setVisible(true);

        if (this.parcelas == null || this.parcelas.isEmpty()) {
            this.parcelas = this.listarParcelas();
            this.nodesP = new Node[parcelas.size()];
            this.pintaParcela();
        }

    }

    @FXML
    private void altaMaquinaria(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaMaquinaria.fxml"));
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
    private void mostrarMaquinaria() {

        Task<Boolean> t = new Task<>() {
            @Override
            protected Boolean call() {

                if (maquinas == null || maquinas.isEmpty()) {
                    maquinas = listarMaquinaria();
                    nodesM = new Node[maquinas.size()];

                    return true;
                }
                return false;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if(t.getValue())
                this.pintaMaquinaria();
            this.panel = 1;
            this.panelParcelas.setVisible(false);
            this.panelGastos.setVisible(false);
            this.panelVentas.setVisible(false);
            this.panelAlmacen.setVisible(false);
            this.panelMaquinaria.setVisible(true);

        });

        new Thread(t).start();
    }

    @FXML
    private void mostrarVentas() {

        Task<Boolean> t = new Task<>() {
            @Override
            protected Boolean call() {

                if (ventas == null || ventas.isEmpty()) {
                    ventas = listarVentas();
                    nodesV = new Node[ventas.size()];
                    return true;
                }
                return false;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if(t.getValue())
                pintaVenta();
            this.panel = 2;
            this.panelMaquinaria.setVisible(false);
            this.panelGastos.setVisible(false);
            this.panelParcelas.setVisible(false);
            this.panelAlmacen.setVisible(false);
            this.panelVentas.setVisible(true);
        });

        new Thread(t).start();
    }

    @FXML
    private void mostrarGastos() {

        Task<Boolean> t = new Task<>() {
            @Override
            protected Boolean call() {

                if (gastos == null || gastos.isEmpty()) {
                    gastos = listarGastos();
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
            this.panelMaquinaria.setVisible(false);
            this.panelParcelas.setVisible(false);
            this.panelVentas.setVisible(false);
            this.panelAlmacen.setVisible(false);
            this.panelGastos.setVisible(true);
        });

        new Thread(t).start();
    }

    @FXML
    private void mostrarAlmacen() {
        this.panel = 4;
    }

    @FXML
    private void buscar() {
        switch (this.panel) {
            case 0 -> {
                this.listaParcelas.getChildren().clear();
                this.listaParcelas.getChildren().addAll(nodesP);
                this.listaParcelas.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    return !id.getText().matches(buscarParcelas.getText() + ".*");
                });
            }
            case 1 -> {
                this.listaMaquinaria.getChildren().clear();
                this.listaMaquinaria.getChildren().addAll(nodesM);
                this.listaMaquinaria.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    Label nombre = (Label) node.lookup("#nombre");
                    return (!id.getText().matches(buscarMaquinaria.getText() + ".*")) &&
                            !nombre.getText().matches(buscarMaquinaria.getText() + ".*");
                });
            }
            case 2 -> {
                this.listaVentas.getChildren().clear();
                this.listaVentas.getChildren().addAll(nodesV);
                this.listaVentas.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    return !id.getText().matches(buscarVentas.getText() + ".*");
                });
            }
            case 3 -> {
                this.listaGastos.getChildren().clear();
                this.listaGastos.getChildren().addAll(nodesG);
                this.listaGastos.getChildren().removeIf(node -> {
                    Label id = (Label) node.lookup("#id");
                    return !id.getText().matches(buscarGastos.getText() + ".*");
                });
            }
        }
    }

    @FXML
    private void recargar() {
        switch (this.panel) {
            case 0 -> {
                this.listaParcelas.getChildren().clear();
                this.parcelas = this.listarParcelas();
                this.nodesP = new Node[parcelas.size()];
                this.pintaParcela();
            }
            case 1 -> {
                this.listaMaquinaria.getChildren().clear();
                this.maquinas = listarMaquinaria();
                this.nodesM = new Node[maquinas.size()];
                this.pintaMaquinaria();
            }
            case 2 -> {
                this.listaVentas.getChildren().clear();
                this.ventas = listarVentas();
                this.nodesV = new Node[ventas.size()];
                this.pintaVenta();
            }
            case 3 -> {
                this.listaGastos.getChildren().clear();
                this.gastos = listarGastos();
                this.nodesG = new Node[gastos.size()];
                this.pintaGasto();
            }
        }
    }

    @FXML
    private void salir(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent agricultor = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/login.fxml"));
        Scene scene = new Scene(agricultor, 1200, 750);
        scene.getStylesheets().add("css/darkGreen.css");
        thisStage.setScene(scene);

        usuarioDAO.cerrarSesion(usuarioActual.getNombreUsuario());

    }

    //METODOS AUXILIARES

    private void pintaParcela() {
        this.listaParcelas.getChildren().clear();
        for (int i = 0; i < nodesP.length; i++) {
            try {
                nodesP[i] = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/parcela.fxml"));

                //Id
                Label id = (Label) nodesP[i].lookup("#id");
                id.setText(String.valueOf(parcelas.get(i).getId()));

                //Tipo Parcela
                Label tipoParcela = (Label) nodesP[i].lookup("#tParcela");
                String tp = parcelas.get(i).getTipoParcela().toString();
                if (tp != null) tipoParcela.setText(tp);

                //Tipo Cultivo
                Label tipoCultivo = (Label) nodesP[i].lookup("#tCultivo");
                String tc = parcelas.get(i).getTipoCultivo().toString();
                if (tc != null) tipoCultivo.setText(tc);

                //Produccion
                Label produccion = (Label) nodesP[i].lookup("#produccion");
                String pr = String.valueOf(parcelas.get(i).getProduccion());
                produccion.setText(pr);

                //Excedente
                Label excedente = (Label) nodesP[i].lookup("#excedente");
                String ex = String.valueOf(parcelas.get(i).getExcedente());
                excedente.setText(ex);

                //Imagen
                ImageView image = (ImageView) nodesP[i].lookup("#image");
                String[] ico = new String[2];
                try {
                    ico = tiempo.getTiempo(parcelas.get(i).getLatitud(), parcelas.get(i).getLongitud());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setImage(new Image("http://openweathermap.org/img/wn/" + ico[0]));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.listaParcelas.getChildren().addAll(nodesP);
    }

    private void pintaMaquinaria() {
        this.listaMaquinaria.getChildren().clear();
        int i = 0;
        for (String[] s : maquinas) {
            try {
                nodesM[i] = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/maquinaria.fxml"));

                Label id = (Label) nodesM[i].lookup("#id");
                id.setText(s[0]);

                Label nombre = (Label) nodesM[i].lookup("#nombre");
                if (s[1] != null)
                    nombre.setText(s[1]);

                Label evento = (Label) nodesM[i].lookup("#evento");
                if (!s[2].equals("null"))
                    evento.setText(s[2]);

            } catch (IOException e) {
                e.printStackTrace();
            }

            i++;
        }
        this.listaMaquinaria.getChildren().addAll(nodesM);
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


}
