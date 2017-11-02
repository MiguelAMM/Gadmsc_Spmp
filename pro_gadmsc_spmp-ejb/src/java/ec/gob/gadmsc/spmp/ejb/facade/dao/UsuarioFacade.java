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
    public List<Usuario> findAll() {
        Query query = em.createQuery("Select u FROM Usuario u");
        List<Usuario> listaUsuarios = query.getResultList();
        return listaUsuarios;
    }

    @Override
    public boolean validarUsuario(Usuario user) {
        List<Usuario> usuarios = findAll();
        for (Usuario u : usuarios) {
            if (u.getUsuPass().equals(user.getUsuPass()) && u.getUsuNombre().equals(user.getUsuNombre())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Usuario> buscarVolquetas() {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT u FROM Usuario u WHERE usuCodigo NOT IN(14) ORDER BY usuCodigo");
        Query query = em.createQuery(consulta.toString());
        List<Usuario> listaUsuarios = query.getResultList();
        return listaUsuarios;
    }

}
