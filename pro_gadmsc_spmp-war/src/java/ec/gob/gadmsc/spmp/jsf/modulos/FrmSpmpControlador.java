/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.jsf.modulos;

import ec.gob.gadmsc.spmp.ejb.entidades.Equipo;
import ec.gob.gadmsc.spmp.ejb.entidades.EquipoFecha;
import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import ec.gob.gadmsc.spmp.ejb.entidades.VolquetaFecha;
import ec.gob.gadmsc.spmp.ejb.entidades.CargaTransportada;
import ec.gob.gadmsc.spmp.ejb.entidades.Material;
import ec.gob.gadmsc.spmp.jsf.base.BaseControlador;
import ec.gob.gadmsc.spmp.jsf.base.NavegacionControlador;
import ec.gob.gadmsc.spmp.servicios.EquipoFechaServicio;
import ec.gob.gadmsc.spmp.servicios.EquipoServicio;
import ec.gob.gadmsc.spmp.servicios.FechaTransporteServicio;
import ec.gob.gadmsc.spmp.servicios.VolquetaFechaServicio;
import ec.gob.gadmsc.spmp.servicios.CargaTransportadaServicio;
import ec.gob.gadmsc.spmp.servicios.MaterialServicio;
import ec.gob.gadmsc.spmp.tools.ManejoFechas;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
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
    private final ManejoFechas manejoFechas;
    private String fechaFormateada;
    private String tipoEquipo;
    private boolean ingreso;
    private boolean actualiza;
    private VolquetaFecha volquetaFecha;
    private CargaTransportada cargaTransportada;
    private List<Material> listaMaterial;
    private List<Object[]> listaVolquetaCargaIngreso;

    @EJB
    private FechaTransporteServicio fechaTransServicio;

    @EJB
    private EquipoServicio equipoServicio;

    @EJB
    private EquipoFechaServicio eqFechaServicio;

    @EJB
    private VolquetaFechaServicio volquetaFechaServicio;

    @EJB
    private CargaTransportadaServicio cargaTransportadaServicio;
    @EJB
    private MaterialServicio materialServicio;

    @ManagedProperty("#{baseControlador}")
    private BaseControlador baseControlador;

    @ManagedProperty("#{navegacionControlador}")
    private NavegacionControlador navegacionControlador;

    public FrmSpmpControlador() {
        ingreso = false;
        actualiza = true;
        Calendar cal = Calendar.getInstance();
        manejoFechas = new ManejoFechas();
        equipoFecha = new EquipoFecha();
        equipo = new Equipo();
        volquetaFecha = new VolquetaFecha();
        cargaTransportada = new CargaTransportada();
    }

    @PostConstruct
    public void init() {
        fechaTrans = navegacionControlador.getTransFecha();
        fechaFormateada = manejoFechas.formatearFecha(fechaTrans);
        equipo = navegacionControlador.getLoginUsuario().getEquipo();
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
        tipoEquipo = navegacionControlador.getLoginUsuario().getUsuario();
        listaEquiposIngreso = eqFechaServicio.listarEquipoTransporte(fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
        listaMaterial = materialServicio.findAll();
        if (!listaEquiposFecha.isEmpty()) {
            ingreso = true;
        }
    }

    public void guardar() {
        ingreso = true;
        actualiza = true;
        equipoFecha.setFkFechaTrCodigo(fechaTrans);
        equipoFecha.setFkEqCodigo(equipo);
        eqFechaServicio.create(equipoFecha);
        baseControlador.addSuccessMessage("Ingreso exitoso");
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
        equipoFecha = new EquipoFecha();
    }

    public void guardarVolq() {
        int contadorIngresosVolq = 0;
        baseControlador.contadorIngresos++;
        if (contadorIngresosVolq == 0) {
            baseControlador.disableIngresoVolq = true;
        }
        if (baseControlador.contadorIngresos == 5) {
            baseControlador.ingresarVolq = true;
        }
        volquetaFecha.setFkFechaTrCodigo(fechaTrans);
        volquetaFecha.setFkUsuCodigo(navegacionControlador.getLoginUsuario().getU());
        volquetaFechaServicio.create(volquetaFecha);

        cargaTransportada.setFkVolqFechaCodigo(volquetaFecha);
        cargaTransportadaServicio.create(cargaTransportada);

        listaVolquetaCargaIngreso = volquetaFechaServicio.listarVolquetaCargaIngreso(fechaTrans.getFechaTrCodigo(), navegacionControlador.getLoginUsuario().getU().getUsuCodigo());
        //  listaVolquetaCargaIngreso = volquetaFechaServicio.listarVolquetaCargaIngreso(1738, 1);

        baseControlador.addSuccessMessage("Ingreso exitoso");
        volquetaFecha = new VolquetaFecha();
        cargaTransportada = new CargaTransportada();

    }

    public void eliminar() {
        eqFechaServicio.remove(equipoFecha);
        RequestContext.getCurrentInstance().execute("PF('dialogo').hide()");
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
        ingreso = true;
        actualiza = true;
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

    public NavegacionControlador getNavegacionControlador() {
        return navegacionControlador;
    }

    public void setNavegacionControlador(NavegacionControlador navegacionControlador) {
        this.navegacionControlador = navegacionControlador;
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

    public VolquetaFecha getVolquetaFecha() {
        return volquetaFecha;
    }

    public void setVolquetaFecha(VolquetaFecha volquetaFecha) {
        this.volquetaFecha = volquetaFecha;
    }

    public CargaTransportada getCargaTransportada() {
        return cargaTransportada;
    }

    public void setCargaTransportada(CargaTransportada cargaTransportada) {
        this.cargaTransportada = cargaTransportada;
    }

    public List<Material> getListaMaterial() {
        return listaMaterial;
    }

    public void setListaMaterial(List<Material> listaMaterial) {
        this.listaMaterial = listaMaterial;
    }

    public List<Object[]> getListaVolquetaCargaIngreso() {
        return listaVolquetaCargaIngreso;
    }

    public void setListaVolquetaCargaIngreso(List<Object[]> listaVolquetaCargaIngreso) {
        this.listaVolquetaCargaIngreso = listaVolquetaCargaIngreso;
    }
}
