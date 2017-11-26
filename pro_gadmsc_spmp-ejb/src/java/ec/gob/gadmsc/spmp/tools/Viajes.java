/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.tools;

import java.io.Serializable;

/**
 *
 * @author HOGAR
 */
public class Viajes implements Serializable {

    private Integer combustible;
    private Integer km;
    private Integer carga;
    private String comprobante;
    private String observacion;
    private Integer volquetaCodigo;
    private Integer cargaCodigo;

    public Viajes() {
    }

    public Viajes(Integer combustible, Integer km, Integer carga, String comprobante, String observacion, Integer volquetaCodigo, Integer cargaCodigo) {
        this.combustible = combustible;
        this.km = km;
        this.carga = carga;
        this.comprobante = comprobante;
        this.observacion = observacion;
        this.volquetaCodigo = volquetaCodigo;
        this.cargaCodigo = cargaCodigo;
    }

    public Integer getCombustible() {
        return combustible;
    }

    public void setCombustible(Integer combustible) {
        this.combustible = combustible;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getCarga() {
        return carga;
    }

    public void setCarga(Integer carga) {
        this.carga = carga;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getCargaCodigo() {
        return cargaCodigo;
    }

    public void setCargaCodigo(Integer cargaCodigo) {
        this.cargaCodigo = cargaCodigo;
    }

    public Integer getVolquetaCodigo() {
        return volquetaCodigo;
    }

    public void setVolquetaCodigo(Integer volquetaCodigo) {
        this.volquetaCodigo = volquetaCodigo;
    }

}
