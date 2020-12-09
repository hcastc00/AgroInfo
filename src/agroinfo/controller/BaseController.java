package agroinfo.controller;

        import com.jfoenix.controls.JFXButton;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.Node;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.Pane;
        import javafx.stage.Stage;

public class BaseController {

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

}
