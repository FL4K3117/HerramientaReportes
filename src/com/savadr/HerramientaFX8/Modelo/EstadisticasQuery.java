package com.savadr.HerramientaFX8.Modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadisticasQuery {

    private Connection connection;
    public EstadisticasQuery(Connection connection) {
        this.connection = connection;
    }

    //Querys for Module Statistics
    public ObservableList<Estadisticas> getArticleQuery() {
        ObservableList<com.savadr.HerramientaFX8.Modelo.Estadisticas> articleList = FXCollections.observableArrayList();
        String SQL = "";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Estadisticas statistics = new Estadisticas();
                statistics.setArtGru(rs.getString("Clasificacion"));
                statistics.setArtCod(rs.getString("Articulo"));
                statistics.setArtDesc(rs.getString("Descripcion"));
                statistics.setArtCanCero(rs.getString("Cantidad"));
                articleList.add(statistics);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return articleList;
    }
    public ObservableList<com.savadr.HerramientaFX8.Modelo.Estadisticas> getClientOrder(String folioCotizacion) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.Estadisticas> purchaseOrderList = FXCollections.observableArrayList();
        String SQL ="SELECT\n"
                +"\tic.codigo_completo As Clasificacion,\n"
                +"\tia.codigo As Articulo,\n"
                +"\tia.descripcion As Descripcion,\n"
                +"\tROUND(vd.cantidad,0) As Cantidad\n"
                +"FROM ven_detalle vd\n"
                +"JOIN inv_articulo ia ON vd.inv_articulo_id = ia.id\n"
                +"JOIN inv_clasificacion ic ON ia.inv_clasificacion_id = ic.id\n"
                +"JOIN ven_encabezado ve ON vd.ven_encabezado_id = ve.id\n"
                +"WHERE ve.estatus NOT IN ('CANCELADO') \n"
                +"\tAND ve.ven_tipo_operacion_id IN(1,2)\n"
                +"\tAND ve.folio = ?\n"
                +"GROUP BY Clasificacion,Articulo\n"
                +"ORDER BY Clasificacion, Articulo;";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, folioCotizacion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Estadisticas statistics = new Estadisticas();
                statistics.setArtGru(rs.getString("Clasificacion"));
                statistics.setArtCod(rs.getString("Articulo"));
                statistics.setArtDesc(rs.getString("Descripcion"));
                statistics.setArtCanCero(rs.getString("Cantidad"));
                purchaseOrderList.add(statistics);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return purchaseOrderList;
    }
    public ObservableList<com.savadr.HerramientaFX8.Modelo.Estadisticas> getPurchaseOrder(String folioOrdenCompra) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.Estadisticas> purchaseOrderList = FXCollections.observableArrayList();
        String SQL = "SELECT\n"
                +"\tcd.inv_articulo_id as ID,\n"
                +"\tic.codigo_completo As Clasificacion,\n"
                +"\tia.codigo As Articulo,\n"
                +"    ia.descripcion As Descripcion,\n"
                +"\tROUND(cd.cantidad) as Cantidad\n"
                +"FROM com_detalle cd\n"
                +"JOIN inv_articulo ia ON cd.inv_articulo_id = ia.id\n"
                +"JOIN inv_clasificacion ic ON ia.inv_clasificacion_id = ic.id\n"
                +"JOIN com_encabezado ce ON cd.com_encabezado_id = ce.id\n"
                +"WHERE ce.estatus NOT IN ('CANCELADO')\n"
                +"\tAND ce.com_tipo_operacion_id in (3)\n"
                +"\tAND ce.folio = ?\n"
                +"GROUP BY Clasificacion,Articulo\n"
                +"ORDER BY Clasificacion, Articulo;";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, folioOrdenCompra);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Estadisticas statistics = new Estadisticas();
                statistics.setArtGru(rs.getString("Clasificacion"));
                statistics.setArtCod(rs.getString("Articulo"));
                statistics.setArtDesc(rs.getString("Descripcion"));
                statistics.setArtCanCero(rs.getString("Cantidad"));
                purchaseOrderList.add(statistics);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return purchaseOrderList;
    }
    public ObservableList<com.savadr.HerramientaFX8.Modelo.Estadisticas> getBranchOrder(String folioPedidoSuc) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.Estadisticas> purchaseOrderList = FXCollections.observableArrayList();
        String SQL = "SELECT\n"
                +"\tipd.inv_articulo_id as ID,\n"
                +"\tic.codigo_completo As Clasificacion,\n"
                +"\tia.codigo As Articulo,\n"
                +"    ia.descripcion As Descripcion,\n"
                +"\tROUND(ipd.cantidad,0) As Cantidad\n"
                +"FROM inv_pedido_detalle ipd\n"
                +"JOIN inv_articulo ia ON ipd.inv_articulo_id = ia.id\n"
                +"JOIN inv_clasificacion ic ON ia.inv_clasificacion_id = ic.id\n"
                +"JOIN inv_pedido_encabezado ipe ON ipd.inv_pedido_encabezado_id = ipe.id\n"
                +"WHERE ipe.estatus NOT IN ('CANCELADO') \n"
                +"\tAND ipe.folio = ?\n"
                +"GROUP BY Clasificacion,Articulo\n"
                +"ORDER BY Clasificacion, Articulo;";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, folioPedidoSuc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Estadisticas statistics = new Estadisticas();
                statistics.setArtGru(rs.getString("Clasificacion"));
                statistics.setArtCod(rs.getString("Articulo"));
                statistics.setArtDesc(rs.getString("Descripcion"));
                statistics.setArtCanCero(rs.getString("Cantidad"));
                purchaseOrderList.add(statistics);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return purchaseOrderList;
    }
}
