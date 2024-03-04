package com.savadr.HerramientaFX8.Controlador;

import com.savadr.HerramientaFX8.Modelo.DBConector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Yair-PC
 */
public class MenuConexionController implements Initializable {

    private DBConector db = DBConector.getInstance();
    private Stage stage;

    @FXML
    private ImageView imgLogo;
    @FXML
    private Button btnConectar;
    @FXML
    private Button btnDesconectar;
    @FXML
    private Label lblNota;
    @FXML
    private ComboBox<String> cbbServer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> listEmpr = FXCollections.observableArrayList("adrimar", "surtidora");
        cbbServer.setItems(listEmpr);
    }

    @FXML
    public void Conectar(ActionEvent ev) {
        String selServer = (String) cbbServer.getValue();
        db = DBConector.getInstance();

        try {
            if (selServer == null || selServer.isEmpty()) {
                lblNota.setText("No ha seleccionado empresa");
                return;
            }

            if (selServer != null) {
                if ("surtidora".equals(selServer)) {
                    db.getDBConn("jdbc:mysql://104.254.244.67:3306/surtidora", "sistemas_ro", "Payara7*");
                    db.setIsConnectedSurtidora(true);
                } else if ("adrimar".equals(selServer)) {
                    db.getDBConn("jdbc:mysql://104.254.244.67:3306/adrimar", "sistemas_ro", "Payara7*");
                    db.setIsConnectedAdrimar(true);
                }

                if (db.getCon() != null) {
                    lblNota.setText("Conexion Exitosa");
                    showMP();
                } else {
                    lblNota.setText("Error De Conexion");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void showMP() throws IOException {
        FXMLLoader loaderMenu = new FXMLLoader(getClass().getResource("/com/savadr/HerramientaFX8/Vista/MenuPrincipal.fxml"));
        Parent root = loaderMenu.load();
        com.savadr.HerramientaFX8.Controlador.MenuPrincipalController mpController = loaderMenu.getController();
        Scene sceneMP = new Scene(root);
        Stage stageMP = new Stage();
        stageMP.setScene(sceneMP);
        mpController.init(stageMP, this);

        Stage currentStage = (Stage) btnConectar.getScene().getWindow();
        currentStage.close();//Metodo para poder cerrar la escena actual mediante una referencia del contenido de escena

        stageMP.show();
    }

    @FXML
    private void Cerrar(ActionEvent ev) {
        if (db.getCon() != null) {
            db.closeConnection();
            lblNota.setText("Conexion Cerrada");
        } else {
            lblNota.setText("No se logro cerrar sesion");
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    void init(Stage stage, Object object) {
        this.stage = stage;
    }

}
