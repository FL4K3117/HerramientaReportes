package com.savadr.HerramientaFX8.Controlador;

import com.savadr.HerramientaFX8.Modelo.AppQuery;
import com.savadr.HerramientaFX8.Modelo.DBConector;
import com.savadr.HerramientaFX8.Modelo.Existencias;
import com.savadr.HerramientaFX8.Modelo.GeneradorReportes;

import javafx.collections.FXCollections;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Yair-PC
 */
public class ExistenciasController implements Initializable {

    private Stage stage;
    private DBConector db;
    private AppQuery appQuery;
    private MenuPrincipalController controllerE;
    private GeneradorReportes export;
    private final Map<String, Integer> almMapSav = new HashMap<>();

    private final Map<String, Integer> almMapAdr = new HashMap<>();

    @FXML
    private TableView TabExist;
    @FXML
    private TableColumn colGrup;
    @FXML
    private TableColumn colArt;
    @FXML
    private TableColumn colDesc;
    @FXML
    private TableColumn colExis;
    @FXML
    private TableColumn colUltCos;
    @FXML
    private TableColumn colCosProm;
    @FXML
    private TableColumn colAlm;
    @FXML
    private CheckBox cbxArt;
    @FXML
    private CheckBox cbxDesc;
    @FXML
    private CheckBox cbxGru;
    @FXML
    private TextField txtArt;
    @FXML
    private TextField txtDesc;
    @FXML
    private TextField txtGru;
    @FXML
    private CheckBox cbxBusGr;
    @FXML
    private CheckBox cbxExi;
    @FXML
    private CheckBox cbxSav;
    @FXML
    private CheckBox cbxAdr;
    @FXML
    private ComboBox<String> cbbSav;
    @FXML
    private ComboBox<String> cbbAdr;
    @FXML
    private TextArea txtNota;
    @FXML
    private Button btnConsul;
    @FXML
    private Button btnLimp;
    @FXML
    private Button btnExpo;

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

        setCheckBoxState();
        cbxBusGr.setSelected(true);
        cbxBusGr.setOnAction(ev -> setCheckBoxState());

        cbxDesc.setSelected(false);
        cbxDesc.setOnAction(ev -> {
            if (!cbxDesc.isSelected()) {
                txtDesc.clear();
            }
            setCheckBoxState();
        });
        cbxGru.setSelected(false);
        cbxGru.setOnAction(ev -> {
            if (!cbxGru.isSelected()) {
                txtGru.clear();
            }
            setCheckBoxState();
        });
        cbxSav.setSelected(false);
        cbxSav.setOnAction(ev -> {
            if (!cbxSav.isSelected()) {
                cbbSav.getSelectionModel().clearSelection();
            }
            setCheckBoxState();
        });
        cbxAdr.setSelected(false);
        cbxAdr.setOnAction(ev -> {
            if (!cbxAdr.isSelected()) {
                cbbAdr.getSelectionModel().clearSelection();
            }
            setCheckBoxState();
        });

        ObservableList<String> listSav = FXCollections.observableArrayList(almMapSav.keySet());
        cbbSav.setItems(listSav);
        ObservableList<String> listAdr = FXCollections.observableArrayList(almMapAdr.keySet());
        cbbAdr.setItems(listAdr);

