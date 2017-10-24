/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.facade.dao;

import ec.gob.gadmsc.spmp.ejb.entidades.CargaTransportada;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ec.gob.gadmsc.spmp.servicios.CargaTransportadaServicio;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author MiguelAngel
 */
@Stateless
public class CargaTransportadaFacade extends AbstractFacade<CargaTransportada> implements CargaTransportadaServicio {

    @PersistenceContext(unitName = "pro_gadmsc_spmp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CargaTransportadaFacade() {
        super(CargaTransportada.class);
    }

    @Override
    public List<Object[]> listarCargaTransportada(Integer rangoDia1, Integer rangoMes1, Integer rangoAnio1,
            Integer rangoDia2, Integer rangoMes2, Integer rangoAnio2) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("select volq.fk_usu_codigo, volq.volq_fecha_combustible, volq.volq_fecha_km, mat.mat_nombre, ");
        consulta.append("c.carga_tr_viaje, c.carga_tr_comprobante, c.carga_tr_observacion, CONCAT(volq.fecha_tr_dia, '/', volq.fecha_tr_mes, '/', volq.fecha_tr_anio) ");
        consulta.append("from (select b.volq_fecha_codigo, b.fk_usu_codigo, a.fecha_tr_dia, ");
        consulta.append("a.fecha_tr_mes, a.fecha_tr_anio, b.volq_fecha_combustible, b.volq_fecha_km ");
        consulta.append("from (select * from fecha_transporte ");
        consulta.append("where fecha_tr_codigo between (select fecha_tr_codigo from fecha_transporte ");
        consulta.append("where fecha_tr_dia =:rangoDia1 ");
        consulta.append("and fecha_tr_mes =:rangoMes1 ");
        consulta.append("and fecha_tr_anio =:rangoAnio1) and (select fecha_tr_codigo from fecha_transporte ");
        consulta.append("where fecha_tr_dia =:rangoDia2 ");
        consulta.append("and fecha_tr_mes =:rangoMes2 ");
        consulta.append("and fecha_tr_anio =:rangoAnio2)) a, volqueta_fecha b ");
        consulta.append("where a.fecha_tr_codigo = b.fk_fecha_tr_codigo ) volq, carga_transportada c, material mat ");
        consulta.append("where volq.volq_fecha_codigo = c.fk_volq_fecha_codigo and mat.mat_codigo = c.fk_mat_codigo");

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
    public List<Object[]> listarResumenCarga(int anio) {
        StringBuilder consulta = new StringBuilder();
        consulta.append("select x.*, y.tt from ");
        consulta.append("(select a.fecha_tr_mes, d.mat_nombre, CAST(sum(c.carga_tr_viaje) AS INTEGER) from fecha_transporte a, volqueta_fecha b, carga_transportada c, material d ");
        consulta.append("where a.fecha_tr_codigo = b.fk_fecha_tr_codigo ");
        consulta.append("and b.volq_fecha_codigo = c.fk_volq_fecha_codigo ");
        consulta.append("and c.fk_mat_codigo = d.mat_codigo ");
        consulta.append("and a.fecha_tr_anio = :anio ");
        consulta.append("group by a.fecha_tr_mes, d.mat_nombre ");
        consulta.append("order by a.fecha_tr_mes, d.mat_nombre) x, ");
        consulta.append("(select r.material, CAST(sum(r.nvolq) AS INTEGER) tt from (select a.fecha_tr_mes mes, d.mat_nombre material, sum(c.carga_tr_viaje) nvolq from fecha_transporte a, volqueta_fecha b, carga_transportada c, material d ");
        consulta.append("where a.fecha_tr_codigo = b.fk_fecha_tr_codigo ");
        consulta.append("and b.volq_fecha_codigo = c.fk_volq_fecha_codigo ");
        consulta.append("and c.fk_mat_codigo = d.mat_codigo ");
        consulta.append("and a.fecha_tr_anio = :anio ");
        consulta.append("group by a.fecha_tr_mes, d.mat_nombre ");
        consulta.append("order by a.fecha_tr_mes, d.mat_nombre) r ");
        consulta.append("group by r.material) y ");
        consulta.append("where x.mat_nombre = y.material ");
        consulta.append("order by x.fecha_tr_mes, x.mat_nombre");

        Query query = em.createNativeQuery(consulta.toString());
        query.setParameter("anio", anio);
        return query.getResultList();
    }

}
