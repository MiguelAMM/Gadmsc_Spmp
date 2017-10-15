/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.jsf.modulos;

import ec.gob.gadmsc.spmp.ejb.entidades.EquipoFecha;
import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import ec.gob.gadmsc.spmp.ejb.entidades.Material;
import ec.gob.gadmsc.spmp.jsf.base.BaseControlador;
import ec.gob.gadmsc.spmp.jsf.base.NavegacionControlador;
import ec.gob.gadmsc.spmp.servicios.CargaTransportadaServicio;
import ec.gob.gadmsc.spmp.servicios.EquipoFechaServicio;
import ec.gob.gadmsc.spmp.servicios.FechaTransporteServicio;
import ec.gob.gadmsc.spmp.servicios.MaterialServicio;
import ec.gob.gadmsc.spmp.tools.AgrupaVolquetas;
import ec.gob.gadmsc.spmp.tools.FechaString;
import ec.gob.gadmsc.spmp.tools.ReporteException;
import static ec.gob.gadmsc.spmp.tools.Validaciones.*;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author desoeco
 */
@ManagedBean
@ViewScoped
public class ReporteControlador {

    //<editor-fold desc="Atributos-propiedades" defaultstate="collapsed">
    private static final Logger LOGGER = Logger.getLogger(ReporteControlador.class.getName());
    private List<Object[]> listaCargaTransportada;
    private List<Object[]> listaEquipoFecha;
    private List<FechaTransporte> listaFechasTransporte;
    private LinkedList<String> listaFechasString;
    private List<String> listaFechasRango;
    private List<AgrupaVolquetas> listaAgrupacionVolq;
    private List<Material> listaMateriales;
    private Date fechaDesde;
    private Date fechaHasta;
    private String maquinaria;
    private String horaActual;
    private final FechaString fechaCadena;
    //</editor-fold>

    //<editor-fold desc="Servicios" defaultstate="collapsed">
    @EJB
    private CargaTransportadaServicio cargaTransportadaServicio;
    @EJB
    private FechaTransporteServicio fechaTransporteServicio;
    @EJB
    private MaterialServicio materialServicio;
    @EJB
    private EquipoFechaServicio equiFechaServicio;

    @ManagedProperty("#{baseControlador}")
    private BaseControlador baseControlador;

    @ManagedProperty("#{navegacionControlador}")
    private NavegacionControlador navegacionControlador;
    //</editor-fold>  

    //<editor-fold desc="Constructor" defaultstate="collapsed">
    public ReporteControlador() {
        fechaCadena = new FechaString();
        listaFechasString = new LinkedList<>();
        System.out.println("Inicio Reporte controlador");
    }
    //</editor-fold>

    //<editor-fold desc="Post Constructor" defaultstate="collapsed">
    @PostConstruct
    public void inicializar() {
        listaFechasTransporte = fechaTransporteServicio.findAll();
        listaMateriales = materialServicio.findAll();
        listarFechas();
        System.out.println("PostInicio Reporte controlador");
    }
    //</editor-fold>

    //<editor-fold desc="Metodos" defaultstate="collapsed">
    public void obtenerCargaTransportada() {
        try {
//            Date fechaInicio = fechaCadena.convertToDate(fechaDesde);
//            Date fechaFin = fechaCadena.convertToDate(fechaHasta);
            fechaCadena.separarFecha(fechaDesde, fechaHasta);
            fechaCadena.comprobarFechas(fechaDesde, fechaHasta);
            listaFechasRango = fechaTransporteServicio.listarFechasRango(fechaCadena.getDiaInicio(), fechaCadena.getMesInicio(),
                    fechaCadena.getAnioInicio(), fechaCadena.getDiaFin(), fechaCadena.getMesFin(), fechaCadena.getAnioFin());
            validarOneRadio(maquinaria);
            if (maquinaria.equals("Volqueta")) {
                listaEquipoFecha = new ArrayList<>();
                listaCargaTransportada = cargaTransportadaServicio.listarCargaTransportada(fechaCadena.getDiaInicio(), fechaCadena.getMesInicio(),
                        fechaCadena.getAnioInicio(), fechaCadena.getDiaFin(), fechaCadena.getMesFin(), fechaCadena.getAnioFin());
//                listarAgrupacionVolq();
            } else {
                listaEquipoFecha = equiFechaServicio.listarEquipoTransporte(fechaCadena.getDiaInicio(), fechaCadena.getMesInicio(),
                        fechaCadena.getAnioInicio(), fechaCadena.getDiaFin(), fechaCadena.getMesFin(), fechaCadena.getAnioFin());
                listaCargaTransportada = new ArrayList<>();
            }
        } catch (ReporteException e) {
            baseControlador.addWarningMessage(e.getMessage());
            listaCargaTransportada = new ArrayList<>();
        } catch (NullPointerException np) {
            baseControlador.addWarningMessage("Seleccione un rango de fechas para iniciar la búsqueda");
        }

    }

