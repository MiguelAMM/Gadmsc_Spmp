/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.servicios;

import ec.gob.gadmsc.spmp.ejb.entidades.CargaTransportada;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author MiguelAngel
 */
@Local
public interface CargaTransportadaServicio {

    void create(CargaTransportada cargaTransportada);

    void edit(CargaTransportada cargaTransportada);

    void remove(CargaTransportada cargaTransportada);

    CargaTransportada find(Object id);

    List<CargaTransportada> findAll();

    List<CargaTransportada> findRange(int[] range);

    int count();
    
    List<Object[]> listarCargaTransportada(Integer dia, Integer mes, Integer anio, Integer dia2, Integer mes2, Integer anio2);
    
}
