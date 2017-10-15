/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.facade.dao;

import ec.gob.gadmsc.spmp.ejb.entidades.VolquetaFecha;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.gob.gadmsc.spmp.servicios.VolquetaFechaServicio;

/**
 *
 * @author MiguelAngel
 */
@Stateless
public class VolquetaFechaFacade extends AbstractFacade<VolquetaFecha> implements VolquetaFechaServicio {

    @PersistenceContext(unitName = "pro_gadmsc_spmp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VolquetaFechaFacade() {
        super(VolquetaFecha.class);
    }
    
}
