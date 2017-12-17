/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.facade.dao;

import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.gob.gadmsc.spmp.servicios.FechaTransporteServicio;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Josue
 */
@Stateless
public class FechaTransporteFacade extends AbstractFacade<FechaTransporte> implements FechaTransporteServicio {

    @PersistenceContext(unitName = "pro_gadmsc_spmp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FechaTransporteFacade() {
        super(FechaTransporte.class);
    }

    @Override
    public List<FechaTransporte> findAll() {
        Query query = em.createQuery("Select f from FechaTransporte f");
        List<FechaTransporte> listaFecha = query.getResultList();
        return listaFecha;
    }

    @Override
    public List<String> listarFechasRango(Integer rangoDia1, Integer rangoMes1, Integer rangoAnio1,
            Integer rangoDia2, Integer rangoMes2, Integer rangoAnio2) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT CONCAT(fecha_tr_dia, '/', fecha_tr_mes, '/', fecha_tr_anio) as fecha from fecha_transporte ");
        consulta.append("WHERE fecha_tr_codigo BETWEEN (SELECT fecha_tr_codigo from fecha_transporte WHERE fecha_tr_dia = :rangoDia1 AND fecha_tr_mes = :rangoMes1 AND fecha_tr_anio = :rangoAnio1) ");
        consulta.append("AND (SELECT fecha_tr_codigo from fecha_transporte WHERE fecha_tr_dia = :rangoDia2 AND fecha_tr_mes = :rangoMes2 AND fecha_tr_anio = :rangoAnio2) ");
        Query query = em.createNativeQuery(consulta.toString());
        query.setParameter("rangoDia1", rangoDia1);
        query.setParameter("rangoMes1", rangoMes1);
        query.setParameter("rangoAnio1", rangoAnio1);
        query.setParameter("rangoDia2", rangoDia2);
        query.setParameter("rangoMes2", rangoMes2);
        query.setParameter("rangoAnio2", rangoAnio2);
        return query.getResultList();
    }

    @Override
    public List<Integer> listarAnios() {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT DISTINCT fecha_tr_anio FROM fecha_transporte ");
        consulta.append("ORDER BY fecha_tr_anio");
        Query query = em.createNativeQuery(consulta.toString());
        return query.getResultList();
    }

    @Override
    public void create(FechaTransporte fechaTransporte) {
        em.persist(fechaTransporte);
    }

    @Override
    public FechaTransporte buscarFecha(Integer dia, Integer mes, Integer anio) {
        FechaTransporte fecha;
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT f FROM FechaTransporte f ");
        consulta.append("WHERE fechaTrDia = :dia AND fechaTrMes = :mes AND fechaTrAnio = :anio");
        Query query = em.createQuery(consulta.toString());
        query.setParameter("dia", dia);
        query.setParameter("mes", mes);
        query.setParameter("anio", anio);
        try {
            fecha = (FechaTransporte) query.getSingleResult();
        } catch (NoResultException e) {
            fecha = null;
        }

        return fecha;
    }

}
