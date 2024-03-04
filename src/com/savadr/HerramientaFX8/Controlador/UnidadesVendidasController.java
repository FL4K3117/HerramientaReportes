package com.savadr.HerramientaFX8.Controlador;

import com.savadr.HerramientaFX8.Modelo.AppQuery;
import com.savadr.HerramientaFX8.Modelo.DBConector;
import com.savadr.HerramientaFX8.Modelo.GeneradorReportes;
import com.savadr.HerramientaFX8.Modelo.UnidadesVendidas;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Yair-PC
 */
public class UnidadesVendidasController implements Initializable {

    private Stage stage;
    private DBConector db;
    private AppQuery appQuery;
    private MenuPrincipalController mpController;
    private GeneradorReportes export;
    private final Map<String, Integer> almMapSav = new HashMap<>();
    private final Map<String, Integer> almMapAdr = new HashMap<>();
    @FXML
    private TableView tabUniVen;
    @FXML
    private TableColumn tcGru;
    @FXML
    private TableColumn tcArt;
    @FXML
    private TableColumn tcDesc;
    @FXML
    private TableColumn tcUbi;
    @FXML
    private TableColumn tcExis;
    @FXML
    private TableColumn tcSal;
    @FXML
    private TableColumn tcCont;
    @FXML
    private Button btnConsultar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnImprimir;
    @FXML
    private Button btnExportar;
    @FXML
    private ComboBox<String> cbbSav;
    @FXML
    private ComboBox<String> cbbAdr;
    @FXML
    private DatePicker dpFecIni;
    @FXML
    private DatePicker dpFecFin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        almMapSav.put("Cordoba", 1);
        almMapSav.put("Coatzacoalcos", 2);
        almMapSav.put("Merida", 3);
        almMapSav.put("Matriz", 4);
        almMapSav.put("Tejeria", 5);
        almMapSav.put("Tuxtla", 6);
        almMapSav.put("Villahermosa", 7);

        almMapAdr.put("Matriz", 1);
        almMapAdr.put("Cuauhtemoc", 2);
        almMapAdr.put("Xalapa", 3);

