package com.savadr.HerramientaFX8.Modelo;

import java.time.LocalDate;

/**
 *
 * @author Yair-PC
 */
public class UnidadesVendidas {

    private String artGru;
    private String artCod;
    private String artDesc;
    private String artUbi;
    private String artCont;
    private int stock;
    private int soldOut;
    private LocalDate fechaIni;
    private LocalDate fechaFin;

    public UnidadesVendidas() {
    }
    public UnidadesVendidas(String artGru, String artCod, String artDesc, String artUbi, int stock, int soldOut, LocalDate fechaIni, LocalDate fechaFin) {
        this.artGru = artGru;
        this.artCod = artCod;
        this.artDesc = artDesc;
        this.artUbi = artUbi;
        this.stock = stock;
        this.soldOut = soldOut;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.artCont = "_____________";
    }
    public String getArtGru() {
        return artGru;
    }
    public void setArtGru(String artGru) {
        this.artGru = artGru;
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
    public String getArtUbi() {
        return artUbi;
    }
    public void setArtUbi(String artUbi) {
        this.artUbi = artUbi;
    }
    public int getStock() {
        return stock;
    }
    public String getArtCont() {
        return artCont;
    }

    public void setArtCont(String artCont) {
        this.artCont = artCont;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getSoldOut() {
        return soldOut;
    }
    public void setSoldOut(int soldOut) {
        this.soldOut = soldOut;
    }
    public LocalDate getFechaIni() {
        return fechaIni;
    }
    public void setFechaIni(LocalDate fechaIni) {
        this.fechaIni = fechaIni;
    }
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
