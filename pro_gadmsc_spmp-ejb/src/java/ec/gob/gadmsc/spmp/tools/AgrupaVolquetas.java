/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.tools;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MiguelAngel
 */
public class AgrupaVolquetas {

    private String fecha;
    private List<Object[]> listaObjetos;

    public AgrupaVolquetas(String fecha) {
        this.fecha = fecha;
        listaObjetos = new ArrayList<>();
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Object[]> getListaObjetos() {
        return listaObjetos;
    }

    public void setListaObjetos(List<Object[]> listaObjetos) {
        this.listaObjetos = listaObjetos;
    }

}
