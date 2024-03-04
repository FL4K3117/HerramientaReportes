package com.savadr.HerramientaFX8.Modelo;

public class Estadisticas {

    String artGru;
    String artCod;
    String artDesc;
    String artCanCero;

    public Estadisticas() {
    }

    public Estadisticas(String artGru, String artCod, String artDesc, String artCanCero) {
        this.artGru = artGru;
        this.artCod = artCod;
        this.artDesc = artDesc;
        this.artCanCero = artCanCero;
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

    public String getArtCanCero() {
        return artCanCero;
    }

    public void setArtCanCero(String artCanCero) {
        this.artCanCero = artCanCero;
    }

}
