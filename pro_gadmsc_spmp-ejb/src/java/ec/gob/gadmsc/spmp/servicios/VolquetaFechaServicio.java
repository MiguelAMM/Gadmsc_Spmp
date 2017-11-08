/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.servicios;

import ec.gob.gadmsc.spmp.ejb.entidades.VolquetaFecha;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author MiguelAngel
 */
@Local
public interface VolquetaFechaServicio {

    void create(VolquetaFecha volquetaFecha);

    void edit(VolquetaFecha volquetaFecha);

    void remove(VolquetaFecha volquetaFecha);

    VolquetaFecha find(Object id);

    List<VolquetaFecha> findAll();

    List<VolquetaFecha> findRange(int[] range);

    int count();

    List<Object[]> listarVolquetaCargaIngreso(Integer codigoFecha, Integer codigoUsuario);
}
