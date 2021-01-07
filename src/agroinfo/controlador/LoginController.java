package agroinfo.controlador;

import agroinfo.modelo.dao.UsuarioDAO;
import agroinfo.modelo.vo.Usuario;
import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    public Usuario usuarioActual;

    @FXML
    private JFXTextField user;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private Label error;

    @FXML
    private JFXButton botonEntrar;


    @FXML
    void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    void entrar(ActionEvent event) throws IOException, InterruptedException {

        Task<Boolean> t = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                boolean res = false;
                usuarioActual = usuarioDAO.iniciarSesion(user.getText(),pass.getText());
                return usuarioActual != null;
            }
        };

        t.setOnSucceeded(workerStateEvent -> {
            if(t.getValue()){
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();
                Parent ventana = null;

                if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Administrador)){
                    try {
                        ventana = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/admin.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Ganadero)){
                    try {
                        ventana = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ganadero.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Agricultor)){
                    try {
                        ventana = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/agricultor.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                thisStage.setScene(new Scene(ventana, 1200, 750));
            }else{
                error.setVisible(true);
                new Shake(botonEntrar).play();
                new FadeIn(error).play();
            }
        });
        new Thread(t).start();
    }

    @FXML
    void mostrarAyuda(ActionEvent event){

    }



}
