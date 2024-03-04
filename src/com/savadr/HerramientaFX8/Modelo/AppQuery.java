package com.savadr.HerramientaFX8.Modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 *
 * @author Yair-PC
 */
public class AppQuery {
    private Connection connection;
    public AppQuery(Connection connection) {
        this.connection = connection;
    }
    //starts methods for class Existencias
    public ObservableList<com.savadr.HerramientaFX8.Modelo.Existencias> getStockGral(String filterArt, String filterDesc, String filterGru, boolean cbxExiSelected, boolean cbxBusGrSelected, boolean cbxArtSelected) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.Existencias> stockList = FXCollections.observableArrayList();
        String SQLGral = "WITH ExistenciaSurtidora AS (\n"
                + "	SELECT\n"
                + "		inv_articulo_id,\n"
                + "		inv_almacen_id,\n"
                + "		ROUND(surtidora.inv_existencia.cantidad_existencia, 0) AS cantidad_existencia,\n"
                + "		ROUND(surtidora.inv_existencia.costo_promedio, 2) AS costo_promedio\n"
                + "	FROM surtidora.inv_existencia\n"
                + "	WHERE inv_almacen_id IN (1, 2, 3, 4, 5, 6, 7)\n"
                + "),\n"
                + "ExistenciaAdrimar AS (\n"
                + "	SELECT\n"
                + "		inv_articulo_id,\n"
                + "		inv_almacen_id,\n"
                + "		ROUND(adrimar.inv_existencia.cantidad_existencia, 0) AS cantidad_existencia,\n"
                + "		ROUND(adrimar.inv_existencia.costo_promedio, 2) AS costo_promedio\n"
                + "	FROM adrimar.inv_existencia\n"
                + "	WHERE inv_almacen_id IN (1, 2, 3)\n"
                + ")\n"
                + "SELECT\n"
                + "	surtidora.inv_clasificacion.codigo_completo AS Clasificacion,\n"
                + "	surtidora.inv_articulo.codigo AS Articulo,\n"
                + "	surtidora.inv_articulo.descripcion AS Descripcion,\n"
                + "	COALESCE(Es.cantidad_existencia, 0) AS Existencia,\n"
                + "	ROUND(surtidora.inv_articulo.ultimo_costo_mxn, 2) AS \"Ultimo Costo\",\n"
                + "	COALESCE(Es.costo_promedio, 0) AS \"Costo Promedio\",\n"
                + "	CASE\n"
                + "		WHEN Es.inv_almacen_id = 1 THEN 'CBA'\n"
                + "		WHEN Es.inv_almacen_id = 2 THEN 'COA'\n"
                + "		WHEN Es.inv_almacen_id = 3 THEN 'MER'\n"
                + "		WHEN Es.inv_almacen_id = 4 THEN 'SAV'\n"
                + "		WHEN Es.inv_almacen_id = 5 THEN 'TEJ'\n"
                + "		WHEN Es.inv_almacen_id = 6 THEN 'TUX'\n"
                + "		WHEN Es.inv_almacen_id = 7 THEN 'VIL'\n"
                + "	END AS Almacen\n"
                + "FROM surtidora.inv_articulo\n"
                + "JOIN surtidora.inv_clasificacion ON surtidora.inv_articulo.inv_clasificacion_id = surtidora.inv_clasificacion.id\n"
                + "LEFT JOIN ExistenciaSurtidora Es ON surtidora.inv_articulo.id = Es.inv_articulo_id\n"
                + "WHERE\n"
                + (cbxArtSelected ? "surtidora.inv_articulo.codigo = ?\n" : "surtidora.inv_articulo.codigo LIKE ?\n")
                + "	AND surtidora.inv_clasificacion.codigo_completo LIKE ?\n"
                + "	AND surtidora.inv_articulo.descripcion LIKE ?\n"
                + (cbxExiSelected ? " AND Es.cantidad_existencia > 0\n" : "")
                + "UNION ALL\n"
                + "SELECT\n"
                + "	adrimar.inv_clasificacion.codigo_completo AS Clasificacion,\n"
                + "	adrimar.inv_articulo.codigo AS Articulo,\n"
                + "	adrimar.inv_articulo.descripcion AS Descripcion,\n"
                + "	COALESCE(Ea.cantidad_existencia, 0) AS Existencias,\n"
                + "	ROUND(adrimar.inv_articulo.ultimo_costo_mxn, 2) AS \"Ultimo Costo\",\n"
                + "	COALESCE(Ea.costo_promedio, 0) AS \"Costo Promedio\",\n"
                + "	CASE\n"
                + "		WHEN Ea.inv_almacen_id = 1 THEN 'ADR'\n"
                + "		WHEN Ea.inv_almacen_id = 2 THEN 'CUA'\n"
                + "		WHEN Ea.inv_almacen_id = 3 THEN 'XAL'\n"
                + "	END AS Almacen\n"
                + "FROM adrimar.inv_articulo\n"
                + "JOIN adrimar.inv_clasificacion ON adrimar.inv_articulo.inv_clasificacion_id = adrimar.inv_clasificacion.id\n"
                + "LEFT JOIN ExistenciaAdrimar Ea ON adrimar.inv_articulo.id = Ea.inv_articulo_id\n"
                + "WHERE\n"
                + (cbxArtSelected ? "adrimar.inv_articulo.codigo = ?\n" : "adrimar.inv_articulo.codigo LIKE ?\n")
                + "	AND adrimar.inv_clasificacion.codigo_completo LIKE ?\n"
                + "	AND adrimar.inv_articulo.descripcion LIKE ?\n"
                + (cbxExiSelected ? " AND Ea.cantidad_existencia > 0" : "");
        try {
            PreparedStatement ps = connection.prepareStatement(SQLGral);
            ps.setString(1, cbxArtSelected ? filterArt : "%" + filterArt + "%");
            ps.setString(2, filterGru + "%");
            ps.setString(3, "%" + filterDesc + "%");
            ps.setString(4, cbxArtSelected ? filterArt : "%" + filterArt + "%");
            ps.setString(5, filterGru + "%");
            ps.setString(6, "%" + filterDesc + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Existencias existencia = new Existencias();
                existencia.setArtClas(rs.getString("Clasificacion"));
                existencia.setArtCod(rs.getString("Articulo"));
                existencia.setArtDesc(rs.getString("Descripcion"));
                existencia.setExistencia(rs.getInt("Existencia"));
                existencia.setUltimoCosto(rs.getDouble("Ultimo Costo"));
                existencia.setCostoPromedio(rs.getDouble("Costo Promedio"));
                existencia.setAlmacen(rs.getString("Almacen"));
                stockList.add(existencia);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stockList;
    }
    public ObservableList<com.savadr.HerramientaFX8.Modelo.Existencias> getStockSav(String filterArt, String filterDesc, String filterGru, boolean cbxExiSelected, boolean cbxBusGrSelected, boolean cbxArtSelected, boolean cbxSavSelected, Integer almSavIdSelected) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.Existencias> stockList = FXCollections.observableArrayList();
        String cbxArtCondition = (cbxArtSelected && !cbxBusGrSelected) ? "surtidora.inv_articulo.codigo = ?" : "surtidora.inv_articulo.codigo LIKE ?";
        String cbxExistCondition = cbxExiSelected ? "AND Es.cantidad_existencia > 0" : "";
        String SQL = "WITH ExistenciaSurtidora AS (\n"
                + "	SELECT\n"
                + "		inv_articulo_id,\n"
                + "		inv_almacen_id,\n"
                + "		ROUND(surtidora.inv_existencia.cantidad_existencia, 0) AS cantidad_existencia,\n"
                + "		ROUND(surtidora.inv_existencia.costo_promedio, 2) AS costo_promedio\n"
                + "	FROM surtidora.inv_existencia\n"
                + "	WHERE inv_almacen_id IN (1, 2, 3, 4, 5, 6, 7)\n"
                + ")\n"
                + "SELECT\n"
                + "	surtidora.inv_clasificacion.codigo_completo AS Clasificacion,\n"
                + "	surtidora.inv_articulo.codigo AS Articulo,\n"
                + "	surtidora.inv_articulo.descripcion AS Descripcion,\n"
                + "	COALESCE(Es.cantidad_existencia, 0) AS Existencia,\n"
                + "	ROUND(surtidora.inv_articulo.ultimo_costo_mxn, 2) AS \"Ultimo costo\",\n"
                + "	COALESCE(Es.costo_promedio, 0) AS \"Costo promedio\",\n"
                + "	CASE\n"
                + "		WHEN Es.inv_almacen_id = 1 THEN 'CBA'\n"
                + "		WHEN Es.inv_almacen_id = 2 THEN 'COA'\n"
                + "		WHEN Es.inv_almacen_id = 3 THEN 'MER'\n"
                + "		WHEN Es.inv_almacen_id = 4 THEN 'SAV'\n"
                + "		WHEN Es.inv_almacen_id = 5 THEN 'TEJ'\n"
                + "		WHEN Es.inv_almacen_id = 6 THEN 'TUX'\n"
                + "		WHEN Es.inv_almacen_id = 7 THEN 'VIL'\n"
                + "	END AS Almacen\n"
                + "FROM surtidora.inv_articulo\n"
                + "JOIN surtidora.inv_clasificacion ON surtidora.inv_articulo.inv_clasificacion_id = surtidora.inv_clasificacion.id\n"
                + "LEFT JOIN ExistenciaSurtidora Es ON surtidora.inv_articulo.id = Es.inv_articulo_id\n"
                + "WHERE\n"
                + cbxArtCondition
                + "	AND surtidora.inv_clasificacion.codigo_completo LIKE ?\n"
                + "	AND surtidora.inv_articulo.descripcion LIKE ?\n"
                + "	AND Es.inv_almacen_id = ?\n"
                + cbxExistCondition;
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, cbxArtSelected ? filterArt : "%" + filterArt + "%");
            ps.setString(2, filterGru + "%");
            ps.setString(3, "%" + filterDesc + "%");
            ps.setInt(4, almSavIdSelected);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Existencias existencia = new Existencias();
                existencia.setArtClas(rs.getString("Clasificacion"));
                existencia.setArtCod(rs.getString("Articulo"));
                existencia.setArtDesc(rs.getString("Descripcion"));
                existencia.setExistencia(rs.getInt("Existencia"));
                existencia.setUltimoCosto(rs.getDouble("Ultimo Costo"));
                existencia.setCostoPromedio(rs.getDouble("Costo Promedio"));
                existencia.setAlmacen(rs.getString("Almacen"));
                stockList.add(existencia);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stockList;
    }
    public ObservableList<com.savadr.HerramientaFX8.Modelo.Existencias> getStockAdr(String filterArt, String filterDesc, String filterGru, boolean cbxExiSelected, boolean cbxBusGrSelected, boolean cbxArtSelected, boolean cbxSavSelected, boolean cbxAdrSelected, Integer almAdrIdSelected) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.Existencias> stockList = FXCollections.observableArrayList();
        String cbxArtCondition = (cbxArtSelected && !cbxBusGrSelected) ? "adrimar.inv_articulo.codigo = ?" : "adrimar.inv_articulo.codigo LIKE ?";
        String cbxExistCondition = cbxExiSelected ? "AND Ea.cantidad_existencia > 0" : "";
        String SQL = "WITH ExistenciasAdrimar AS(\n"
                + "	SELECT\n"
                + "		inv_articulo_id,\n"
                + "		inv_almacen_id,\n"
                + "		ROUND(adrimar.inv_existencia.cantidad_existencia, 0) AS cantidad_existencia,\n"
                + "		ROUND(adrimar.inv_existencia.costo_promedio, 2) AS costo_promedio\n"
                + "	FROM\n"
                + "		adrimar.inv_existencia\n"
                + "	WHERE\n"
                + "		inv_almacen_id  IN (1 , 2, 3)\n"
                + ")\n"
                + "SELECT\n"
                + "	adrimar.inv_clasificacion.codigo_completo AS Clasificacion,\n"
                + "	adrimar.inv_articulo.codigo AS Articulo,\n"
                + "	adrimar.inv_articulo.descripcion AS Descripcion,\n"
                + "	COALESCE(Ea.cantidad_existencia, 0) AS Existencia,\n"
                + "	ROUND(adrimar.inv_articulo.ultimo_costo_mxn, 2) AS \"Ultimo Costo\",\n"
                + "	COALESCE(Ea.costo_promedio, 0) AS \"Costo promedio\",\n"
                + "	CASE\n"
                + "		WHEN Ea.inv_almacen_id = 1 THEN 'ADR'\n"
                + "		WHEN Ea.inv_almacen_id = 2 THEN 'SUC'\n"
                + "		WHEN Ea.inv_almacen_id = 3 THEN 'XAL'\n"
                + "	END AS Almacen\n"
                + "FROM adrimar.inv_articulo\n"
                + "JOIN adrimar.inv_clasificacion ON adrimar.inv_articulo.inv_clasificacion_id = adrimar.inv_clasificacion.id\n"
                + "LEFT JOIN ExistenciasAdrimar Ea ON adrimar.inv_articulo.id = Ea.inv_articulo_id\n"
                + "WHERE\n"
                + cbxArtCondition
                + "	AND adrimar.inv_clasificacion.codigo_completo LIKE ?\n"
                + "	AND adrimar.inv_articulo.descripcion LIKE ?\n"
                + "	AND Ea.inv_almacen_id = ?\n"
                + cbxExistCondition;
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, cbxArtSelected ? filterArt : "%" + filterArt + "%");
            ps.setString(2, filterGru + "%");
            ps.setString(3, "%" + filterDesc + "%");
            ps.setInt(4, almAdrIdSelected);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Existencias existencia = new Existencias();
                existencia.setArtClas(rs.getString("Clasificacion"));
                existencia.setArtCod(rs.getString("Articulo"));
                existencia.setArtDesc(rs.getString("Descripcion"));
                existencia.setExistencia(rs.getInt("Existencia"));
                existencia.setUltimoCosto(rs.getDouble("Ultimo Costo"));
                existencia.setCostoPromedio(rs.getDouble("Costo Promedio"));
                existencia.setAlmacen(rs.getString("Almacen"));
                stockList.add(existencia);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stockList;
    }
    //Ends methods for Module Existencias
    //Starts methods for Module HistoricoPrecios
    public ObservableList<com.savadr.HerramientaFX8.Modelo.HistoricoPrecios> getHistory(String filterGru, boolean cbxGruSelected, Date dateInitial, Date dateFinal) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.HistoricoPrecios> historyList = FXCollections.observableArrayList();
        String SQL = "WITH ultimos_precios AS (\n"
                + "	SELECT\n"
                + "		inv_articulo.id,\n"
                + "        inv_clasificacion.codigo_completo,\n"
                + "        inv_articulo.codigo,\n"
                + "        inv_articulo.descripcion,\n"
                + "        inv_historial_lista_precio.precio AS precio_actual,\n"
                + "        (\n"
                + "			SELECT inv_historial_lista_precio.precio\n"
                + "			FROM inv_historial_cambio_precio\n"
                + "            JOIN inv_historial_lista_precio ON inv_historial_cambio_precio.id = inv_historial_lista_precio.inv_historial_cambio_precio_id\n"
                + "            WHERE inv_historial_cambio_precio.inv_articulo_id = inv_articulo.id\n"
                + "            AND DATE(inv_historial_cambio_precio.fecha_modificacion) < ?\n"
                + "            AND gen_moneda_id = 1\n"
                + "            AND orden = 1\n"
                + "            AND inv_historial_cambio_precio.usuario_id IN (1, 5, 34, 35)\n"
                + "            ORDER BY inv_historial_cambio_precio.fecha_modificacion DESC\n"
                + "            LIMIT 1) AS precio_anterior,\n"
                + "	ROW_NUMBER() OVER (PARTITION BY inv_articulo.id ORDER BY inv_historial_cambio_precio.fecha_modificacion DESC) AS rn\n"
                + "    FROM inv_articulo\n"
                + "    JOIN inv_clasificacion ON inv_articulo.inv_clasificacion_id = inv_clasificacion.id\n"
                + "    JOIN inv_historial_cambio_precio ON inv_articulo.id = inv_historial_cambio_precio.inv_articulo_id\n"
                + "    JOIN inv_historial_lista_precio ON inv_historial_cambio_precio.id = inv_historial_lista_precio.inv_historial_cambio_precio_id\n"
                + "    WHERE\n"
                + "		gen_moneda_id = 1\n"
                + "        AND orden = 1\n"
                + "        AND inv_historial_cambio_precio.usuario_id IN (1, 5, 34, 35)\n"
                + "        AND DATE(inv_historial_cambio_precio.fecha_modificacion) BETWEEN ? AND ?\n"
                + ")\n"
                + "SELECT DISTINCT\n"
                + "	codigo_completo,\n"
                + "    codigo,\n"
                + "    descripcion,\n"
                + "    ROUND(COALESCE(precio_anterior, 0),2) AS precio_anterior,\n"
                + "    ROUND(precio_actual,2) As precio_actual\n"
                + "FROM ultimos_precios\n"
                + "WHERE\n"
                + "	rn = 1\n"
                + "    AND codigo_completo <> '20 099' &&  '20'\n"
                + (!cbxGruSelected ? "" : " AND codigo_completo LIKE ?;");
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setDate(1, dateInitial);
            ps.setDate(2, dateInitial);
            ps.setDate(3, dateFinal);
            if (cbxGruSelected) {
                ps.setString(4, filterGru + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HistoricoPrecios histPre = new HistoricoPrecios();
                histPre.setArtGru(rs.getString("codigo_completo"));
                histPre.setArtCod(rs.getString("codigo"));
                histPre.setArtDesc(rs.getString("descripcion"));
                histPre.setPrecioAnt(rs.getDouble("precio_anterior"));
                histPre.setPrecioAct(rs.getDouble("precio_actual"));
                double margen = ((histPre.getPrecioAct() - histPre.getPrecioAnt()) / histPre.getPrecioAnt()) * 100;
                histPre.setMargen(margen);
                historyList.add(histPre);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return historyList;
    }
    //Ends methods for Module HistoricoPrecios
    //Starts methods for Module UnidadesVendidas
    public ObservableList<com.savadr.HerramientaFX8.Modelo.UnidadesVendidas> getUnitSoldSav(Date dateInitial, Date dateFinal, Integer almSavIdSelected) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.UnidadesVendidas> soldList = FXCollections.observableArrayList();
        String SQL = "SELECT\n"
                + "	inv_clasificacion.codigo_completo AS Clasificacion,\n"
                + "	inv_articulo.codigo AS Articulo,\n"
                + "	inv_articulo.descripcion AS Descripcion,\n"
                + "	inv_ubicacion.codigo_completo AS Ubicacion,\n"
                + "	ROUND(inv_existencia.cantidad_existencia, 0) AS Existencia,\n"
                + "	ROUND(SUM(inv_kardex.cantidad_salida), 0) AS Salida\n"
                + "FROM inv_articulo\n"
                + "JOIN inv_clasificacion ON inv_articulo.inv_clasificacion_id = inv_clasificacion.id\n"
                + "JOIN inv_kardex ON inv_articulo.id = inv_kardex.inv_articulo_id\n"
                + "JOIN inv_existencia ON inv_articulo.id = inv_existencia.inv_articulo_id\n"
                + "JOIN inv_articulo_config_almacen ON inv_articulo.id = inv_articulo_config_almacen.inv_articulo_id\n"
                + "JOIN inv_articulo_config_almacen_ubicacion ON inv_articulo_config_almacen.id = inv_articulo_config_almacen_ubicacion.inv_articulo_config_almacen_id\n"
                + "JOIN inv_ubicacion ON inv_articulo_config_almacen_ubicacion.inv_ubicacion_id = inv_ubicacion.id\n"
                + "WHERE DATE(fecha) BETWEEN ? AND ?\n"
                + "	AND inv_tipo_operacion_id IN (3, 6, 32)\n"
                + "	AND inv_kardex.inv_almacen_id = ?\n"
                + "	AND inv_existencia.inv_almacen_id = ?\n"
                + "	AND inv_ubicacion.inv_almacen_id = ?\n"
                + "GROUP BY inv_clasificacion.codigo_completo, inv_articulo.codigo, inv_articulo.descripcion, Ubicacion\n"
                + "  UNION ALL\n"
                + "SELECT\n"
                + "	inv_clasificacion.codigo_completo AS Clasificacion,\n"
                + "	inv_articulo.codigo AS Articulo,\n"
                + "	inv_articulo.descripcion AS Descripcion,\n"
                + "	'Sin ubicacion' AS Ubicacion,\n"
                + "	ROUND(inv_existencia.cantidad_existencia,0) AS Existencia,\n"
                + "	ROUND(SUM(inv_kardex.cantidad_salida), 0) AS Salida\n"
                + "FROM inv_articulo\n"
                + "JOIN inv_clasificacion ON inv_articulo.inv_clasificacion_id = inv_clasificacion.id\n"
                + "JOIN inv_kardex ON inv_articulo.id = inv_kardex.inv_articulo_id\n"
                + "JOIN inv_existencia ON inv_articulo.id = inv_existencia.inv_articulo_id\n"
                + "WHERE DATE(fecha) BETWEEN ? AND ?\n"
                + "	AND inv_tipo_operacion_id IN (3, 6, 32)\n"
                + "	AND inv_kardex.inv_almacen_id = ?\n"
                + "	AND inv_existencia.inv_almacen_id = ?\n"
                + "	AND inv_articulo.id NOT IN (\n"
                + "		SELECT DISTINCT inv_articulo.id\n"
                + "		FROM inv_articulo\n"
                + "		JOIN inv_articulo_config_almacen ON inv_articulo.id = inv_articulo_config_almacen.inv_articulo_id\n"
                + "		JOIN inv_articulo_config_almacen_ubicacion ON inv_articulo_config_almacen.id = inv_articulo_config_almacen_ubicacion.inv_articulo_config_almacen_id\n"
                + "		JOIN inv_ubicacion ON inv_articulo_config_almacen_ubicacion.inv_ubicacion_id = inv_ubicacion.id\n"
                + "		WHERE inv_ubicacion.inv_almacen_id = ?\n"
                + "	)\n"
                + "GROUP BY inv_clasificacion.codigo_completo, inv_articulo.codigo, inv_articulo.descripcion, Ubicacion;";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setDate(1, dateInitial);
            ps.setDate(2, dateFinal);
            ps.setInt(3, almSavIdSelected);
            ps.setInt(4, almSavIdSelected);
            ps.setInt(5, almSavIdSelected);
            ps.setDate(6, dateInitial);
            ps.setDate(7, dateFinal);
            ps.setInt(8, almSavIdSelected);
            ps.setInt(9, almSavIdSelected);
            ps.setInt(10, almSavIdSelected);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UnidadesVendidas unidVend = new UnidadesVendidas();
                unidVend.setArtGru(rs.getString("Clasificacion"));
                unidVend.setArtCod(rs.getString("Articulo"));
                unidVend.setArtDesc(rs.getString("Descripcion"));
                unidVend.setArtUbi(rs.getString("Ubicacion"));
                unidVend.setStock(rs.getInt("Existencia"));
                unidVend.setSoldOut(rs.getInt("Salida"));
                soldList.add(unidVend);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return soldList;

    }
    public ObservableList<com.savadr.HerramientaFX8.Modelo.UnidadesVendidas> getUnitSoldAdr(Date dateInitial, Date dateFinal, Integer almAdrIdSelected) {
        ObservableList<com.savadr.HerramientaFX8.Modelo.UnidadesVendidas> soldList = FXCollections.observableArrayList();
        String SQL = "SELECT\n"
                + "	inv_clasificacion.codigo_completo AS Clasificacion,\n"
                + "	inv_articulo.codigo AS Articulo,\n"
                + "	inv_articulo.descripcion AS Descripcion,\n"
                + "	inv_ubicacion.codigo_completo AS Ubicacion,\n"
                + "	ROUND(inv_existencia.cantidad_existencia, 0) AS Existencia,\n"
                + "	ROUND(SUM(inv_kardex.cantidad_salida), 0) AS Salida\n"
                + "FROM inv_articulo\n"
                + "JOIN inv_clasificacion ON inv_articulo.inv_clasificacion_id = inv_clasificacion.id\n"
                + "JOIN inv_kardex ON inv_articulo.id = inv_kardex.inv_articulo_id\n"
                + "JOIN inv_existencia ON inv_articulo.id = inv_existencia.inv_articulo_id\n"
                + "JOIN inv_articulo_config_almacen ON inv_articulo.id = inv_articulo_config_almacen.inv_articulo_id\n"
                + "JOIN inv_articulo_config_almacen_ubicacion ON inv_articulo_config_almacen.id = inv_articulo_config_almacen_ubicacion.inv_articulo_config_almacen_id\n"
                + "JOIN inv_ubicacion ON inv_articulo_config_almacen_ubicacion.inv_ubicacion_id = inv_ubicacion.id\n"
                + "WHERE DATE(fecha) BETWEEN ? AND ?\n"
                + "	AND inv_tipo_operacion_id IN (3, 6, 32)\n"
                + "	AND inv_kardex.inv_almacen_id = ?\n"
                + "	AND inv_existencia.inv_almacen_id = ?\n"
                + "	AND inv_ubicacion.inv_almacen_id = ?\n"
                + "GROUP BY inv_clasificacion.codigo_completo, inv_articulo.codigo, inv_articulo.descripcion, Ubicacion\n"
                + "  UNION ALL\n"
                + "SELECT\n"
                + "	inv_clasificacion.codigo_completo AS Clasificacion,\n"
                + "	inv_articulo.codigo AS Articulo,\n"
                + "	inv_articulo.descripcion AS Descripcion,\n"
                + "	'Sin ubicacion' AS Ubicacion,\n"
                + "	ROUND(inv_existencia.cantidad_existencia,0) AS Existencia,\n"
                + "	ROUND(SUM(inv_kardex.cantidad_salida), 0) AS Salida\n"
                + "FROM inv_articulo\n"
                + "JOIN inv_clasificacion ON inv_articulo.inv_clasificacion_id = inv_clasificacion.id\n"
                + "JOIN inv_kardex ON inv_articulo.id = inv_kardex.inv_articulo_id\n"
                + "JOIN inv_existencia ON inv_articulo.id = inv_existencia.inv_articulo_id\n"
                + "WHERE DATE(fecha) BETWEEN ? AND ?\n"
                + "	AND inv_tipo_operacion_id IN (3, 6, 32)\n"
                + "	AND inv_kardex.inv_almacen_id = ?\n"
                + "	AND inv_existencia.inv_almacen_id = ?\n"
                + "	AND inv_articulo.id NOT IN (\n"
                + "		SELECT DISTINCT inv_articulo.id\n"
                + "		FROM inv_articulo\n"
                + "		JOIN inv_articulo_config_almacen ON inv_articulo.id = inv_articulo_config_almacen.inv_articulo_id\n"
                + "		JOIN inv_articulo_config_almacen_ubicacion ON inv_articulo_config_almacen.id = inv_articulo_config_almacen_ubicacion.inv_articulo_config_almacen_id\n"
                + "		JOIN inv_ubicacion ON inv_articulo_config_almacen_ubicacion.inv_ubicacion_id = inv_ubicacion.id\n"
                + "		WHERE inv_ubicacion.inv_almacen_id = ?\n"
                + "	)\n"
                + "GROUP BY inv_clasificacion.codigo_completo, inv_articulo.codigo, inv_articulo.descripcion, Ubicacion;";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setDate(1, dateInitial);
            ps.setDate(2, dateFinal);
            ps.setInt(3, almAdrIdSelected);
            ps.setInt(4, almAdrIdSelected);
            ps.setInt(5, almAdrIdSelected);
            ps.setDate(6, dateInitial);
            ps.setDate(7, dateFinal);
            ps.setInt(8, almAdrIdSelected);
            ps.setInt(9, almAdrIdSelected);
            ps.setInt(10, almAdrIdSelected);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UnidadesVendidas unidVend = new UnidadesVendidas();
                unidVend.setArtGru(rs.getString("Clasificacion"));
                unidVend.setArtCod(rs.getString("Articulo"));
                unidVend.setArtDesc(rs.getString("Descripcion"));
                unidVend.setArtUbi(rs.getString("Ubicacion"));
                unidVend.setStock(rs.getInt("Existencia"));
                unidVend.setSoldOut(rs.getInt("Salida"));
                soldList.add(unidVend);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return soldList;
    }
    //Ends methods for Module UnidadesVendidas
}
