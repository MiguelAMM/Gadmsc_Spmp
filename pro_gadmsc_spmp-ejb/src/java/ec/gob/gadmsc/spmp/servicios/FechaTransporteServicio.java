/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.servicios;

import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author MiguelAngel
 */
@Local
public interface FechaTransporteServicio {

    void create(FechaTransporte fechaTransporte);

    void edit(FechaTransporte fechaTransporte);

    void remove(FechaTransporte fechaTransporte);

    FechaTransporte find(Object id);

    List<FechaTransporte> findAll();

    List<FechaTransporte> findRange(int[] range);
    
    List<String> listarFechasRango(Integer rangoDia1, Integer rangoMes1, Integer rangoAnio1,
            Integer rangoDia2, Integer rangoMes2, Integer rangoAnio2);

    int count();
    
    List<Integer> listarAnios();
    
}
