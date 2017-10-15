/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.servicios;

import ec.gob.gadmsc.spmp.ejb.entidades.EquipoFecha;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author MiguelAngel
 */
@Local
public interface EquipoFechaServicio {

    void create(EquipoFecha equipoFecha);

    void edit(EquipoFecha equipoFecha);

    void remove(EquipoFecha equipoFecha);

    EquipoFecha find(Object id);

    List<EquipoFecha> findAll();

    List<EquipoFecha> findRange(int[] range);

    int count();
    
    List<Object[]> listarEquipoTransporte(Integer dia, Integer mes, Integer anio, Integer dia2, Integer mes2, Integer anio2);
    
}
