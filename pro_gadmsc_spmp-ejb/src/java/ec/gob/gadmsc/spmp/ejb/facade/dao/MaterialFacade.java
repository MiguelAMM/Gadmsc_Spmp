/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.facade.dao;

import ec.gob.gadmsc.spmp.ejb.entidades.Material;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.gob.gadmsc.spmp.servicios.MaterialServicio;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author MiguelAngel
 */
@Stateless
public class MaterialFacade extends AbstractFacade<Material> implements MaterialServicio {

    @PersistenceContext(unitName = "pro_gadmsc_spmp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MaterialFacade() {
        super(Material.class);
    }

    @Override
    public List<Material> findAll() {
        Query query = em.createQuery("Select m FROM Material m");
        List<Material> listaMaterial = query.getResultList();
        return listaMaterial;
    }

}
