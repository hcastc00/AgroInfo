package agroinfo.controlador;

import agroinfo.modelo.dao.UsuarioDAO;
import agroinfo.modelo.vo.Usuario;
import agroinfo.vista.Ventana;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static Usuario usuarioActual;

    @FXML
    private Label cargandoLabel;

    @FXML
    private JFXSpinner spinnerProgreso;

    @FXML
    private JFXTextField user;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private Label error;

    @FXML
    private JFXButton botonEntrar;

    @FXML
    private AnchorPane root;

    @FXML
    private void close(ActionEvent event) {

        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    private void entrar(ActionEvent event) throws IOException, InterruptedException {
        Task<Boolean> t = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                boolean res = false;
                usuarioActual = usuarioDAO.iniciarSesion(user.getText(), pass.getText());
                return usuarioActual != null;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if (t.getValue()) {
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();

                Task<Parent> cargarVista = new Task<Parent>() {
                    @Override
                    protected Parent call() throws Exception {

                        Parent ventana = null;

                        if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Administrador)) {
                            try {
                                ventana = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/admin.fxml"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Ganadero)) {
                            try {
                                ventana = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ganadero.fxml"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Agricultor)) {
                            try {
                                ventana = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/agricultor.fxml"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return ventana;
                    }
                };

                cargarVista.setOnSucceeded(workerStateEvent1 -> {

                    Parent ventana = cargarVista.getValue();

                    Label nombre = (Label) ventana.lookup("#nombre");
                    nombre.setText(usuarioActual.getNombreUsuario());
                    Scene scene = new Scene(ventana, 1200, 750);
                    scene.getStylesheets().add(Ventana.color);
                    this.spinnerProgreso.setVisible(false);
                    this.cargandoLabel.setVisible(false);
                    this.botonEntrar.setVisible(true);
                    thisStage.setScene(scene);

                });

                new Thread(cargarVista).start();
            } else {

                this.spinnerProgreso.setVisible(false);
                this.cargandoLabel.setVisible(false);
                this.botonEntrar.setVisible(true);

                this.error.setVisible(true);
                new Shake(botonEntrar).play();
                new FadeIn(error).play();
            }
        });
        new Thread(t).start();

        this.botonEntrar.setVisible(false);
        this.spinnerProgreso.setVisible(true);
        this.cargandoLabel.setVisible(true);
    }

    @FXML
    private void mostrarAyuda(ActionEvent event) {

    }

    @FXML
    private ImageView logo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.setOnMousePressed(pressEvent -> {
            root.setOnMouseDragged(dragEvent -> {
                if (pressEvent.getSceneY() < 75) {
                    root.getScene().getWindow().setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                    root.getScene().getWindow().setY(dragEvent.getScreenY() - pressEvent.getSceneY());
                }
            });
        });
    }

    public static Usuario getUsuarioActual() { return usuarioActual; }

    @FXML
    public void easterEgg(javafx.scene.input.MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 15) {
            String audio = "tractorAmarillo.mp3";
            File f = new File(String.valueOf(this.getClass().getClassLoader().getResource(audio)));
            Media sound = new Media(f.toString());
            new MediaPlayer(sound).play();
        }
    }
}
