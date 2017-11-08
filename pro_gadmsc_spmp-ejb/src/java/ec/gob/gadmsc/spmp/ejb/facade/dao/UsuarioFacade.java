/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.facade.dao;

import ec.gob.gadmsc.spmp.ejb.entidades.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.gob.gadmsc.spmp.servicios.UsuarioServicio;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author MiguelAngel
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioServicio {

    @PersistenceContext(unitName = "pro_gadmsc_spmp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public List<Usuario> buscarVolquetas() {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT u FROM Usuario u WHERE usuCodigo NOT IN(200) ORDER BY usuCodigo");
        Query query = em.createQuery(consulta.toString());
        List<Usuario> listaUsuarios = query.getResultList();
        return listaUsuarios;
    }

    @Override
    public List<Usuario> findAll() {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT u FROM Usuario u ORDER BY usuCodigo");
        Query query = em.createQuery(consulta.toString());
        List<Usuario> listaUsuarios = query.getResultList();
        return listaUsuarios;
    }
}
