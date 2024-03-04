package com.savadr.HerramientaFX8.Modelo;


import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.ReportStyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

/**
 *
 * @author Yair-PC
 */
public class GeneradorReportes {

    public <T> void exportToPdf(TableView<T> tableView, Stage stage, String headerTitle) {
        try {
            //Parameters for the report
            JasperReportBuilder report = DynamicReports.report();
            report.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
            report.setPageMargin(DynamicReports.margin().setLeft(15).setRight(15).setTop(15).setBottom(15));
            ReportStyleBuilder headerStyle = stl.style().setFont(stl.font("Arial", true, false, 20)).setPadding(10);
            ReportStyleBuilder textHeaderStyle = stl.style().setFont(stl.font("Arial", true, false, 12)).setPadding(10);
            ReportStyleBuilder columnTitleStyle = stl.style().setFont(stl.font("Arial", true, false, 12))
                    .setBorder(stl.pen1Point()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY);
            ReportStyleBuilder columnStyle = stl.style().setFont(stl.font("Arial", false, true, 9))
                    .setBorder(stl.pen1Point()).setPadding(5);
            ReportStyleBuilder footerStyle = stl.style().setFont(stl.font("Arial", true, false, 9));
            //Header Data Config
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            // Report Title **
            ComponentBuilder<?, ?> titleComponent = Components.text(headerTitle)
                    .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                    .setStyle(headerStyle);
            // Logo **
            ComponentBuilder<?, ?> logoComponent = Components.image(getClass().getResourceAsStream("/com/savadr/HerramientaFX8/Vista/Images/logoComb.png"))
                    .setDimension(100, 60)
                    .setHorizontalImageAlignment(HorizontalImageAlignment.LEFT)
                    .setStyle(stl.style().setPadding(10));
            // Report Date**
            ComponentBuilder<?, ?> dateComponent = Components.text("Fecha: " + date)
                    .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
                    .setStyle(textHeaderStyle);
            // Header Set**
            VerticalListBuilder headerConfig = cmp.verticalList()
                    .add(cmp.horizontalList()
                            .add(logoComponent, cmp.horizontalGap(10), titleComponent, cmp.horizontalGap(10), dateComponent)
                            .setStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)));
            report.title(headerConfig);
            //Ends Header Config
            //Set table content
            for (TableColumn<T, ?> col : tableView.getColumns()) {
                String propertyName = ((PropertyValueFactory) col.getCellValueFactory()).getProperty();
                TextColumnBuilder<?> column;
                if (col.getCellData(0) instanceof String) {
                    column = DynamicReports.col.column(
                                    col.getText(), propertyName, DynamicReports.type.stringType()
                            ).setStyle(columnStyle)
                            .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
                            .setTitleStyle(columnTitleStyle)
                            .setFixedRows(1).setStretchWithOverflow(false);
                } else if (col.getCellData(0) instanceof Integer) {
                    column = DynamicReports.col.column(
                                    col.getText(), propertyName, DynamicReports.type.integerType()
                            ).setStyle(columnStyle)
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                            .setTitleStyle(columnTitleStyle);
                } else if (col.getCellData(0) instanceof Double) {
                    column = DynamicReports.col.column(
                                    col.getText(), propertyName, DynamicReports.type.doubleType()
                            ).setStyle(columnStyle)
                            .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
                            .setTitleStyle(columnTitleStyle);
                } else {
                    // Para otros tipos de datos, puedes definir una alineación predeterminada
                    column = DynamicReports.col.column(
                                    col.getText(), propertyName, DynamicReports.type.stringType()
                            ).setStyle(columnStyle)
                            .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
                            .setTitleStyle(columnTitleStyle);
                }
                report.addColumn(column);
            }
            //End table content
            //Footer Config
            ComponentBuilder<?, ?> textComponent = Components.text("Elaboro: ")
                    .setStyle(footerStyle)
                    .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
            ComponentBuilder<?, ?> pageComponent = cmp.text("Página: ").setStyle(footerStyle)
                    .setStyle(footerStyle)
                    .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
            HorizontalListBuilder footerList = cmp.horizontalList()
                    .add(textComponent)
                    .add(pageComponent, cmp.pageXofY())
                    .setStyle(stl.style().setPadding(10).setBorder(stl.pen2Point()));
            report.pageFooter(footerList);
            //Ends Footer Config
            //Set data source
            ObservableList<T> items = tableView.getItems();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
            report.setDataSource(dataSource);
            //Ends data source
            //Save stage to PDF via FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try (OutputStream out = new FileOutputStream(file)) {
                    report.toPdf(out);
                }catch(FileNotFoundException e){
                    if(e.getMessage().contains("El proceso no tiene acceso al archivo porque está siendo utilizado por otro proceso")){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("El archivo ya está siendo utilizado por otro proceso y no puede ser reemplazado por el momento.");
                        alert.showAndWait();
                    }else{
                        throw e;
                    }
                }
            }
            //Ends save stage to PDF via FileChooser
        } catch (DRException | IOException e) {
            e.printStackTrace();
        }
    }
    public <T> void exportToExcel(TableView<T> tableView, Stage stage, String headerText, String sheetName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar A Excel");
        FileChooser.ExtensionFilter extFilterXLSX = new FileChooser.ExtensionFilter("Archivo Excel (.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter extFilterXLS = new FileChooser.ExtensionFilter("Archivo Excel Antiguo (.xls)", "*.xls");
        FileChooser.ExtensionFilter extFilterCSV = new FileChooser.ExtensionFilter("Archivo CSV (.csv)", "*.csv");
        fileChooser.getExtensionFilters().addAll(extFilterXLSX, extFilterXLS, extFilterCSV);

        File file = fileChooser.showSaveDialog(stage);
        String fileExtension = getFileExtension(file);

        if (file != null) {
            try {
                if (fileExtension.equals("xlsx")) {
                    //Construct file
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet(sheetName);
                    //Get Data
                    ObservableList<TableColumn<T, ?>> columns = tableView.getColumns();
                    ObservableList<T> items = tableView.getItems();
                    //Put column headers and fill row data
                    Row headerRow = sheet.createRow(0);
                    for (int i = 0; i < columns.size(); i++) {
                        headerRow.createCell(i).setCellValue(columns.get(i).getText());
                    }
                    for (int i = 0; i < items.size(); i++) {
                        Row row = sheet.createRow(i + 1);
                        for (int j = 0; j < columns.size(); j++) {
                            TableColumn<T, ?> column = columns.get(j);
                            Object cellData = column.getCellData(i);
                            row.createCell(j).setCellValue(String.valueOf(cellData));
                        }
                    }
                    //Set sheet header, footer and data margin
                    sheet.getHeader().setCenter(headerText);
                    sheet.getFooter().setCenter(sheetName);

                    for (int i = 0; i < columns.size(); i++) {
                        sheet.autoSizeColumn(i);
                    }

                    try (FileOutputStream fileOut = new FileOutputStream(file)) {
                        workbook.write(fileOut);
                    }
                    workbook.close();
                } else if (fileExtension.equals("xls")) {
                    Workbook workbook = new HSSFWorkbook();
                    Sheet sheet = workbook.createSheet(sheetName);
                    ObservableList<TableColumn<T, ?>> columns = tableView.getColumns();
                    ObservableList<T> items = tableView.getItems();
                    Row headerRow = sheet.createRow(0);
                    for (int i = 0; i < columns.size(); i++) {
                        headerRow.createCell(i).setCellValue(columns.get(i).getText());
                    }
                    for (int i = 0; i < items.size(); i++) {
                        Row row = sheet.createRow(i + 1);
                        for (int j = 0; j < columns.size(); j++) {
                            TableColumn<T, ?> column = columns.get(j);
                            Object cellData = column.getCellData(i);
                            row.createCell(j).setCellValue(String.valueOf(cellData));
                        }
                    }
                    sheet.getHeader().setCenter(headerText);
                    sheet.getFooter().setCenter(sheetName);
                    for (int i = 0; i < columns.size(); i++) {
                        sheet.autoSizeColumn(i);
                    }
                    try (FileOutputStream fileOut = new FileOutputStream(file)) {
                        workbook.write(fileOut);
                    }
                    workbook.close();
                } else if (fileExtension.equals("csv")) {
                    FileWriter csvWriter = new FileWriter(file);
                    ObservableList<TableColumn<T, ?>> columns = tableView.getColumns();
                    ObservableList<T> items = tableView.getItems();
                    for (int i = 0; i < columns.size(); i++) {
                        csvWriter.append(columns.get(i).getText());
                        if (i < columns.size() - 1) {
                            csvWriter.append(",");
                        } else {
                            csvWriter.append("\n");
                        }
                    }
                    for (T item : items) {
                        for (int i = 0; i < columns.size(); i++) {
                            TableColumn<T, ?> column = columns.get(i);
                            Object cellData = column.getCellData(item);
                            csvWriter.append(String.valueOf(cellData));
                            if (i < columns.size() - 1) {
                                csvWriter.append(",");
                            } else {
                                csvWriter.append("\n");
                            }
                        }
                    }
                    csvWriter.flush();
                    csvWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileExtension(File file) {
        if (file != null) {
            String fileName = file.getName();
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                return fileName.substring(dotIndex + 1).toLowerCase();
            }
        }
        return "";
    }
}