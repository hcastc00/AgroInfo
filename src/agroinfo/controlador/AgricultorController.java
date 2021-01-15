package agroinfo.controlador;

import agroinfo.modelo.conexion.ConexionOpenWheatherAPI;
import agroinfo.modelo.dao.*;
import agroinfo.modelo.vo.*;
import agroinfo.vista.Ventana;
import com.jfoenix.controls.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
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
    private JFXTextField buscarEvento;

    @FXML
    private Pane panelMaquinaria;

    @FXML
    private Pane panelGastos;

    @FXML
    private Pane panelVentas;

    @FXML
    private Pane panelParcelas;

    @FXML
    private Pane panelEventos;

    @FXML
    private VBox listaMaquinaria;

    @FXML
    private VBox listaParcelas;

    @FXML
    private VBox listaGastos;

    @FXML
    private VBox listaAlmacen;

    @FXML
    private VBox listaVentas;

    @FXML
    private TableView<Evento> listaEventos;

    @FXML
    private TableColumn<Evento, String> fecha;

    @FXML
    private TableColumn<Evento, String> descripcion;

    @FXML
    private AnchorPane root;

    @FXML
    private Label idEscondido;

    @FXML
    private Label matriculaEscondido;

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

    // Lista de los eventos
    private List<Evento> eventos;

    //Cosas de almacen
    private Node nodeA;
    private Almacen almacen;

    private Evento eventoSeleccionado;

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

        this.listaEventos.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                eventoSeleccionado = listaEventos.getSelectionModel().getSelectedItem();
            }
        });
    }

    @FXML
    private void altaParcela(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaParcela.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.setUserData("alta");

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        JFXComboBox tipoParcela = (JFXComboBox)root.lookup("#tipoParcela");
        JFXComboBox tipoCultivo = (JFXComboBox)root.lookup("#tipoCultivo");

        tipoParcela.getItems().addAll(Parcela.TipoParcela.values());
        tipoCultivo.getItems().addAll(Parcela.TipoCultivo.values());

        stage.show();
        stage.setOnHidden(windowEvent -> {
            this.recargar();
        });
    }

    @FXML
    private void modificarParcela(MouseEvent mouseEvent) throws IOException {

        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaParcela.fxml"));
                Parent root = (Parent) loader.load();

                //Get nodo de la parcela clickada
                Node node = (Node)mouseEvent.getSource();
                Label id = (Label)node.lookup("#id");
                int idText = Integer.parseInt(id.getText());
                Parcela parcela = parcelaDAO.buscar(idText);

                Label titulo = (Label) root.lookup("#titulo");
                titulo.setText("Parcela" + id.getText());

                JFXTextField idParcela = (JFXTextField) root.lookup("#idParcela");
                idParcela.setText(id.getText());
                idParcela.setEditable(false);

                JFXTextField latitud = (JFXTextField) root.lookup("#latitud");
                JFXTextField longitud = (JFXTextField) root.lookup("#longitud");
                JFXTextField tam = (JFXTextField) root.lookup("#tam");

                JFXComboBox tipoParcela = (JFXComboBox)root.lookup("#tipoParcela");
                JFXComboBox tipoCultivo = (JFXComboBox)root.lookup("#tipoCultivo");

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
    private void altaVenta(ActionEvent actionEvent) throws IOException {
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
    private void altaEvento(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaEvento.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        JFXButton boton = (JFXButton)actionEvent.getSource();
        Label label = (Label)boton.getScene().lookup("#idEscondido");

        //Si el id esta vacio, es maquinaria
        if(idEscondido == null || idEscondido.getText().isBlank()){
            scene.setUserData(matriculaEscondido.getText());
        }else{
            scene.setUserData(Integer.parseInt(idEscondido.getText()));
        }

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
        eventoDAO.eliminar(eventoSeleccionado, LoginController.getUsuarioActual().getNombreUsuario());
        this.recargar();
    }

    @FXML
    private void altaGasto(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/altaGasto.fxml"));
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.setUserData(Gasto.TipoGasto.Agricultura);

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
    private void mostrarParcelas() {
        this.panel = 0;
        this.panelMaquinaria.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAlmacen.setVisible(false);
        this.panelEventos.setVisible(false);
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
            this.panelEventos.setVisible(false);
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
            this.panelEventos.setVisible(false);
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
            this.panelEventos.setVisible(false);
            this.panelGastos.setVisible(true);
        });

        new Thread(t).start();
    }

    @FXML
    private void mostrarAlmacen() {
        Task<Boolean> t = new Task<>() {
            @Override
            protected Boolean call() {

                if(almacen == null){
                    almacen = almacenDAO.getAlmacen();
                    return true;
                }
                return false;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if(t.getValue())
                this.pintaAlmacen();
            this.panel = 4;
            this.panelMaquinaria.setVisible(false);
            this.panelParcelas.setVisible(false);
            this.panelVentas.setVisible(false);
            this.panelGastos.setVisible(false);
            this.panelEventos.setVisible(false);
            this.panelAlmacen.setVisible(true);
        });

        new Thread(t).start();
    }

    @FXML
    private void mostrarEventosParcela(int id){
        this.panel = 5;
        this.panelMaquinaria.setVisible(false);
        this.panelParcelas.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAlmacen.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelEventos.setVisible(true);

        if(this.eventos == null || this.eventos.isEmpty() || this.idEscondido != null || !this.idEscondido.getText().equals(String.valueOf(id))){
            this.idEscondido.setText(String.valueOf(id));
            this.matriculaEscondido.setText("");
            this.eventos = eventoDAO.listarEventosParcela(id);
            this.pintaEventosParcela(id);
        }

    }

    @FXML
    private void mostrarEventosMaquinaria(String id){
        this.panel = 5;
        this.panelMaquinaria.setVisible(false);
        this.panelParcelas.setVisible(false);
        this.panelVentas.setVisible(false);
        this.panelAlmacen.setVisible(false);
        this.panelGastos.setVisible(false);
        this.panelEventos.setVisible(true);

        if(this.eventos == null || this.eventos.isEmpty() || this.matriculaEscondido != null || !this.matriculaEscondido.getText().equals(id)){
            this.matriculaEscondido.setText(id);
            this.idEscondido.setText("");
            this.eventos = eventoDAO.listarEventosMaquinaria(id);
            this.pintaEventosMaquinaria(id);
        }
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

            case 4 -> {
                this.listaAlmacen.getChildren().clear();
                this.almacen = almacenDAO.getAlmacen();
                this.pintaAlmacen();
            }

            case 5 -> {
                this.listaEventos.setItems(null);
                if(matriculaEscondido.getText().isBlank())
                    this.pintaEventosParcela(Integer.parseInt(idEscondido.getText()));
                else
                    this.pintaEventosMaquinaria(matriculaEscondido.getText());

            }
        }
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
                usuarioDAO.cerrarSesion(usuarioActual.getNombreUsuario());
                return null;
            }
        };

        new Thread(t).start();
        new Thread(cargarVista).start();
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

                //Borrar
                JFXButton borrar = (JFXButton) nodesP[i].lookup("#botonBorrar");
                borrar.setOnAction(e ->{
                    parcelaDAO.eliminar(Integer.parseInt(id.getText()),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                Pane base = (Pane) nodesP[i].lookup("#base");
                base.setOnMouseClicked(e ->{
                    try {
                        this.modificarParcela(e);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });

                //Eventos
                JFXButton gestionarEventos = (JFXButton) nodesP[i].lookup("#botonEvent");
                gestionarEventos.setOnAction(e ->{
                    mostrarEventosParcela(Integer.parseInt(id.getText()));
                });

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

                Label matricula = (Label) nodesM[i].lookup("#id");
                matricula.setText(s[0]);

                Label nombre = (Label) nodesM[i].lookup("#nombre");
                if (s[1] != null)
                    nombre.setText(s[1]);

                Label evento = (Label) nodesM[i].lookup("#evento");
                if (!s[2].equals("null"))
                    evento.setText(s[2]);

                //Borrar
                JFXButton borrar = (JFXButton) nodesM[i].lookup("#botonBorrar");
                borrar.setOnAction(e ->{
                    maquinariaDAO.eliminar(matricula.getText(),
                            LoginController.getUsuarioActual().getNombreUsuario());
                    this.recargar();
                });

                //Eventos
                JFXButton gestionarEventos = (JFXButton) nodesM[i].lookup("#botonEventos");
                gestionarEventos.setOnAction(e ->{
                    mostrarEventosMaquinaria(matricula.getText());
                });

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

    private void pintaAlmacen(){
        this.listaAlmacen.getChildren().clear();
        try {
            nodeA = FXMLLoader.load(this.getClass().getClassLoader().getResource("fxml/almacen.fxml"));

            Pane agriPane = (Pane) nodeA.lookup("#agriPane");
            Pane ganPane = (Pane) nodeA.lookup("#ganPane");

            ganPane.setVisible(false);
            agriPane.setVisible(true);

            //Trigo
            Label trigo = (Label) nodeA.lookup("#trigo");
            trigo.setText(String.valueOf(almacen.getExcedenteTrigo()));

            //Cebada
            Label cebada = (Label) nodeA.lookup("#cebada");
            cebada.setText(String.valueOf(almacen.getExcedenteCebada()));

            //Maiz
            Label maiz = (Label) nodeA.lookup("#maiz");
            maiz.setText(String.valueOf(almacen.getExcedenteMaiz()));

            //Remolacha
            Label remolacha = (Label) nodeA.lookup("#remolacha");
            remolacha.setText(String.valueOf(almacen.getExcedenteRemolacha()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.listaAlmacen.getChildren().add(nodeA);
    }

    private void pintaEventosParcela(int id){

        this.listaEventos.setItems(null);
        this.listaEventos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.fecha.setCellValueFactory(f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getFecha())));
        this.descripcion.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getDescripcion()));

        FilteredList<Evento> filteredData = new FilteredList<>(FXCollections.observableArrayList(eventoDAO.listarEventosParcela(id)), p -> true);
        SortedList<Evento> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(this.listaEventos.comparatorProperty());
        this.listaEventos.setItems(sortedData);

        buscarEvento.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(eventos -> {
                if(text == null || text.isEmpty()) return true;

                String fecha = String.valueOf(eventos.getFecha());
                String descripcion = eventos.getDescripcion();
                return  fecha.contains(text) || descripcion.contains(text);
            });
        });

        this.matriculaEscondido.setText("");
        this.idEscondido.setText(String.valueOf(id));

    }

    private void pintaEventosMaquinaria(String id){

        this.listaEventos.setItems(null);
        this.listaEventos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.fecha.setCellValueFactory(f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getFecha())));
        this.descripcion.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getDescripcion()));

        FilteredList<Evento> filteredData = new FilteredList<>(FXCollections.observableArrayList(eventoDAO.listarEventosMaquinaria(id)), p -> true);
        SortedList<Evento> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(this.listaEventos.comparatorProperty());
        this.listaEventos.setItems(sortedData);

        buscarEvento.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(eventos -> {
                if(text == null || text.isEmpty()) return true;

                String fecha = String.valueOf(eventos.getFecha());
                String descripcion = eventos.getDescripcion();
                return  fecha.contains(text) || descripcion.contains(text);
            });
        });

        this.idEscondido.setText("");
        this.matriculaEscondido.setText(id);

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
