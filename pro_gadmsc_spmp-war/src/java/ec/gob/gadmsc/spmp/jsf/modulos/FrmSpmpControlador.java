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
import ec.gob.gadmsc.spmp.tools.Materiales;
import ec.gob.gadmsc.spmp.tools.Viajes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(FrmSpmpControlador.class.getName());
    private List<Object[]> listaEquiposIngreso;
    private List<EquipoFecha> listaEquiposFecha;
    private FechaTransporte fechaTrans;
    private EquipoFecha equipoFecha;
    private Equipo equipo;
    private Material material;
    private Viajes carga;
    private final ManejoFechas manejoFechas;
    private String fechaFormateada;
    private String tipoEquipo;
    private boolean ingreso;
    private boolean actualiza;
    private VolquetaFecha volquetaFecha;
    private CargaTransportada cargaTransportada;
    private List<Material> listaMaterial;
    private List<Object[]> listaVolquetaCargaIngreso;
    private List<Materiales> materiales;
    private Integer combustible;
    private Integer km;
    private boolean ingresarVolq;
    private boolean disableIngresoVolq;

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
        material = new Material();
        carga = new Viajes();
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
        traerListaViajesIngresados();
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

    public void traerListaViajesIngresados() {
        try {
            listaVolquetaCargaIngreso = volquetaFechaServicio.listarVolquetaCargaIngreso(fechaTrans.getFechaTrCodigo(), navegacionControlador.getLoginUsuario().getU().getUsuCodigo());
            //listaVolquetaCargaIngreso = volquetaFechaServicio.listarVolquetaCargaIngreso(1551, 4);

            materiales = new ArrayList<>();
            for (Material materialN : listaMaterial) {
                Materiales mat = new Materiales(materialN.getMatNombre());

                for (Object[] viaje : listaVolquetaCargaIngreso) {
                    if (materialN.getMatNombre().equals(viaje[3].toString())) {
                        mat.getViajes().add(new Viajes(Integer.valueOf(viaje[0].toString()), Integer.valueOf(viaje[1].toString()), Integer.valueOf(viaje[2].toString()), Integer.valueOf(viaje[4].toString()), viaje[5].toString(), Integer.valueOf(viaje[6].toString()), Integer.valueOf(viaje[7].toString())));
                    }
                }
                if (!mat.getViajes().isEmpty()) {
                    materiales.add(mat);
                }
            }
            if (!listaVolquetaCargaIngreso.isEmpty()) {
                combustible = materiales.get(0).getViajes().get(0).getCombustible();
                km = materiales.get(0).getViajes().get(0).getKm();
            }
            System.err.println("size1: " + listaVolquetaCargaIngreso.size());
            if (listaVolquetaCargaIngreso.size() >= 5) {
                ingresarVolq = true;
            }
            if (listaVolquetaCargaIngreso.size() >= 1) {
                disableIngresoVolq = true;
            }

            volquetaFecha = new VolquetaFecha();
            cargaTransportada = new CargaTransportada();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void guardarVolq() {
        try {
            System.err.println("size2: " + listaVolquetaCargaIngreso.size());
            if (listaVolquetaCargaIngreso.size() < 1) {
                volquetaFecha.setFkFechaTrCodigo(fechaTrans);
                volquetaFecha.setFkUsuCodigo(navegacionControlador.getLoginUsuario().getU());
                volquetaFechaServicio.create(volquetaFecha);
            }

            if (listaVolquetaCargaIngreso.size() <= 5) {
                if (!listaVolquetaCargaIngreso.isEmpty()) {
                    VolquetaFecha volquetaFechaRecup = volquetaFechaServicio.buscarByCodigo(Integer.valueOf(listaVolquetaCargaIngreso.get(0)[6].toString()));
                    cargaTransportada.setFkVolqFechaCodigo(volquetaFechaRecup);
                } else {
                    cargaTransportada.setFkVolqFechaCodigo(volquetaFecha);
                }
                cargaTransportadaServicio.create(cargaTransportada);
                traerListaViajesIngresados();

                baseControlador.addSuccessMessage("Ingreso exitoso");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void recuperarCargaLista(Viajes carga) {
        try {
            if (carga.getCargaCodigo() != null) {
                cargaTransportada = cargaTransportadaServicio.buscarCarga(carga.getCargaCodigo());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void eliminarCargaLista() {
        try {
            cargaTransportadaServicio.remove(cargaTransportada);
            baseControlador.addSuccessMessage("Viaje borrado exitosamente!!!");
            traerListaViajesIngresados();
            baseControlador.redirect(baseControlador.getContextName() + "/paginas/ingresos/formulario_volqueta.xhtml");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarCargaLista() {
        try {
            volquetaFecha = new VolquetaFecha();
            cargaTransportada = new CargaTransportada();
            VolquetaFecha volquetaFechaRecup = volquetaFechaServicio.buscarByCodigo(carga.getVolquetaCodigo());
            volquetaFechaRecup.setVolqFechaCombustible(carga.getCombustible());
            volquetaFechaRecup.setVolqFechaKm(carga.getKm());
            volquetaFechaServicio.edit(volquetaFechaRecup);

            CargaTransportada cargaRecup = cargaTransportadaServicio.buscarCarga(carga.getCargaCodigo());
            cargaRecup.setCargaTrComprobante(carga.getComprobante());
            cargaRecup.setCargaTrObservacion(carga.getObservacion());
            cargaRecup.setCargaTrViaje(carga.getCarga());
            cargaTransportadaServicio.edit(cargaRecup);

            baseControlador.addSuccessMessage("Viaje actualizado exitosamente!!!");
            traerListaViajesIngresados();
            baseControlador.redirect(baseControlador.getContextName() + "/paginas/ingresos/formulario_volqueta.xhtml");
            actualiza = true;
            ingreso = false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void eliminar() {
        eqFechaServicio.remove(equipoFecha);
        baseControlador.addSuccessMessage("Equipo eliminado");
        equipoFecha = new EquipoFecha();
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
        ingreso = false;
        RequestContext.getCurrentInstance().execute("PF('dialogo').hide()");
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
        System.out.println("Ingreso selecciona");
    }

    public void ingresarNuevoMaterial() {
        material = new Material();
        ingreso = false;
        actualiza = true;
    }

    public void seleccionarMaterial() {
        ingreso = true;
        actualiza = false;
    }

    public void ingresarMaterial() {
        materialServicio.create(material);
        baseControlador.addSuccessMessage("Ingreso exitoso");
        listaMaterial = materialServicio.findAll();
        material = new Material();
        RequestContext.getCurrentInstance().execute("PF('materialDialogo').hide()");
    }

    public void actualizarMaterial() {
        materialServicio.edit(material);
        baseControlador.addSuccessMessage("Actualización exitosa");
        listaMaterial = materialServicio.findAll();
        material = new Material();
        RequestContext.getCurrentInstance().execute("PF('materialDialogo').hide()");
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<Materiales> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Materiales> materiales) {
        this.materiales = materiales;
    }

    public Integer getCombustible() {
        return combustible;
    }

    public void setCombustible(Integer combustible) {
        this.combustible = combustible;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public boolean isIngresarVolq() {
        return ingresarVolq;
    }

    public void setIngresarVolq(boolean ingresarVolq) {
        this.ingresarVolq = ingresarVolq;
    }

    public boolean isDisableIngresoVolq() {
        return disableIngresoVolq;
    }

    public void setDisableIngresoVolq(boolean disableIngresoVolq) {
        this.disableIngresoVolq = disableIngresoVolq;
    }

    public Viajes getCarga() {
        return carga;
    }

    public void setCarga(Viajes carga) {
        this.carga = carga;
    }

}
