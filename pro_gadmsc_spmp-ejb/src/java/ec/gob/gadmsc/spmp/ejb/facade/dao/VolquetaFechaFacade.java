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
import java.util.List;
import javax.persistence.Query;

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

    @Override
    public List<Object[]> listarVolquetaCargaIngreso(Integer codigoFecha, Integer codigoUsuario) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.volq_fecha_combustible, ");
        sql.append("a.volq_fecha_km, ");
        sql.append("b.carga_tr_viaje, ");
        sql.append("c.mat_nombre, ");
        sql.append("b.carga_tr_comprobante, ");
        sql.append("b.carga_tr_observacion, ");
        sql.append("a.volq_fecha_codigo, ");
        sql.append("b.carga_tr_codigo  ");
        sql.append("from volqueta_fecha a, carga_transportada b, material c ");
        sql.append("where a.volq_fecha_codigo = b.fk_volq_fecha_codigo ");
        sql.append("and c.mat_codigo = b.fk_mat_codigo ");
        sql.append("and a.fk_fecha_tr_codigo = :codigoFecha ");
        sql.append("and a.fk_usu_codigo = :codigoUsuario ");
        sql.append("and b.carga_tr_observacion <> '' ");
        sql.append("order by c.mat_codigo ");

        Query q = em.createNativeQuery(sql.toString());
        q.setParameter("codigoFecha", codigoFecha);
        q.setParameter("codigoUsuario", codigoUsuario);
        return q.getResultList();
    }

    @Override
    public VolquetaFecha buscarByCodigo(Integer volqFechaCodigo) {
        String sql = "Select e  from VolquetaFecha  e where  e.volqFechaCodigo =:volqFechaCodigo ";
        Query q = em.createQuery(sql);
        q.setParameter("volqFechaCodigo", volqFechaCodigo);
        return (VolquetaFecha) q.getSingleResult();
    }
}
