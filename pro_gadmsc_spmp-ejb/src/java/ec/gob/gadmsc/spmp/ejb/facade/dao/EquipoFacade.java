/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.facade.dao;

import ec.gob.gadmsc.spmp.ejb.entidades.Equipo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.gob.gadmsc.spmp.servicios.EquipoServicio;
import javax.persistence.Query;

/**
 *
 * @author MiguelAngel
 */
@Stateless
public class EquipoFacade extends AbstractFacade<Equipo> implements EquipoServicio {

    @PersistenceContext(unitName = "pro_gadmsc_spmp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EquipoFacade() {
        super(Equipo.class);
    }

    @Override
    public Equipo buscarPorTipo(String tipoEquipo) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT e FROM Equipo e ");
        consulta.append("WHERE e.eqTipo = :tipoEquipo");
        Query query = em.createQuery(consulta.toString());
        query.setParameter("tipoEquipo", tipoEquipo);
        return (Equipo) query.getSingleResult();
    }
}
