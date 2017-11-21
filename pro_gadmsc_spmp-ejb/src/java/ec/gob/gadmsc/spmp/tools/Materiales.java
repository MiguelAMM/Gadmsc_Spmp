/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author HOGAR
 */
public class Materiales implements Serializable {
 
    private String nombre;
     
    private List<Viajes> viajes;
     
    public Materiales() {
        viajes = new ArrayList<Viajes>();
    }
     
    public Materiales(String nombre) {
        this.nombre = nombre;
        viajes = new ArrayList<Viajes>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Viajes> getViajes() {
        return viajes;
    }

    public void setViajes(List<Viajes> viajes) {
        this.viajes = viajes;
    } 
    
}
