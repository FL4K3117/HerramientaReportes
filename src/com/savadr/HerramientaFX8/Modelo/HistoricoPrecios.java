package com.savadr.HerramientaFX8.Modelo;

import java.time.LocalDate;

/**
 *
 * @author Yair-PC
 */
public class HistoricoPrecios {

    private String artDesc;
    private String artCod;
    private String artGru;
    private double precioAnt;
    private double precioAct;
    private double margen;
    private boolean grupo;
    private LocalDate fechaIni;
    private LocalDate fechaFin;

    public HistoricoPrecios() {
    }

    public HistoricoPrecios(String artDesc, String artCod, String artGru, double precioAnt, double precioAct, double margen, boolean grupo, LocalDate fechaIni, LocalDate fechaFin) {
        this.artDesc = artDesc;
        this.artCod = artCod;
        this.artGru = artGru;
        this.precioAnt = precioAnt;
        this.precioAct = precioAct;
        this.margen = margen;
        this.grupo = grupo;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
    }

    public String getArtDesc() {
        return artDesc;
    }

    public void setArtDesc(String artDesc) {
        this.artDesc = artDesc;
    }

    public String getArtCod() {
        return artCod;
    }

    public void setArtCod(String artCod) {
        this.artCod = artCod;
    }

    public String getArtGru() {
        return artGru;
    }

    public void setArtGru(String artGru) {
        this.artGru = artGru;
    }

    public Double getPrecioAnt() {
        return precioAnt;
    }

    public void setPrecioAnt(Double precioAnt) {
        this.precioAnt = precioAnt;
    }

    public Double getPrecioAct() {
        return precioAct;
    }

    public void setPrecioAct(Double precioAct) {
        this.precioAct = precioAct;
    }

    public Double getMargen() {
        return margen;
    }

    public void setMargen(Double margen) {
        this.margen = margen;
    }

    public Boolean getGrupo() {
        return grupo;
    }

    public void setGrupo(Boolean grupo) {
        this.grupo = grupo;
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