    public void listarFechas() {
        for (FechaTransporte fechaTrans : listaFechasTransporte) {
            listaFechasString.add(fechaCadena.formatearFecha(fechaTrans));
        }
    }

    public void listarAgrupacionVolq() {
        listaAgrupacionVolq = new ArrayList<>();
        AgrupaVolquetas agVolq;
//        List<AgrupaVolquetas> listaAgrupaVolq = new ArrayList<>();
        for (String rangos : listaFechasRango) {
            System.out.println(rangos);
            agVolq = new AgrupaVolquetas(rangos);
            for (Object[] obj : listaCargaTransportada) {
                if (obj[7].equals(rangos)) {
                    agVolq.getListaObjetos().add(obj);
                }
            }
            listaAgrupacionVolq.add(agVolq);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get and Set" defaultstate="collapsed">
    public List<Object[]> getListaCargaTransportada() {
        return listaCargaTransportada;
    }

    public void setListaCargaTransportada(List<Object[]> listaCargaTransportada) {
        this.listaCargaTransportada = listaCargaTransportada;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public CargaTransportadaServicio getCargaTransportadaServicio() {
        return cargaTransportadaServicio;
    }

    public void setCargaTransportadaServicio(CargaTransportadaServicio cargaTransportadaServicio) {
        this.cargaTransportadaServicio = cargaTransportadaServicio;
    }

    public BaseControlador getBaseControlador() {
        return baseControlador;
    }

    public void setBaseControlador(BaseControlador baseControlador) {
        this.baseControlador = baseControlador;
    }

    public NavegacionControlador getNavegacionControlador() {
        return navegacionControlador;
    }

    public void setNavegacionControlador(NavegacionControlador navegacionControlador) {
        this.navegacionControlador = navegacionControlador;
    }

    public List<FechaTransporte> getListaFechasInicio() {
        return listaFechasTransporte;
    }

    public void setListaFechasInicio(List<FechaTransporte> listaFechasInicio) {
        this.listaFechasTransporte = listaFechasInicio;
    }

    public String getMaquinaria() {
        return maquinaria;
    }

    public void setMaquinaria(String maquinaria) {
        this.maquinaria = maquinaria;
    }

    public List<String> getListaFechasString() {
        return listaFechasString;
    }

    public void setListaFechasString(LinkedList<String> listaFechasString) {
        this.listaFechasString = listaFechasString;
    }

    public List<Material> getListaMateriales() {
        return listaMateriales;
    }

    public void setListaMateriales(List<Material> listaMateriales) {
        this.listaMateriales = listaMateriales;
    }

    public List<Object[]> getListaEquipoFecha() {
        return listaEquipoFecha;
    }

    public void setListaEquipoFecha(List<Object[]> listaEquipoFecha) {
        this.listaEquipoFecha = listaEquipoFecha;
    }

    public List<String> getListaFechasRango() {
        return listaFechasRango;
    }

    public void setListaFechasRango(List<String> listaFechasRango) {
        this.listaFechasRango = listaFechasRango;
    }

    public List<AgrupaVolquetas> getListaAgrupacionVolq() {
        return listaAgrupacionVolq;
    }

    public void setListaAgrupacionVolq(List<AgrupaVolquetas> listaAgrupacionVolq) {
        this.listaAgrupacionVolq = listaAgrupacionVolq;
    }

    public String getHoraActual() throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        horaActual = formatter.format(java.util.Calendar.getInstance().getTime());
//        return horaActual;
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int anio = cal.get(Calendar.YEAR);
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = cal.get(Calendar.MINUTE);
        horaActual = dia + "-" + mes + "-" + anio + "_" + hora + "-" + minuto;
        return horaActual;
    }

    public void setHoraActual(String horaActual) {
        this.horaActual = horaActual;
    }
    //</editor-fold>
}
