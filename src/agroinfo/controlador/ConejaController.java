package agroinfo.controlador;

import agroinfo.modelo.dao.ConejaDAO;
import agroinfo.modelo.vo.Coneja;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConejaController {

    @FXML
    private JFXTextField idAlta;

    ConejaDAO conejaDAO = new ConejaDAO();

    @FXML
    private void close(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    private void guardar(ActionEvent event) {
       conejaDAO.crear(new Coneja((Integer.parseInt(idAlta.getText()))));
       this.close(event);
    }
}
