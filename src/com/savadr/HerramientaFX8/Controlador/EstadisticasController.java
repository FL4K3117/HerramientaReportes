package com.savadr.HerramientaFX8.Controlador;

import com.savadr.HerramientaFX8.Modelo.DBConector;
import com.savadr.HerramientaFX8.Modelo.Estadisticas;
import com.savadr.HerramientaFX8.Modelo.EstadisticasQuery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EstadisticasController implements Initializable {

    private final Map<String, Integer> sucSav = new HashMap<>();
    private final Map<String, Integer> sucAdr = new HashMap<>();
    private final Map<String, Integer> orderType = new HashMap<>();
    @FXML
    private TableView tabEstadisticaArt;
    @FXML
    private TableColumn tcTipo;
    @FXML
    private TableColumn nMes1;
    @FXML
    private TableColumn nMes2;
    @FXML
    private TableColumn nMes3;
    @FXML
    private TableColumn nMes4;
    @FXML
    private TableColumn nMes5;
    @FXML
    private TableColumn nMes6;
    @FXML
    private TableView tabDetallePed;
    @FXML
    private TableColumn tcGrupo;
    @FXML
    private TableColumn tcArticulo;
    @FXML
    private TableColumn tcDescripcion;
    @FXML
    private TableColumn tcCantidad;
    @FXML
    private TableColumn tcMax;
    @FXML
    private TableColumn tcMin;
    @FXML
    private CheckBox cbxArticulo;
    @FXML
    private CheckBox cbxGrupo;
    @FXML
    private CheckBox cbxCantidad;
    @FXML
    private CheckBox cbxFol;
    @FXML
    private ComboBox cbbType;
    @FXML
    private ComboBox cbbSucursales;
    @FXML
    private TextField txtArticulo;
    @FXML
    private TextField txtGrupo;
    @FXML
    private TextField txtFolioPed;
    @FXML
    private Button btnConsultaPed;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnSalir;
    private Stage stage;
    private DBConector db;
    private EstadisticasQuery statisticsQuery;
    private MenuPrincipalController mpController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sucSav.put("Cordoba", 1);
        sucSav.put("Coatzacoalcos", 2);
        sucSav.put("Merida", 3);
        sucSav.put("Matriz", 4);
        sucSav.put("Tejeria", 5);
        sucSav.put("Tuxtla", 6);
        sucSav.put("Villahermosa", 7);

        sucAdr.put("Matriz", 1);
        sucAdr.put("Cuauhtemoc", 2);
        sucAdr.put("Xalapa", 3);

        orderType.put("Cotizacion", 1);
        orderType.put("Orden de Compra", 2);
        orderType.put("Pedidos de traspaso", 3);

        cbxArticulo.setSelected(false);
        cbxArticulo.setOnAction(ev -> setCheckBoxState());

        cbxFol.setSelected(false);
        cbxFol.setOnAction(ev -> setCheckBoxState());

        btnSalir.setOnMouseClicked(event -> {
            Stage actualStage = (Stage) btnSalir.getScene().getWindow();
            actualStage.close();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/savadr/HerramientaFX8/Vista/MenuPrincipal.fxml"));
                Parent mpRoot = loader.load();
                Scene scene = new Scene(mpRoot);
                Stage stage = new Stage();
                stage.setScene(scene);
                com.savadr.HerramientaFX8.Controlador.MenuPrincipalController mpController = loader.getController();
                mpController.init(stage, null);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        db = DBConector.getInstance();
        statisticsQuery = new EstadisticasQuery(db.getCon());

        if(db != null && db.isConnectedSurtidora()){
            ObservableList<String> listSav = FXCollections.observableArrayList(sucSav.keySet());
            cbbSucursales.setItems(listSav);
        }else if(db != null && db.isConnectedAdrimar()){
            ObservableList<String> listAdr = FXCollections.observableArrayList(sucAdr.keySet());
            cbbSucursales.setItems(listAdr);
        }
        ObservableList<String> typeList = FXCollections.observableArrayList(orderType.keySet());
        cbbType.setItems(typeList);
    }
    void init(Stage stageSt, MenuPrincipalController aThis) {
        this.mpController = aThis;
        this.stage = stageSt;
    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setDBConector(DBConector db) {
        this.db = db;
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void showQuerys(ActionEvent e){
        String filterFol = txtFolioPed.getText();
        String filterArt = txtArticulo.getText();
        String filterGru = txtGrupo.getText();
        boolean cbxArtSelected = cbxArticulo.isSelected();
        boolean cbxGruSelected = cbxGrupo.isSelected();
        boolean cbxCantCeroSelected = cbxCantidad.isSelected();

        if(!cbxArticulo.isSelected() && !cbxFol.isSelected()){
            showAlert("Advertencia","Seleccione un metodo de busqueda");
        }else if(cbxArticulo.isSelected() && cbbSucursales.getSelectionModel().isEmpty()){
            showAlert("Advertencia","Seleccione una sucursal.");
        }else if(cbxFol.isSelected() && cbbType.getSelectionModel().isEmpty()){
            showAlert("Advertencia","Seleccione un de tipo folio.");
        }else{
            if(db.isConnectedSurtidora()){
                if(cbxFol.isSelected()){
                    ObservableList<Estadisticas> result = statisticsQuery.getArticleQuery();
                    tabDetallePed.setItems(result);
                }else if(cbxArticulo.isSelected()){

                }
            }else if(db.isConnectedAdrimar()){
                ObservableList<Estadisticas> result = statisticsQuery.getArticleQuery();
                tabDetallePed.setItems(result);
            }
        }


    }
    private void setCheckBoxState(){
        if(cbxArticulo.isSelected()){
            cbxFol.setSelected(false);
            cbbType.getSelectionModel().clearSelection();
            txtFolioPed.clear();
            cbbType.setDisable(true);
            txtFolioPed.setDisable(true);
            tabDetallePed.setDisable(true);
        }else{
            cbbType.setDisable(false);
            txtFolioPed.setDisable(false);
            tabDetallePed.setDisable(false);
        }

        if(cbxFol.isSelected()){
            cbxArticulo.setSelected(false);
            cbxGrupo.setSelected(false);
            cbxCantidad.setSelected(false);
            txtArticulo.clear();
            txtGrupo.clear();
            cbbSucursales.getSelectionModel().clearSelection();
            cbxGrupo.setDisable(true);
            cbxCantidad.setDisable(true);
            txtArticulo.setDisable(true);
            txtGrupo.setDisable(true);
            cbbSucursales.setDisable(true);
        }else{
            cbxGrupo.setDisable(false);
            cbxCantidad.setDisable(false);
            txtArticulo.setDisable(false);
            txtGrupo.setDisable(false);
            cbbSucursales.setDisable(false);
        }
    }
}
