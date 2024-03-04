package com.savadr.HerramientaFX8.Controlador;

import com.savadr.HerramientaFX8.Modelo.DBConector;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Yair-PC
 */
public class MenuPrincipalController implements Initializable {

    private Stage stage;
    private MenuConexionController controllerMC;
    private DBConector db;

    @FXML
    private Button btnStatistics;
    @FXML
    private Button btnBack;
    @FXML
    private Label lblInfo;
    @FXML
    private Label lblInfoClose;
    @FXML
    private AnchorPane slider;
    @FXML
    private Button btnStock;
    @FXML
    private Button btnHistory;
    @FXML
    private Button btnUnitSold;
    @FXML
    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnBack.setOnMouseClicked(event -> {
            Stage actualStage = (Stage) btnBack.getScene().getWindow();
            actualStage.close();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/savadr/HerramientaFX8/Vista/MenuConexion.fxml"));
                Parent mpRoot = loader.load();
                Scene scene = new Scene(mpRoot);
                Stage stage = new Stage();
                stage.setScene(scene);
                com.savadr.HerramientaFX8.Controlador.MenuConexionController mcController = loader.getController();
                mcController.init(stage, null);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        slider.setTranslateX(-115);
        lblInfoClose.setVisible(false);

        lblInfo.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();
            slider.setTranslateX(-115);

            slide.setOnFinished((ActionEvent e) -> {
                lblInfo.setVisible(false);
                lblInfoClose.setVisible(true);
            });
        });

        lblInfoClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(-115);
            slide.play();
            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                lblInfo.setVisible(true);
                lblInfoClose.setVisible(false);
            });
        });
    }

    @FXML
    private void MostrarExistencias(MouseEvent event) {
        try {
            FXMLLoader loaderPane = new FXMLLoader(getClass().getResource("/com/savadr/HerramientaFX8/Vista/Existencias.fxml"));
            Parent existRoot = loaderPane.load();
            com.savadr.HerramientaFX8.Controlador.ExistenciasController eController = loaderPane.getController();
            content.getChildren().clear();
            content.getChildren().add(existRoot);
            eController.init(stage, this);
            eController.setDBConector(DBConector.getInstance());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void MostrarHistPre(MouseEvent event) {
        try {
            FXMLLoader loaderPane = new FXMLLoader(getClass().getResource("/com/savadr/HerramientaFX8/Vista/HistoricoPrecios.fxml"));
            Parent historicoRoot = loaderPane.load();
            com.savadr.HerramientaFX8.Controlador.HistoricoPreciosController hpController = loaderPane.getController();
            content.getChildren().clear();
            content.getChildren().add(historicoRoot);
            hpController.init(stage, this);
            hpController.setDBConector(DBConector.getInstance());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void MostrarUnidVend(MouseEvent event) {
        try {
            FXMLLoader loaderPane = new FXMLLoader(getClass().getResource("/com/savadr/HerramientaFX8/Vista/UnidadesVendidas.fxml"));
            Parent unitSold = loaderPane.load();
            com.savadr.HerramientaFX8.Controlador.UnidadesVendidasController uvController = loaderPane.getController();
            content.getChildren().clear();
            content.getChildren().add(unitSold);
            uvController.init(stage, this);
            uvController.setDBConector(DBConector.getInstance());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void MostrarEstadisticas(MouseEvent event) {
        try {
            FXMLLoader loaderStatistics = new FXMLLoader(getClass().getResource("/com/savadr/HerramientaFX8/Vista/Estadisticas.fxml"));
            Parent statistics = loaderStatistics.load();
            com.savadr.HerramientaFX8.Controlador.EstadisticasController eController = loaderStatistics.getController();
            Scene sceneSt = new Scene(statistics);
            Stage stageSt = new Stage();
            stageSt.setScene(sceneSt);
            eController.init(stageSt, this);
            eController.setDBConector(DBConector.getInstance());

            Stage currentStage = (Stage) btnStatistics.getScene().getWindow();
            currentStage.close();

            stageSt.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    void init(Stage stageMP, MenuConexionController aThis) {
        this.controllerMC = aThis;
        this.stage = stageMP;
    }

}
