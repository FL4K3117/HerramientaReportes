package com.savadr.HerramientaFX8.Modelo;

/**
 *
 * @author Yair-PC
 */
public class Existencias {

    private String artCod;
    private String artDesc;
    private String artClas;
    private String almacen;
    private  String colGrup;
    private String colArt;
    private String colDesc;
    private String colAlm;
    private int idSucSav;
    private int idSucAdr;
    private int existencia;
    private int colExis;
    private double ultimoCosto;
    private double costoPromedio;
    private double colUltCos;
    private double colCosProm;
    private boolean busGral;
    private boolean artCods;
    private boolean sav;
    private boolean adr;

    public Existencias() {
    }

    public Existencias(String artCod, String artDesc, String artClas, String almacen, String colGrup, String colArt, String colDesc, String colAlm, int idSucSav, int idSucAdr, int existencia, int colExis, double ultimoCosto, double costoPromedio, double colUltCos, double colCosProm, boolean busGral, boolean artCods, boolean sav, boolean adr) {
        this.artCod = artCod;
        this.artDesc = artDesc;
        this.artClas = artClas;
        this.almacen = almacen;
        this.colGrup = colGrup;
        this.colArt = colArt;
        this.colDesc = colDesc;
        this.colAlm = colAlm;
        this.idSucSav = idSucSav;
        this.idSucAdr = idSucAdr;
        this.existencia = existencia;
        this.colExis = colExis;
        this.ultimoCosto = ultimoCosto;
        this.costoPromedio = costoPromedio;
        this.colUltCos = colUltCos;
        this.colCosProm = colCosProm;
        this.busGral = busGral;
        this.artCods = artCods;
        this.sav = sav;
        this.adr = adr;
    }

    public String getArtCod() {
        return artCod;
    }

    public void setArtCod(String artCod) {
        this.artCod = artCod;
    }

    public String getArtDesc() {
        return artDesc;
    }

    public void setArtDesc(String artDesc) {
        this.artDesc = artDesc;
    }

    public String getArtClas() {
        return artClas;
    }

    public void setArtClas(String artClas) {
        this.artClas = artClas;
    }

    public String getAlmacen() {
        return almacen;
    }

    public void setAlmacen(String almacen) {
        this.almacen = almacen;
    }

    public String getColGrup() {
        return colGrup;
    }

    public void setColGrup(String colGrup) {
        this.colGrup = colGrup;
    }

    public String getColArt() {
        return colArt;
    }

    public void setColArt(String colArt) {
        this.colArt = colArt;
    }

    public String getColDesc() {
        return colDesc;
    }

    public void setColDesc(String colDesc) {
        this.colDesc = colDesc;
    }

    public String getColAlm() {
        return colAlm;
    }

    public void setColAlm(String colAlm) {
        this.colAlm = colAlm;
    }

    public int getIdSucSav() {
        return idSucSav;
    }

    public void setIdSucSav(int idSucSav) {
        this.idSucSav = idSucSav;
    }

    public int getIdSucAdr() {
        return idSucAdr;
    }

    public void setIdSucAdr(int idSucAdr) {
        this.idSucAdr = idSucAdr;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public int getColExis() {
        return colExis;
    }

    public void setColExis(int colExis) {
        this.colExis = colExis;
    }

    public double getUltimoCosto() {
        return ultimoCosto;
    }

    public void setUltimoCosto(double ultimoCosto) {
        this.ultimoCosto = ultimoCosto;
    }

    public double getCostoPromedio() {
        return costoPromedio;
    }

    public void setCostoPromedio(double costoPromedio) {
        this.costoPromedio = costoPromedio;
    }

    public double getColUltCos() {
        return colUltCos;
    }

    public void setColUltCos(double colUltCos) {
        this.colUltCos = colUltCos;
    }

    public double getColCosProm() {
        return colCosProm;
    }

    public void setColCosProm(double colCosProm) {
        this.colCosProm = colCosProm;
    }

    public boolean isBusGral() {
        return busGral;
    }

    public void setBusGral(boolean busGral) {
        this.busGral = busGral;
    }

    public boolean isArtCods() {
        return artCods;
    }

    public void setArtCods(boolean artCods) {
        this.artCods = artCods;
    }

    public boolean isSav() {
        return sav;
    }

    public void setSav(boolean sav) {
        this.sav = sav;
    }

    public boolean isAdr() {
        return adr;
    }

    public void setAdr(boolean adr) {
        this.adr = adr;
    }



}
