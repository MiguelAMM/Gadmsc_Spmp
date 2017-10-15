/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.tools;

import ec.gob.gadmsc.spmp.servicios.CargaTransportadaServicio;
import ec.gob.gadmsc.spmp.tools.ReporteException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author MiguelAngel
 */
public class Validaciones {

    public static void validarOneRadio(String valorRadio) throws ReporteException {
        if (valorRadio == null) {
            throw new ReporteException("Seleccione el tipo de maquinaria para realizar la búsqueda");
        }
    }
}
