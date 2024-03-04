package com.savadr.HerramientaFX8.Controlador;

import com.savadr.HerramientaFX8.Modelo.AppQuery;
import com.savadr.HerramientaFX8.Modelo.DBConector;
import com.savadr.HerramientaFX8.Modelo.GeneradorReportes;
import com.savadr.HerramientaFX8.Modelo.HistoricoPrecios;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Yair-PC
 */
public class HistoricoPreciosController implements Initializable {

    private Stage stage;
    private DBConector db;
    private AppQuery appQuery;
    private MenuPrincipalController mpController;
    private GeneradorReportes export;
    @FXML
    private TableView tabHisPre;
    @FXML
    private TableColumn tcGru;
    @FXML
    private TableColumn tcArt;
    @FXML
    private TableColumn tcDes;
    @FXML
    private TableColumn tcPreAnt;
    @FXML
    private TableColumn tcPreAct;
    @FXML
    private TableColumn tcMar;
    @FXML
    private Button btnConsultar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnImprimir;
    @FXML
    private Button btnExportar;
    @FXML
    private DatePicker dpFecIni;
    @FXML
    private DatePicker dpFecFin;
    @FXML
    private TextField txtGru;
    @FXML
    private CheckBox cbxGru;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setCheckBoxState();
        cbxGru.setSelected(false);
        cbxGru.setOnAction(ev -> {
            if (!cbxGru.isSelected()) {
                txtGru.clear();
            }
            setCheckBoxState();
        });

        db = DBConector.getInstance();
        appQuery = new AppQuery(db.getCon());
        tcGru.setCellValueFactory(new PropertyValueFactory<>("artGru"));
        tcGru.setCellFactory(forTableColumn(Pos.CENTER));
        tcArt.setCellValueFactory(new PropertyValueFactory<>("artCod"));
        tcArt.setCellFactory(forTableColumn(Pos.CENTER));
        tcDes.setCellValueFactory(new PropertyValueFactory<>("artDesc"));
        tcDes.setCellFactory(forTableColumn(Pos.CENTER));
        tcPreAnt.setCellValueFactory(new PropertyValueFactory("precioAnt"));
        tcPreAnt.setCellFactory(forTableColumn(Pos.CENTER_RIGHT));
        tcPreAct.setCellValueFactory(new PropertyValueFactory("precioAct"));
        tcPreAct.setCellFactory(forTableColumn(Pos.CENTER_RIGHT));
        tcMar.setCellValueFactory(new PropertyValueFactory<>("margen"));
        tcMar.setCellFactory(column -> new TableCell<HistoricoPrecios, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f%%", item));
                }
            }
        });
        tcMar.setCellFactory(forTableColumn(Pos.CENTER_RIGHT));
    }
    @FXML
    private void clearField(ActionEvent event) {
        txtGru.clear();
        dpFecIni.setValue(null);
        dpFecFin.setValue(null);
        tabHisPre.getItems().clear();
    }
    @FXML
    private void showQuery(ActionEvent event) {
        String filterGru = txtGru.getText();
        LocalDate dateInitial = dpFecIni.getValue();
        LocalDate dateFinal = dpFecFin.getValue();
        boolean cbxGruSelected = cbxGru.isSelected();
        if (dateInitial == null || dateFinal == null) {
            showAlert("Advertencia", "Ingrese fechas inicial y final");
        } else if (cbxGru.isSelected() && txtGru.getText().isEmpty()) {
            showAlert("Advertencia", "Ingrese el grupo que desea filtrar");
        } else {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateInitial = dateInitial.format(format);
            String formattedDateFinal = dateFinal.format(format);
            java.sql.Date sqlDateInitial = java.sql.Date.valueOf(formattedDateInitial);
            java.sql.Date sqlDateFinal = java.sql.Date.valueOf(formattedDateFinal);
            ObservableList<HistoricoPrecios> result = appQuery.getHistory(filterGru, cbxGruSelected, sqlDateInitial, sqlDateFinal);
            tabHisPre.setItems(result);
        }
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
    @FXML
    private void exportToExcel(ActionEvent e) {
        if (tabHisPre.getItems().isEmpty()) {
            showAlert("Advertencia", "No hay datos por exportar");
        } else {
            export = new GeneradorReportes();
            export.exportToExcel(tabHisPre, getStage(), "Historial De Precios", "Reporte de Precios");
        }
    }
    @FXML
    private void exportToPdf(ActionEvent e) {
        if (tabHisPre.getItems().isEmpty()) {
            showAlert("Advertencia", "No hay datos por exportar");
        } else {
            export = new GeneradorReportes();
            export.exportToPdf(tabHisPre, getStage(), "Historial de Precios");
        }
    }
    void init(Stage stage, MenuPrincipalController aThis) {
        this.stage = stage;
        this.mpController = aThis;
    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    void setDBConector(DBConector db) {
        this.db = db;
    }
    private void setCheckBoxState() {
        if (cbxGru.isSelected()) {
            txtGru.setDisable(false);
        } else {
            txtGru.setDisable(true);
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //Starts allignment method fot TableView
    public static class CellPosition<T> extends TableCell<T, Object>{
        private final Pos alignment;
        public CellPosition(Pos alignment){
            this.alignment = alignment;
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
    public static <T> Callback<TableColumn<T, Object>, TableCell<T, Object>> forTableColumn(Pos alignment){
        return param -> new CellPosition<>(alignment);
    }//Ends method
}
