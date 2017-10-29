/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.jsf.modulos;

import ec.gob.gadmsc.spmp.ejb.entidades.Equipo;
import ec.gob.gadmsc.spmp.ejb.entidades.EquipoFecha;
import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import ec.gob.gadmsc.spmp.jsf.base.BaseControlador;
import ec.gob.gadmsc.spmp.jsf.base.NavegacionControlador;
import ec.gob.gadmsc.spmp.servicios.EquipoFechaServicio;
import ec.gob.gadmsc.spmp.servicios.EquipoServicio;
import ec.gob.gadmsc.spmp.servicios.FechaTransporteServicio;
import ec.gob.gadmsc.spmp.tools.Login;
import ec.gob.gadmsc.spmp.tools.ManejoFechas;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author MiguelAngel
 */
@ManagedBean
@ViewScoped
public class FrmSpmpControlador {

    private List<Object[]> listaEquiposIngreso;
    private List<EquipoFecha> listaEquiposFecha;
    private FechaTransporte fechaTrans;
    private EquipoFecha equipoFecha;
    private Equipo equipo;
    private Login login;
    private final ManejoFechas manejoFechas;
    private String fechaFormateada;
    private String tipoEquipo;
    private boolean ingreso;
    private boolean actualiza;

    @EJB
    private FechaTransporteServicio fechaTransServicio;

    @EJB
    private EquipoServicio equipoServicio;

    @EJB
    private EquipoFechaServicio eqFechaServicio;

    @ManagedProperty("#{baseControlador}")
    private BaseControlador baseControlador;

    @ManagedProperty("#{navegacionControlador}")
    private NavegacionControlador navega;

    public FrmSpmpControlador() {
        ingreso = false;
        actualiza = true;
        Calendar cal = Calendar.getInstance();
        manejoFechas = new ManejoFechas();
        equipoFecha = new EquipoFecha();
    }

    @PostConstruct
    public void init() {
        fechaTrans = navega.getTransFecha();
        fechaFormateada = manejoFechas.formatearFecha(fechaTrans);
        equipo = navega.getLoginUsuario().getEquipo();
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
    }

    public void guardar() {
        equipoFecha.setFkFechaTrCodigo(fechaTrans);
        equipoFecha.setFkEqCodigo(equipo);
        eqFechaServicio.create(equipoFecha);
        baseControlador.addSuccessMessage("Ingreso exitoso");
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
        equipoFecha = new EquipoFecha();
    }

    public void eliminar() {
        eqFechaServicio.remove(equipoFecha);
//        RequestContext.getCurrentInstance().execute("PF('dialogo').hide()");
        baseControlador.addSuccessMessage("Equipo eliminado");
        equipoFecha = new EquipoFecha();
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
    }

    public void actualizar() {
        eqFechaServicio.edit(equipoFecha);
        baseControlador.addSuccessMessage("Actualización exitosa");
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
        equipoFecha = new EquipoFecha();
    }

    public void seleccionar() {
        ingreso = true;
        actualiza = false;
    }

    public FechaTransporte getFecha() {
        return fechaTrans;
    }

    public void setFecha(FechaTransporte fecha) {
        this.fechaTrans = fecha;
    }

    public EquipoFecha getEquipoFecha() {
        return equipoFecha;
    }

    public void setEquipoFecha(EquipoFecha equipoFecha) {
        this.equipoFecha = equipoFecha;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }

    public boolean isIngreso() {
        return ingreso;
    }

    public void setIngreso(boolean ingreso) {
        this.ingreso = ingreso;
    }

    public boolean isActualiza() {
        return actualiza;
    }

    public void setActualiza(boolean actualiza) {
        this.actualiza = actualiza;
    }

    public NavegacionControlador getNavega() {
        return navega;
    }

    public void setNavega(NavegacionControlador navega) {
        this.navega = navega;
    }

    public BaseControlador getBaseControlador() {
        return baseControlador;
    }

    public void setBaseControlador(BaseControlador baseControlador) {
        this.baseControlador = baseControlador;
    }

    public List<Object[]> getListaEquiposIngreso() {
        return listaEquiposIngreso;
    }

    public void setListaEquiposIngreso(List<Object[]> listaEquiposIngreso) {
        this.listaEquiposIngreso = listaEquiposIngreso;
    }

    public List<EquipoFecha> getListaEquiposFecha() {
        return listaEquiposFecha;
    }

    public void setListaEquiposFecha(List<EquipoFecha> listaEquiposFecha) {
        this.listaEquiposFecha = listaEquiposFecha;
    }

}
