/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.facade.dao;

import ec.gob.gadmsc.spmp.ejb.entidades.Chofer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.gob.gadmsc.spmp.servicios.ChoferServicio;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author MiguelAngel
 */
@Stateless
public class ChoferFacade extends AbstractFacade<Chofer> implements ChoferServicio {

    @PersistenceContext(unitName = "pro_gadmsc_spmp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChoferFacade() {
        super(Chofer.class);
    }

    @Override
    public List<Chofer> findAll() {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT c FROM Chofer c ORDER BY choferNombre, choferApellido");
        Query query = em.createQuery(consulta.toString());
        List<Chofer> listaChoferes = query.getResultList();
        return listaChoferes;
    }

    @Override
    public List<Chofer> buscarChoferNoAsignado() {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT c FROM Chofer c WHERE choferAsignado NOT IN ('SI') ORDER BY choferNombre, choferApellido");
        Query query = em.createQuery(consulta.toString());
        List<Chofer> listaChoferes = query.getResultList();
        return listaChoferes;
    }

}
