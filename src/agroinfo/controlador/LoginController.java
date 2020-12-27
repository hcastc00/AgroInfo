package agroinfo.controlador;

        import agroinfo.modelo.dao.UsuarioDAO;
        import agroinfo.modelo.vo.Usuario;
        import com.jfoenix.controls.JFXButton;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Node;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.layout.Pane;
        import javafx.stage.Stage;
        import agroinfo.vista.*;
        import java.io.IOException;

public class LoginController {
    private UsuarioDAO usuarioDAO;

    @FXML
    private JFXButton exitButton;

    @FXML
    private Pane loginPane;

    @FXML
    void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    void entrar(ActionEvent event) throws IOException {
        //TODO lo del login
        Usuario u =  usuarioDAO.iniciarSesion("pepe","SoyDios");


        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Parent ganadero = FXMLLoader.load(getClass().getResource("../vista/ganadero.fxml"));
        thisStage.setScene(new Scene(ganadero, 1200, 750));
    }

    @FXML
    void mostrarAyuda(ActionEvent event){

    }


}