        db = DBConector.getInstance();
        appQuery = new AppQuery(db.getCon());
        colGrup.setCellValueFactory(new PropertyValueFactory<>("artClas"));
        colGrup.setCellFactory(forTableColumn(Pos.CENTER));
        colArt.setCellValueFactory(new PropertyValueFactory<>("artCod"));
        colArt.setCellFactory(forTableColumn(Pos.CENTER_LEFT));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("artDesc"));
        colDesc.setCellFactory(forTableColumn(Pos.CENTER_LEFT));
        colExis.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        colExis.setCellFactory(forTableColumn(Pos.CENTER));
        colUltCos.setCellValueFactory(new PropertyValueFactory<>("ultimoCosto"));
        colUltCos.setCellFactory(forTableColumn(Pos.CENTER_RIGHT));
        colCosProm.setCellValueFactory(new PropertyValueFactory<>("costoPromedio"));
        colCosProm.setCellFactory(forTableColumn(Pos.CENTER_RIGHT));
        colAlm.setCellValueFactory(new PropertyValueFactory<>("almacen"));
        colAlm.setCellFactory(forTableColumn(Pos.CENTER));
    }
    @FXML
    private void mostrarConsulta(ActionEvent event) {
        String filterArt = txtArt.getText();
        String filterDesc = txtDesc.getText();
        String filterGru = txtGru.getText();
        boolean cbxExiSelected = cbxExi.isSelected();
        boolean cbxBusGrSelected = cbxBusGr.isSelected();
        boolean cbxArtSelected = cbxArt.isSelected();
        boolean cbxSavSelected = cbxSav.isSelected();
        boolean cbxAdrSelected = cbxAdr.isSelected();
        if (!cbxBusGr.isSelected() && !cbxSav.isSelected() && !cbxAdr.isSelected()) {
            showAlert("Advertencia","Por favor, seleccione una sucursal.");
        } else if ((cbxSav.isSelected() && cbbSav.getSelectionModel().isEmpty()) || (cbxAdr.isSelected() && cbbAdr.getSelectionModel().isEmpty())) {
            showAlert("Advertencia","Por favor, seleccione una sucursal.");
        } else {
            if (cbxBusGr.isSelected()) {
                ObservableList<Existencias> resultadosGral = appQuery.getStockGral(filterArt, filterDesc, filterGru, cbxExiSelected, cbxBusGrSelected, cbxArtSelected);
                TabExist.setItems(resultadosGral);
            } else if (cbxSav.isSelected()) {
                Integer almSavIdSelected = almMapSav.get(cbbSav.getSelectionModel().getSelectedItem());
                ObservableList<Existencias> resultadosSav = appQuery.getStockSav(filterArt, filterDesc, filterGru, cbxExiSelected, cbxBusGrSelected, cbxArtSelected, cbxSavSelected, almSavIdSelected);
                TabExist.setItems(resultadosSav);
            } else if (cbxAdr.isSelected()) {
                Integer almAdrIdSelected = almMapAdr.get(cbbAdr.getSelectionModel().getSelectedItem());
                ObservableList<Existencias> resultadosAdr = appQuery.getStockAdr(filterArt, filterDesc, filterGru, cbxExiSelected, cbxBusGrSelected, cbxSavSelected, cbxAdrSelected, cbxAdrSelected, almAdrIdSelected);
                TabExist.setItems(resultadosAdr);
            }
        }
    }
    @FXML
    private void showExportFormatDialog(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Formato de Exportación");
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
    private void exportToPdf(ActionEvent e) {
        if (TabExist.getItems().isEmpty()) {
            showAlert("Advertencia", "No hay datos por exportar");
        } else {
            export = new GeneradorReportes();
            export.exportToPdf(TabExist, getStage(), "Existencias");
        }
    }
    @FXML
    private void exportToExcel(ActionEvent e) {
        if (TabExist.getItems().isEmpty()) {
            showAlert("Advertencia", "No hay datos por exportar");
        } else {
            export = new GeneradorReportes();
            export.exportToExcel(TabExist, getStage(), "Existencias", "Reporte Existencias");
        }
    }
    @FXML
    private void clearField(ActionEvent event) {
        txtArt.clear();
        txtDesc.clear();
        txtGru.clear();
        cbbSav.getSelectionModel().clearSelection();
        cbbAdr.getSelectionModel().clearSelection();
        TabExist.getItems().clear();
    }
    void init(Stage stageEx, MenuPrincipalController aThis) {
        this.controllerE = aThis;
        this.stage = stageEx;
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
    private void setCheckBoxState() {
        if (cbxBusGr.isSelected()) {
            cbxDesc.setSelected(false);
            cbxGru.setSelected(false);
            cbxSav.setSelected(false);
            cbxAdr.setSelected(false);
            txtDesc.clear();
            txtGru.clear();
            cbbSav.getSelectionModel().clearSelection();
            cbbAdr.getSelectionModel().clearSelection();
            cbxDesc.setDisable(true);
            cbxGru.setDisable(true);
            cbxSav.setDisable(true);
            cbxAdr.setDisable(true);
        } else {
            cbxDesc.setDisable(false);
            cbxGru.setDisable(false);
            cbxSav.setDisable(false);
            cbxAdr.setDisable(false);
        }

        if (cbxGru.isSelected()) {
            txtGru.setDisable(false);
        } else {
            txtGru.setDisable(true);
        }

        if (cbxDesc.isSelected()) {
            txtDesc.setDisable(false);
        } else {
            txtDesc.setDisable(true);
        }

        if (cbxSav.isSelected()) {
            cbbSav.setDisable(false);
        } else {
            cbbSav.setDisable(true);
        }

        if (cbxAdr.isSelected()) {
            cbbAdr.setDisable(false);
        } else {
            cbbAdr.setDisable(true);
        }
    }
    void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //Method to align text in tableview
    public static class CellPosition<T> extends TableCell<T, Object> {
        private final Pos alignment;
        public CellPosition(Pos alignment) {
            this.alignment = alignment;
        }
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(item.toString());
            }
            setAlignment(alignment);
        }
    }
    public static <T> Callback<TableColumn<T, Object>, TableCell<T, Object>> forTableColumn(Pos alignment) {
        return param -> new CellPosition<>(alignment);
    }// Ends method to align text in tableview
}