package agroinfo.controlador;

        import agroinfo.modelo.dao.UsuarioDAO;
        import agroinfo.modelo.vo.Usuario;
        import animatefx.animation.*;
        import com.jfoenix.controls.JFXButton;
        import com.jfoenix.controls.JFXPasswordField;
        import com.jfoenix.controls.JFXTextField;
        import javafx.animation.FadeTransition;
        import javafx.animation.ScaleTransition;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Node;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Label;
        import javafx.scene.paint.Color;
        import javafx.stage.Stage;
        import javafx.util.Duration;

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
    void entrar(ActionEvent event) throws IOException {
        if(!user.getText().isBlank() && !pass.getText().isBlank()) {
            usuarioActual = usuarioDAO.iniciarSesion(user.getText(), pass.getText());
        }


        if (usuarioActual!=null){
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            Parent ventana = null;
            if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Administrador)){
               ventana = FXMLLoader.load(getClass().getResource("../vista/admin.fxml"));
            }

            if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Ganadero)){
                ventana = FXMLLoader.load(getClass().getResource("../vista/ganadero.fxml"));
            }

            if (usuarioActual.getTipo().equals(Usuario.TipoUsuario.Agricultor)){
                ventana = FXMLLoader.load(getClass().getResource("../vista/agricultor.fxml"));
            }
            thisStage.setScene(new Scene(ventana, 1200, 750));
        }else{
            error.setVisible(true);
            new Shake(botonEntrar).play();
            new FadeIn(error).play();
        }

        Parent agricultor = FXMLLoader.load(getClass().getResource("../vista/agricultor.fxml"));

    }

    @FXML
    void mostrarAyuda(ActionEvent event){

    }



}
