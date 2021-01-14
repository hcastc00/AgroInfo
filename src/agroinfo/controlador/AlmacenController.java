package agroinfo.controlador;

import agroinfo.modelo.dao.AlmacenDAO;
import agroinfo.modelo.vo.Almacen;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AlmacenController implements Initializable {


    AlmacenDAO almacenDAO = new AlmacenDAO();

    @FXML
    private Pane agriPane;

    @FXML
    private Label trigo;

    @FXML
    private Label cebada;

    @FXML
    private Label maiz;

    @FXML
    private Label remolacha;

    @FXML
    private Pane ganPane;

    @FXML
    private Label conejos;

    @FXML
    private JFXTextField conejosModificacion;

    @FXML
    private Label piensoLactancia;

    @FXML
    private JFXTextField piensoLactanciaModificacion;

    @FXML
    private Label piensoMedicado;

    @FXML
    private JFXTextField piensoMedicadoModificacion;

    @FXML
    private Label piensoRemate;

    @FXML
    private JFXTextField piensoRemateModificado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        soloEntradaNumerica(conejosModificacion);
        soloEntradaNumerica(piensoLactanciaModificacion);
        soloEntradaNumerica(piensoMedicadoModificacion);
        soloEntradaNumerica(piensoRemateModificado);
    }

    private void soloEntradaNumerica(JFXTextField entrada){
        entrada.getProperties().put("vkType", "numeric");
        entrada.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() == 0) {
                    return c;
                }
                try {
                    Integer.parseInt(c.getControlNewText());
                    return c;
                } catch (NumberFormatException e) {
                }
                return null;
            }
            return c;
        }));
    }


    @FXML
    private void sumarConejos(ActionEvent actionEvent){

        boolean conejosError =  conejosModificacion.getText().isBlank() || !conejosModificacion.getText().matches("^[0-9]*$");
        Almacen almacen = almacenDAO.getAlmacen();
        if(!conejosError) {
            int conejosTotales = almacen.getConejos() + Integer.parseInt(conejosModificacion.getText());
            conejos.setText(String.valueOf(conejosTotales));
            almacen.setConejos(Integer.parseInt(conejos.getText()));
            almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());
        }
    }

    @FXML
    private void restarConejos(ActionEvent actionEvent){

        boolean conejosError = conejosModificacion.getText().isBlank() || !conejosModificacion.getText().matches("^[0-9]*$");
        Almacen almacen = almacenDAO.getAlmacen();

        if(!conejosError) {
            int conejosTotales = almacen.getConejos() - Integer.parseInt(conejosModificacion.getText());
            if(conejosTotales >=0) {
                conejos.setText(String.valueOf(conejosTotales));
                almacen.setConejos(Integer.parseInt(conejos.getText()));
                almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());

    }else if(conejosTotales < 0){
        conejos.setText("0");
        almacen.setConejos(Integer.parseInt(conejos.getText()));
        almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());
    }
        }
    }

    @FXML
    private void sumarPiensoLactancia(ActionEvent actionEvent){

        boolean piensoLactanciaError =  piensoLactanciaModificacion.getText().isBlank() || !piensoLactanciaModificacion.getText().matches("^[0-9]*$");
        Almacen almacen = almacenDAO.getAlmacen();

        if(!piensoLactanciaError) {
            double piensoLactanciaTotal = almacen.getPiensoLactancia() + Integer.parseInt(piensoLactanciaModificacion.getText());
            piensoLactancia.setText(String.valueOf(piensoLactanciaTotal));
            almacen.setPiensoLactancia(Double.parseDouble(piensoLactancia.getText()));
            almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());
        }
    }

    @FXML
    private void restarPiensoLactancia(ActionEvent actionEvent) {

        boolean piensoLactanciaError = piensoLactanciaModificacion.getText().isBlank() || !piensoLactanciaModificacion.getText().matches("^[0-9]*$");
        Almacen almacen = almacenDAO.getAlmacen();

        if (!piensoLactanciaError) {
            Double piensoLactanciaTotal = almacen.getPiensoLactancia() - Integer.parseInt(piensoLactanciaModificacion.getText());
            if (piensoLactanciaTotal >= 0) {
                piensoLactancia.setText(String.valueOf(piensoLactanciaTotal));
                almacen.setPiensoLactancia(Double.parseDouble(piensoLactancia.getText()));
                almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());

    } else if (piensoLactanciaTotal < 0) {
        piensoLactancia.setText("0");
        almacen.setPiensoLactancia(Double.parseDouble(piensoLactancia.getText()));
        almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());
    }
        }
    }

    @FXML
    private void sumarPiensoMedicado(ActionEvent actionEvent){

        boolean piensoMedicadoError =  piensoMedicadoModificacion.getText().isBlank() || !piensoMedicadoModificacion.getText().matches("^[0-9]*$");
        Almacen almacen = almacenDAO.getAlmacen();

        if(!piensoMedicadoError) {
            double piensoMedicadoTotal = almacen.getPiensoMedicado() + Integer.parseInt(piensoMedicadoModificacion.getText());
            piensoMedicado.setText(String.valueOf(piensoMedicadoTotal));
            almacen.setPiensoMedicado(Double.parseDouble(piensoMedicado.getText()));
            almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());
        }
    }

    @FXML
    private void restarPiensoMedicado(ActionEvent actionEvent){
        boolean piensoMedicadoError = piensoMedicadoModificacion.getText().isBlank() || !piensoMedicadoModificacion.getText().matches("^[0-9]*$");
        Almacen almacen = almacenDAO.getAlmacen();

        if (!piensoMedicadoError) {
            Double piensoMedicadoTotal = almacen.getPiensoMedicado() - Integer.parseInt(piensoLactanciaModificacion.getText());
            if(piensoMedicadoTotal >= 0) {
                piensoMedicado.setText(String.valueOf(piensoMedicadoTotal));
                almacen.setPiensoMedicado(Double.parseDouble(piensoMedicado.getText()));
                almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());

        } else if (piensoMedicadoTotal < 0) {
            piensoLactancia.setText("0");
            almacen.setPiensoMedicado(Double.parseDouble(piensoLactancia.getText()));
            almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());
        }
        }
    }

    @FXML
    private void sumarPiensoRemate(ActionEvent actionEvent){
        boolean piensoRemateError =  piensoRemateModificado.getText().isBlank() || !piensoRemateModificado.getText().matches("^[0-9]*$");
        Almacen almacen = almacenDAO.getAlmacen();

        if(!piensoRemateError) {
            double piensoRemateTotal = almacen.getPiensoRemate() + Integer.parseInt(piensoRemateModificado.getText());
            piensoRemate.setText(String.valueOf(piensoRemateTotal));
            almacen.setPiensoRemate(Double.parseDouble(piensoRemate.getText()));
            almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());
        }
    }

    @FXML
    private void restarPiensoRemate(ActionEvent actionEvent){
        boolean piensoRemateError = piensoRemateModificado.getText().isBlank() || !piensoRemateModificado.getText().matches("^[0-9]*$");
        Almacen almacen = almacenDAO.getAlmacen();

        if (!piensoRemateError) {
            Double piensoRemateTotal = almacen.getPiensoRemate() - Integer.parseInt(piensoRemateModificado.getText());
            if(piensoRemateTotal>= 0) {
                piensoRemate.setText(String.valueOf(piensoRemateTotal));
                almacen.setPiensoRemate(Double.parseDouble(piensoRemate.getText()));
                almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());

        } else if (piensoRemateTotal < 0) {
            piensoRemate.setText("0");
            almacen.setPiensoRemate(Double.parseDouble(piensoRemate.getText()));
            almacenDAO.modificar(almacen, LoginController.getUsuarioActual().getNombreUsuario());
        }
        }
    }


}