        db = DBConector.getInstance();
        appQuery = new AppQuery(db.getCon());
        tcGru.setCellValueFactory(new PropertyValueFactory<>("artGru"));
        tcGru.setCellFactory(forTableColumn(Pos.CENTER));
        tcArt.setCellValueFactory(new PropertyValueFactory<>("artCod"));
        tcArt.setCellFactory(forTableColumn(Pos.CENTER));
        tcDesc.setCellValueFactory(new PropertyValueFactory<>("artDesc"));
        tcDesc.setCellFactory(forTableColumn(Pos.CENTER));
        tcUbi.setCellValueFactory(new PropertyValueFactory("artUbi"));
        tcUbi.setCellFactory(forTableColumn(Pos.CENTER));
        tcExis.setCellValueFactory(new PropertyValueFactory("stock"));
        tcExis.setCellFactory(forTableColumn(Pos.CENTER));
        tcSal.setCellValueFactory(new PropertyValueFactory("soldOut"));
        tcSal.setCellFactory(forTableColumn(Pos.CENTER));
        //New Column for "Conteo"
        tcCont.setCellValueFactory(cellData -> new SimpleStringProperty("_____________"));
        tcCont.setCellFactory(TextFieldTableCell.forTableColumn());
        tcCont.setCellValueFactory(new PropertyValueFactory<>("artCont"));
        tcCont.setCellFactory(forTableColumn(Pos.CENTER));
        //conteo
        if (db != null && db.isConnectedSurtidora()) {
            ObservableList<String> listSav = FXCollections.observableArrayList(almMapSav.keySet());
            cbbSav.setItems(listSav);
            cbbAdr.setDisable(true);
        } else if (db != null && db.isConnectedAdrimar()) {
            ObservableList<String> listAdr = FXCollections.observableArrayList(almMapAdr.keySet());
            cbbAdr.setItems(listAdr);
            cbbSav.setDisable(true);
        }
    }
    @FXML
    private void showQuerySold(ActionEvent event) {
        LocalDate dateInitial = dpFecIni.getValue();
        LocalDate dateFinal = dpFecFin.getValue();
        if (dateInitial == null || dateFinal == null) {
            showAlert("Advertencia","Ingrese una fecha inicial y una final");
        } else if (cbbSav.getSelectionModel().isEmpty() && cbbAdr.getSelectionModel().isEmpty()) {
            showAlert("Advertencia","Seleccione una sucursal");
        } else {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateInitial = dateInitial.format(format);
            String formattedDateFinal = dateFinal.format(format);
            java.sql.Date sqlDateInitial = java.sql.Date.valueOf(formattedDateInitial);
            java.sql.Date sqlDateFinal = java.sql.Date.valueOf(formattedDateFinal);
            if (db.isConnectedSurtidora()) {
                Integer almSavIdSelected = almMapSav.get(cbbSav.getSelectionModel().getSelectedItem());
                ObservableList<UnidadesVendidas> result = appQuery.getUnitSoldSav(sqlDateInitial, sqlDateFinal, almSavIdSelected);
                tabUniVen.setItems(result);
            } else if (db.isConnectedAdrimar()) {
                Integer almAdrIdSelected = almMapAdr.get(cbbAdr.getSelectionModel().getSelectedItem());
                ObservableList<UnidadesVendidas> result = appQuery.getUnitSoldAdr(sqlDateInitial, sqlDateFinal, almAdrIdSelected);
                tabUniVen.setItems(result);
            }
        }
    }
    @FXML
    private void clearField(ActionEvent event) {
        dpFecIni.setValue(null);
        dpFecFin.setValue(null);
        cbbSav.getSelectionModel().clearSelection();
        cbbAdr.getSelectionModel().clearSelection();
        tabUniVen.getItems().clear();
    }
    @FXML
    private void showExportFormatDialog(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Formato de Exportacion");
        alert.setHeaderText(null);
        alert.setContentText("Seleccione el formato de exportación:");
        ButtonType pdfButton = new ButtonType("PDF");
        ButtonType excelButton = new ButtonType("Excel");
        alert.getButtonTypes().setAll(pdfButton, excelButton);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == pdfButton) {
                exportToPdf(e);
            } else if (buttonType == excelButton) {
                exportToExcel(e);
            }
        });
    }
    private void exportToExcel(ActionEvent e) {
        if (tabUniVen.getItems().isEmpty()) {
            showAlert("Advertencia", "No hay datos por exportar");
        } else {
            export = new GeneradorReportes();
            export.exportToExcel(tabUniVen, getStage(), "Unidades Vendidas Por Articulo", "Reporte Salidas");
        }
    }
    private void exportToPdf(ActionEvent e) {
        if (tabUniVen.getItems().isEmpty()) {
            showAlert("Advertencia", "No hay datos por exportar");
        } else {
            export = new GeneradorReportes();
            export.exportToPdf(tabUniVen, getStage(), "Unidades Vendidas Por Articulo");
        }
    }
    void init(Stage stage, MenuPrincipalController aThis) {
        this.stage = stage;
        this.mpController = aThis;
    }
    void setDBConector(DBConector db) {
        this.db = db;
    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //Starts method for allignment on tableView
    public static class CellPosition<T> extends TableCell<T, Object>{
        private final Pos alignment;
        public CellPosition(Pos alignment){
            this.alignment =  alignment;
        }
        @Override
        protected void updateItem(Object item, boolean empty){
            super.updateItem(item, empty);
            if(item == null || empty){
                setText(null);
            }else{
                setText(item.toString());
            }
            setAlignment(alignment);
        }
    }
    public static <T>Callback<TableColumn<T, Object>, TableCell<T, Object>> forTableColumn(Pos alignment){
        return param-> new CellPosition<>(alignment);
    }//End method for allignment
}
