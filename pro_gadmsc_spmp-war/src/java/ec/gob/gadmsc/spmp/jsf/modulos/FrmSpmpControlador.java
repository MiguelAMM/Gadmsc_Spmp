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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
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
    private VolquetaFecha volquetaFecha, volquetaFechaEditar;
    private CargaTransportada cargaTransportada;
    private List<Material> listaMaterial;
    private List<Object[]> listaVolquetaCargaIngreso;
    private List<Materiales> materiales;
    private Integer combustible;
    private Integer km;
    private Time horaEntrada;
    private Time horaSalida;
    private boolean disableIngresoVolq;
    private String edita;
    private boolean disableVolq;

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
        activarIngreso();
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
        baseControlador.addSuccessMessage("Ingreso exitoso", null);
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
        equipoFecha = new EquipoFecha();
    }

    public void traerListaViajesIngresados() {
        try {
            if(baseControlador.usuarioActual.getTipoUsuario().equals("volqueta")){
            listaVolquetaCargaIngreso = volquetaFechaServicio.listarVolquetaCargaIngreso(fechaTrans.getFechaTrCodigo(), navegacionControlador.getLoginUsuario().getU().getUsuCodigo());
            //listaVolquetaCargaIngreso = volquetaFechaServicio.listarVolquetaCargaIngreso(1551, 4);

            materiales = new ArrayList<>();
            for (Material materialN : listaMaterial) {
                Materiales mat = new Materiales(materialN.getMatNombre());

                for (Object[] viaje : listaVolquetaCargaIngreso) {
                    if (materialN.getMatNombre().equals(viaje[3].toString())) {
                        mat.getViajes().add(new Viajes(Integer.valueOf(viaje[0].toString()), Integer.valueOf(viaje[1].toString()),
                                Integer.valueOf(viaje[2].toString()), viaje[4].toString(), viaje[5].toString(),
                                Integer.valueOf(viaje[6].toString()), Integer.valueOf(viaje[7].toString()),
                                Time.valueOf(viaje[8].toString()), Time.valueOf(viaje[9].toString())));
                    }
                }
                if (!mat.getViajes().isEmpty()) {
                    materiales.add(mat);
                }
            }
            if (!listaVolquetaCargaIngreso.isEmpty()) {
                /*combustible = materiales.get(0).getViajes().get(0).getCombustible();
                km = materiales.get(0).getViajes().get(0).getKm();
                horaEntrada = materiales.get(0).getViajes().get(0).getHoraEntrada();
                horaSalida = materiales.get(0).getViajes().get(0).getHoraSalida();*/
                volquetaFechaEditar = volquetaFechaServicio.find(materiales.get(0).getViajes().get(0).getVolquetaCodigo());
            }
            //System.err.println("size1: " + listaVolquetaCargaIngreso.size());
            if (listaVolquetaCargaIngreso.size() >= 1) {
                disableIngresoVolq = true;
            } else {
                disableIngresoVolq = false;
            }

            volquetaFecha = new VolquetaFecha();
            cargaTransportada = new CargaTransportada();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void guardarVolq() {
        try {
            //System.err.println("size2: " + listaVolquetaCargaIngreso.size());
            activarIngreso();
            if (listaVolquetaCargaIngreso.size() < 1) {
                if(volquetaFecha.getVolqHoraE()!=null || volquetaFecha.getVolqHoraS()!=null){
                volquetaFecha.setFkFechaTrCodigo(fechaTrans);
                volquetaFecha.setFkUsuCodigo(navegacionControlador.getLoginUsuario().getU());
                volquetaFechaServicio.create(volquetaFecha);
                } else{
                    baseControlador.addErrorMessage("Ingrese la hora de entrada y salida.", null);
                }
            }

            //if (listaVolquetaCargaIngreso.size() <= 5) {
            if (!listaVolquetaCargaIngreso.isEmpty()) {
                VolquetaFecha volquetaFechaRecup = volquetaFechaServicio.find(Integer.valueOf(listaVolquetaCargaIngreso.get(0)[6].toString()));
                cargaTransportada.setFkVolqFechaCodigo(volquetaFechaRecup);
            } else {
                cargaTransportada.setFkVolqFechaCodigo(volquetaFecha);
            }
            if(cargaTransportada.getCargaTrComprobante() == null){
                cargaTransportada.setCargaTrComprobante("0");
            }
            cargaTransportadaServicio.create(cargaTransportada);
            traerListaViajesIngresados();

            baseControlador.addSuccessMessage("Ingreso exitoso",null);
            //}
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void recuperarCargaLista(Viajes carga) {
        try {
            if (carga.getCargaCodigo() != null) {
                cargaTransportada = cargaTransportadaServicio.find(carga.getCargaCodigo());
                ingreso = true;
                actualiza = false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void eliminarCargaLista(Viajes carga) {
        try {
            cargaTransportada = cargaTransportadaServicio.find(carga.getCargaCodigo());
            cargaTransportadaServicio.remove(cargaTransportada);
            
            if(listaVolquetaCargaIngreso.size() ==1){
                volquetaFecha=volquetaFechaServicio.find(Integer.valueOf(listaVolquetaCargaIngreso.get(0)[6].toString()));
                volquetaFechaServicio.remove(volquetaFecha);
                volquetaFechaEditar = null;
                baseControlador.redirect(baseControlador.getContextName() + "/paginas/ingresos/formulario_volqueta.xhtml");
            }
            traerListaViajesIngresados();
            baseControlador.addSuccessMessage("Viaje eliminado",null);
            //baseControlador.redirect(baseControlador.getContextName() + "/paginas/ingresos/formulario_volqueta.xhtml");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarPnlEditar() {
        try {
            switch (edita) {
                case "v":
                    volquetaFechaServicio.edit(volquetaFechaEditar);
                    baseControlador.addSuccessMessage("Registro actualizado",null);
                    disableIngresoVolq = true;
                    break;
                case "c":
                    cargaTransportadaServicio.edit(cargaTransportada);
                    baseControlador.addSuccessMessage("Actualización exitosa",null);
                    break;
            }
            traerListaViajesIngresados();
            activarIngreso();
            disableVolq = false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void recuperaVolquetaLista() {
        try {
            volquetaFecha = volquetaFechaEditar;
            disableIngresoVolq = false;
            disableVolq = true;
            ingreso = true;
            actualiza = false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void eliminar() {
        eqFechaServicio.remove(equipoFecha);
        baseControlador.addSuccessMessage("Equipo eliminado", null);
        equipoFecha = new EquipoFecha();
        listaEquiposFecha = eqFechaServicio.listarEqFecha(equipo.getEqTipo(), fechaTrans.getFechaTrDia(),
                fechaTrans.getFechaTrMes(), fechaTrans.getFechaTrAnio());
        ingreso = false;
        RequestContext.getCurrentInstance().execute("PF('dialogo').hide()");
    }

    public void actualizar() {
        eqFechaServicio.edit(equipoFecha);
        baseControlador.addSuccessMessage("Actualización exitosa", null);
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
        baseControlador.addSuccessMessage("Ingreso exitoso", null);
        listaMaterial = materialServicio.findAll();
        material = new Material();
        RequestContext.getCurrentInstance().execute("PF('materialDialogo').hide()");
    }

    public void actualizarMaterial() {
        materialServicio.edit(material);
        baseControlador.addSuccessMessage("Actualización exitosa", null);
        listaMaterial = materialServicio.findAll();
        material = new Material();
        RequestContext.getCurrentInstance().execute("PF('materialDialogo').hide()");
    }

    public String parametro() {
        FacesContext fc = FacesContext.getCurrentInstance();
        this.edita = conseguirParametro(fc);
        return edita;
    }

    public String conseguirParametro(FacesContext fc) {
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        return params.get("edita");
    }

    public void activarIngreso() {
        ingreso = false;
        actualiza = true;
        disableVolq = false;
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

    public Time getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Time horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Time getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Time horaSalida) {
        this.horaSalida = horaSalida;
    }

    public VolquetaFecha getVolquetaFechaEditar() {
        return volquetaFechaEditar;
    }

    public void setVolquetaFechaEditar(VolquetaFecha volquetaFechaEditar) {
        this.volquetaFechaEditar = volquetaFechaEditar;
    }

    public boolean isDisableVolq() {
        return disableVolq;
    }

    public void setDisableVolq(boolean disableVolq) {
        this.disableVolq = disableVolq;
    }

}
