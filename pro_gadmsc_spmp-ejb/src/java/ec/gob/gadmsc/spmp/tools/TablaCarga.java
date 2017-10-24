/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.tools;

/**
 *
 * @author MiguelAngel
 */
public class TablaCarga {

    private Meses mes;
    private Integer relleno;
    private Integer arenaFina;
    private Integer materialBloque;
    private Integer ripio;
    private Integer polvoPiedra;
    private Integer totalVolquetadasRelleno;
    private Integer totalVolquetadasArena;
    private Integer totalVolquetadasBloque;
    private Integer totalVolquetadasRipio;
    private Integer totalVolquetadasPolvo;
    private Integer totalM3Relleno;
    private Integer totalM3Arena;
    private Integer totalM3Bloque;
    private Integer totalM3Ripio;
    private Integer totalM3Polvo;

    public void calcularTotalesM3() {
        totalM3Relleno = totalVolquetadasRelleno * 8;
        totalM3Arena = totalVolquetadasArena * 8;
        totalM3Bloque = totalVolquetadasBloque * 8;
        totalM3Ripio = totalVolquetadasRipio * 8;
        totalM3Polvo = totalVolquetadasPolvo * 8;
    }

    public Meses getMes() {
        return mes;
    }

    public void setMes(Meses mes) {
        this.mes = mes;
    }

    public Integer getRelleno() {
        return relleno;
    }

    public void setRelleno(Integer relleno) {
        this.relleno = relleno;
    }

    public Integer getArenaFina() {
        return arenaFina;
    }

    public void setArenaFina(Integer arenaFina) {
        this.arenaFina = arenaFina;
    }

    public Integer getMaterialBloque() {
        return materialBloque;
    }

    public void setMaterialBloque(Integer materialBloque) {
        this.materialBloque = materialBloque;
    }

    public Integer getRipio() {
        return ripio;
    }

    public void setRipio(Integer ripio) {
        this.ripio = ripio;
    }

    public Integer getPolvoPiedra() {
        return polvoPiedra;
    }

    public void setPolvoPiedra(Integer polvoPiedra) {
        this.polvoPiedra = polvoPiedra;
    }

    public Integer getTotalVolquetadasRelleno() {
        return totalVolquetadasRelleno;
    }

    public void setTotalVolquetadasRelleno(Integer totalVolquetadasRelleno) {
        this.totalVolquetadasRelleno = totalVolquetadasRelleno;
    }

    public Integer getTotalVolquetadasArena() {
        return totalVolquetadasArena;
    }

    public void setTotalVolquetadasArena(Integer totalVolquetadasArena) {
        this.totalVolquetadasArena = totalVolquetadasArena;
    }

    public Integer getTotalVolquetadasBloque() {
        return totalVolquetadasBloque;
    }

    public void setTotalVolquetadasBloque(Integer totalVolquetadasBloque) {
        this.totalVolquetadasBloque = totalVolquetadasBloque;
    }

    public Integer getTotalVolquetadasRipio() {
        return totalVolquetadasRipio;
    }

    public void setTotalVolquetadasRipio(Integer totalVolquetadasRipio) {
        this.totalVolquetadasRipio = totalVolquetadasRipio;
    }

    public Integer getTotalVolquetadasPolvo() {
        return totalVolquetadasPolvo;
    }

    public void setTotalVolquetadasPolvo(Integer totalVolquetadasPolvo) {
        this.totalVolquetadasPolvo = totalVolquetadasPolvo;
    }

    public Integer getTotalM3Relleno() {
        return totalM3Relleno;
    }

    public void setTotalM3Relleno(Integer totalM3Relleno) {
        this.totalM3Relleno = totalM3Relleno;
    }

    public Integer getTotalM3Arena() {
        return totalM3Arena;
    }

    public void setTotalM3Arena(Integer totalM3Arena) {
        this.totalM3Arena = totalM3Arena;
    }

    public Integer getTotalM3Bloque() {
        return totalM3Bloque;
    }

    public void setTotalM3Bloque(Integer totalM3Bloque) {
        this.totalM3Bloque = totalM3Bloque;
    }

    public Integer getTotalM3Ripio() {
        return totalM3Ripio;
    }

    public void setTotalM3Ripio(Integer totalM3Ripio) {
        this.totalM3Ripio = totalM3Ripio;
    }

    public Integer getTotalM3Polvo() {
        return totalM3Polvo;
    }

    public void setTotalM3Polvo(Integer totalM3Polvo) {
        this.totalM3Polvo = totalM3Polvo;
    }
}
