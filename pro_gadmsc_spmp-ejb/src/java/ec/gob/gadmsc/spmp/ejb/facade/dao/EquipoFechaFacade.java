/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.facade.dao;

import ec.gob.gadmsc.spmp.ejb.entidades.EquipoFecha;
import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.gob.gadmsc.spmp.servicios.EquipoFechaServicio;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Josue
 */
@Stateless
public class EquipoFechaFacade extends AbstractFacade<EquipoFecha> implements EquipoFechaServicio {

    @PersistenceContext(unitName = "pro_gadmsc_spmp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EquipoFechaFacade() {
        super(EquipoFecha.class);
    }

    @Override
    public List<Object[]> listarEquipoTransporte(Integer dia, Integer mes, Integer anio, Integer dia2, Integer mes2, Integer anio2) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("select q.eq_tipo, b.eq_fecha_combustible, b.eq_fecha_observacion, CONCAT(a.fecha_tr_dia, '/', a.fecha_tr_mes, '/', a.fecha_tr_anio)  ");
        consulta.append("from (select * from fecha_transporte ");
        consulta.append("where fecha_tr_codigo between (select fecha_tr_codigo from fecha_transporte ");
        consulta.append("where fecha_tr_dia = :rangoDia1 ");
        consulta.append("and fecha_tr_mes = :rangoMes1 ");
        consulta.append("and fecha_tr_anio = :rangoAnio1) and (select fecha_tr_codigo from fecha_transporte ");
        consulta.append("where fecha_tr_dia = :rangoDia2 ");
        consulta.append("and fecha_tr_mes = :rangoMes2 ");
        consulta.append("and fecha_tr_anio = :rangoAnio2)) a, equipo_fecha b, equipo q ");
        consulta.append("where a.fecha_tr_codigo = b.fk_fecha_tr_codigo and q.eq_codigo = b.fk_eq_codigo ");

        Query query = em.createNativeQuery(consulta.toString());
        query.setParameter("rangoDia1", dia);
        query.setParameter("rangoMes1", mes);
        query.setParameter("rangoAnio1", anio);
        query.setParameter("rangoDia2", dia2);
        query.setParameter("rangoMes2", mes2);
        query.setParameter("rangoAnio2", anio2);
        return query.getResultList();
    }

    @Override
    public List<EquipoFecha> listarEqFecha(String tipoEq, Integer dia, Integer mes, Integer anio) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("SELECT ef FROM EquipoFecha ef, Equipo eq, FechaTransporte ft ");
        consulta.append("WHERE ft.fechaTrDia = :dia AND ft.fechaTrMes = :mes AND ft.fechaTrAnio = :anio AND eq.eqTipo = :tipoEq ");
        consulta.append("AND ef.fkEqCodigo = eq.eqCodigo AND ef.fkFechaTrCodigo = ft.fechaTrCodigo ORDER BY ef.eqFechaCodigo");

        Query query = em.createQuery(consulta.toString());
        query.setParameter("dia", dia);
        query.setParameter("mes", mes);
        query.setParameter("anio", anio);
        query.setParameter("tipoEq", tipoEq);
        return query.getResultList();
    }

    @Override
    public List<Object[]> listarEquipoTransporte(Integer numEquipo, Integer dia, Integer mes, Integer anio, Integer dia2, Integer mes2,
            Integer anio2) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("select q.eq_tipo, b.eq_fecha_combustible, b.eq_fecha_observacion, "
                + "CONCAT(a.fecha_tr_dia, '/', a.fecha_tr_mes, '/', a.fecha_tr_anio), b.eq_fecha_hora_e, b.eq_fecha_hora_s ");
        consulta.append("from (select * from fecha_transporte ");
        consulta.append("where fecha_tr_codigo between (select fecha_tr_codigo from fecha_transporte ");
        consulta.append("where fecha_tr_dia = :rangoDia1 ");
        consulta.append("and fecha_tr_mes = :rangoMes1 ");
        consulta.append("and fecha_tr_anio = :rangoAnio1) and (select fecha_tr_codigo from fecha_transporte ");
        consulta.append("where fecha_tr_dia = :rangoDia2 ");
        consulta.append("and fecha_tr_mes = :rangoMes2 ");
        consulta.append("and fecha_tr_anio = :rangoAnio2)) a, equipo_fecha b, equipo q ");
        consulta.append("where a.fecha_tr_codigo = b.fk_fecha_tr_codigo and q.eq_codigo = b.fk_eq_codigo ");
        consulta.append("and q.eq_codigo =:numEquipo");

        Query query = em.createNativeQuery(consulta.toString());
        query.setParameter("rangoDia1", dia);
        query.setParameter("rangoMes1", mes);
        query.setParameter("rangoAnio1", anio);
        query.setParameter("rangoDia2", dia2);
        query.setParameter("rangoMes2", mes2);
        query.setParameter("rangoAnio2", anio2);
        query.setParameter("numEquipo", numEquipo);
        return query.getResultList();
    }
}
