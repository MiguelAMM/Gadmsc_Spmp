/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.jsf.base;

import ec.gob.gadmsc.spmp.ejb.entidades.Equipo;
import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import ec.gob.gadmsc.spmp.ejb.entidades.Usuario;
import ec.gob.gadmsc.spmp.servicios.EquipoServicio;
import ec.gob.gadmsc.spmp.servicios.FechaTransporteServicio;
import ec.gob.gadmsc.spmp.servicios.UsuarioServicio;
import ec.gob.gadmsc.spmp.tools.Login;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author desoeco
 */
@ManagedBean
@SessionScoped
public class NavegacionControlador {

    private static final Logger LOGGER = Logger.getLogger(NavegacionControlador.class.getName());
    //Variables
    private String usuario;
    private String password;
    private MenuModel model;
    private Calendar Calendario = new GregorianCalendar();
    private String anioActual, mesActual, diaActual, fActual;
    private boolean activo;
    private String localeCode;
    private String idReporte;
    private boolean paginaPrincipal;
    private Date date1;
    private BarChartModel barModel;
//    private Usuario user;
    private Login loginUsuario;
    private FechaTransporte transFecha;

    @EJB
    private UsuarioServicio usuarioServicio;
    @EJB
    private EquipoServicio equipoServicio;
    @EJB
    private FechaTransporteServicio fechaTransorteServicio;

    @ManagedProperty("#{baseControlador}")
    private BaseControlador baseControlador;

    /**
     * Creates a new instance of NavegacionControlador
     */
    public NavegacionControlador() {
        System.out.println("Inicio Navegacion controlador");
//        user = new Usuario();
        loginUsuario = new Login();

        paginaPrincipal = true;
    }

    public Date obtenerFechaActual() {

        anioActual = Integer.toString(Calendario.get(Calendar.YEAR));
        if ((Calendario.get(Calendar.MONTH) + 1) < 10) {
            mesActual = "0" + Integer.toString((Calendario.get(Calendar.MONTH) + 1));
        } else {
            mesActual = Integer.toString((Calendario.get(Calendar.MONTH) + 1));
        }
        if (Calendario.get(Calendar.DAY_OF_MONTH) < 10) {
            diaActual = "0" + Integer.toString(Calendario.get(Calendar.DAY_OF_MONTH));
        } else {
            diaActual = Integer.toString(Calendario.get(Calendar.DAY_OF_MONTH));
        }

        fActual = anioActual + "-" + mesActual + "-" + diaActual;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(anioActual), Integer.parseInt(mesActual) - 1, Integer.parseInt(diaActual));

        baseControlador.fechaActual = calendar.getTime(); //System.err.println("Fecha Actual +++++ " + fechaActual);

        return baseControlador.fechaActual;

    }

    public void validarAccesoUsuario() throws IOException, Exception {
        try {
            obtenerFechaActual();
            List<Usuario> listaUsuarios = usuarioServicio.findAll();
            List<Equipo> listaEquipos = equipoServicio.findAll();
            transFecha = fechaTransorteServicio.buscarFecha(Integer.parseInt(diaActual), Integer.parseInt(mesActual), Integer.parseInt(anioActual));
            if (transFecha == null) {
                transFecha = new FechaTransporte(Integer.parseInt(diaActual), Integer.parseInt(mesActual), Integer.parseInt(anioActual));
                fechaTransorteServicio.create(transFecha);
            }
            boolean login = loginUsuario.validarUsuario(listaUsuarios, listaEquipos);
            //System.err.println("loginec:" + login);
            if (login) {

                baseControlador.setUsuarioActual(loginUsuario);
                baseControlador.addWarningMessage("Usuario logueado correctamente");
                //Menu que se mostrara en la pagina principal
                //cargarMenuDinamico(baseControlador.usuarioActual);
                //Ir a pagina principal
                //baseControlador.redirect(baseControlador.getContextName() + "/paginas/pagina_principal.xhtml");
//                baseControlador.redirect(baseControlador.getContextName() + "/paginas/reportes/reporte_carga_transportada.xhtml");
                switch (loginUsuario.getTipoUsuario()) {
                    case "secretaria":
                        baseControlador.redirect(baseControlador.getContextName() + "/paginas/reportes/reporte_equipos_chofer.xhtml");
                        break;
                    case "volqueta":
                        baseControlador.redirect(baseControlador.getContextName() + "/paginas/reportes/reporte_resumen.xhtml");
                        break;
                    case "equipo":
                        baseControlador.redirect(baseControlador.getContextName() + "/paginas/ingresos/ingreso_equipo.xhtml");
                        break;
                }

                //List<Usuario> listaUsuarios = usuarioServicio.listarPersonaPorUsuarioPassword(usuario, password);

                /*if (listaUsuarios.size() == 1) {
                    for (Usuario u : listaUsuarios) {
                        usuarioActual = u;                       
                    }*/
//                if (baseControlador.usuarioActual.getUsuNombreUsuario() != null && baseControlador.usuarioActual.getUsuCodigo() == 0) {
//                    if (baseControlador.usuarioActual.getUsuNombreUsuario() != null) {
//                        //usuarioActual.setUsuCodigo(9);
//                        usuarioServicio.edit(baseControlador.usuarioActual);
//                        baseControlador.getSession().setAttribute("usuario", baseControlador.usuarioActual);
//                        
//                        FacesContext context1 = FacesContext.getCurrentInstance();
//                        navegacionReportes(context1);
//                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", baseControlador.usuarioActual);
//
//                        baseControlador.getSession().setAttribute("periodo", baseControlador.periodoActualIpp);
//                        baseControlador.getSession().setAttribute("periodo2", baseControlador.periodoActualIpimIrh);
//                        baseControlador.getSession().setAttribute("fechaDiaria", baseControlador.fechaActual);
//                        baseControlador.codigoUsuario = baseControlador.usuarioActual.getUsuNombreUsuario();
//                        baseControlador.getSession().setAttribute("codigoUsuario", baseControlador.codigoUsuario);
//                        //Menu que se mostrara en la pagina principal
//                        cargarMenuDinamico(baseControlador.usuarioActual);
//                        // Ir a pagina principal
//                        baseControlador.redirect(baseControlador.getContextName() + "/paginas/pagina_principal.xhtml");
//                        //Carga auditoria
//                        //baseControlador.cargarAuditoriaUsuario(baseControlador.usuarioActual, "I", baseControlador.codigoUsuario);
//
//                    } else {
//                        baseControlador.addErrorMessage("Usuario", "o clave incorrecta.");
//
//                    }
//
//                } else {
//                    baseControlador.addErrorMessage("Estimado usuario", "Esta cuenta esta siendo usada por otro usuario");
//                }
//
//                /*} else {
//
//                    addErrorMessage("Estimado usuario", "El usuario o la contraseña son incorrectos. ");
//                }*/
            } else {
                baseControlador.addWarningMessage("El Usuario o la Contraseña son incorrectos");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cierra la sesion del usuario logueado CAB IPP IPI.
     */
//    public void reanudarSesion() {        
//        System.err.println("Entro reanudar ");   
//        try {                     
//                 
//                //  Borrar los datos de la sesion
//                limpiarAtributosSesion(); 
//                //this.getSession().invalidate();  ****se quito para pruebas
//                 redirect(getContextName() + "/index.xhtml");
//        } catch (Exception ex) {
//            Logger.getLogger(NavegacionControlador.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }        
    public void cerrarSesion() {

        try {
            baseControlador.usuarioActual = baseControlador.obtenerUsuarioAutenticado();

            if (baseControlador.usuarioActual != null) {
                //baseControlador.cargarAuditoriaUsuario(baseControlador.usuarioActual, "S", baseControlador.codigoUsuario);
                //usuarioActual.setUsuCodigo(0);
                //usuarioServicio.edit(usuarioActual);
                baseControlador.usuarioActual.setUsuario(null);
                baseControlador.getSession().invalidate();
                limpiarAtributosSesion();
                baseControlador.redirect(baseControlador.getContextName() + "/index.xhtml");
                //  Borrar los datos de la sesion

            }

        } catch (Exception ex) {
            Logger.getLogger(NavegacionControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el menu accesos para el usuario logueado.
     */
    private void cargarMenuDinamico(Usuario vusuario) {

        int cont1 = 0;
        int cont2 = 0;
        int cont3 = 0;
        try {
            setModel(new DefaultMenuModel());
            /*List<SegFuncionalidad> listaFuncionalidades = segFuncionalidadServicio.listarFuncionalidadesPorUsuarioyTipo(vusuario.getUsuNombreUsuario(), 4);
            List<SegFuncionalidad> listaFuncionalidadesInternas = segFuncionalidadServicio.listarFuncionalidadesPorUsuario(vusuario.getUsuNombreUsuario());

            setModel(new DefaultMenuModel());
            long startTime = System.currentTimeMillis();

            for (SegFuncionalidad menu : listaFuncionalidades) {
                cont1++;
                DefaultSubMenu subMenu = new DefaultSubMenu(menu.getNombreFun());

                //subMenu.setId("a" + cont1);
                List<SegFuncionalidad> listaFuncionalidadesHijos = segFuncionalidadServicio.listarFuncionalidadesPorPadre(menu.getIdFuncionalidad());

                for (SegFuncionalidad it : listaFuncionalidadesHijos) {
                    boolean b = false;
                    for (SegFuncionalidad per : listaFuncionalidadesInternas) {
                        if (it.equals(per)) {
                            b = true;
                        }
                    }
                    if (b) {

                        cont2++;
                        if (it.getTipoFun() == 5) {

                            DefaultSubMenu subSubMenu = new DefaultSubMenu(it.getNombreFun());
                            subSubMenu.setId("b" + cont2);

                            List<SegFuncionalidad> listaFuncionalidadesHijos2 = segFuncionalidadServicio.listarFuncionalidadesPorPadre(it.getIdFuncionalidad());

                            for (SegFuncionalidad hijo2 : listaFuncionalidadesHijos2) {
                                cont3++;
                                if (hijo2.getUrlFun() != null && hijo2.getUrlFun().trim().length() > 0) {
                                    try {
                                        DefaultMenuItem mItem = new DefaultMenuItem(hijo2.getNombreFun(), null, hijo2.getUrlFun());
                                        mItem.setId("c" + cont3);
                                        mItem.setAjax(true);                                          
                                        //mItem.setCommand("reportesAyuda([{name:'reporte',value:'" + hijo2.getVersionFun() + "'}])");
                                        mItem.setOnclick("reportesAyuda([{name:'reporte',value:'" + hijo2.getVersionFun() + "'}])");
                                        subSubMenu.addElement(mItem);
                                    } catch (Exception ex) {
                                        LOGGER.log(Level.SEVERE, null, ex);
                                    }
                                }

                            }
                            subMenu.addElement(subSubMenu);

                        } else if (it.getTipoFun() == 6) {

                            if (it.getUrlFun() != null && it.getUrlFun().trim().length() > 0) {
                                try {
                                    DefaultMenuItem mItem = new DefaultMenuItem(it.getNombreFun(), null, it.getUrlFun());
                                    mItem.setAjax(true);
                                    
                                    //mItem.setCommand("reportesAyuda([{name:'reporte',value:'" + it.getVersionFun() + "'}])");
                                    mItem.setOnclick("reportesAyuda([{name:'reporte',value:'" + it.getVersionFun() + "'}])");
                                    subMenu.addElement(mItem);
                                } catch (Exception ex) {
                                    LOGGER.log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }

                }
                getModel().addElement(subMenu);
            }*/

            DefaultMenuItem mItemAyuda = new DefaultMenuItem("Ayuda", "ui-icon-help");
            mItemAyuda.setCommand("#{navegacionControlador.irPantallaAyuda()}");
            getModel().addElement(mItemAyuda);
            long endTime1 = System.currentTimeMillis();
            DefaultMenuItem mItemSalir = new DefaultMenuItem("Salir", "ui-icon-power");
            mItemSalir.setCommand("#{navegacionControlador.cerrarSesion()}");
            getModel().addElement(mItemSalir);
            DefaultMenuItem mItemHome = new DefaultMenuItem("Inicio", "ui-icon-home");
            mItemHome.setCommand("#{navegacionControlador.irPantallaHome()}");
            getModel().addElement(mItemHome);
            long endTime2 = System.currentTimeMillis();

            //LOGGER.log(Level.INFO, "Total elapsed time in execution of method callMethod() is :");
            //LOGGER.log(Level.INFO, String.valueOf(endTime - startTime));
//            baseControlador.cargarComboPeriodoIndicador();
//            baseControlador.cargarComboActualizacion();
            createBarModel();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void navegacionReportes(FacesContext context) {
        //context = FacesContext.getCurrentInstance();           
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String reporte = (String) map.get("reporte");
        System.out.println("---->Pagina---" + reporte);
        idReporte = reporte;
    }

    /**
     * Va a la pantalla de home
     */
    public void irPantallaHome() {
        try {
            idReporte = "54_RGL";
            paginaPrincipal = true;
            baseControlador.redirect(baseControlador.getContextName() + "/paginas/pagina_principal.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(NavegacionControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Va a la pantalla de ayuda
     */
    public void irPantallaAyuda() {
        try {
            baseControlador.redirect(baseControlador.getContextName() + "/paginas/manual/descargas.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(NavegacionControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void limpiarAtributosSesion() {
        baseControlador.getSession().setAttribute("usuario", null);
    }

    /**
     * ***********************************************
     */
    private static Map<String, Object> countries;

    static {
        countries = new LinkedHashMap<String, Object>();

        countries.put("Español", Locale.ROOT); //label, value
        countries.put("English", Locale.ENGLISH);
    }

    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    //value change event listener
    public void countryLocaleCodeChanged(ValueChangeEvent e) {

        String newLocaleValue = e.getNewValue().toString();

        //loop country map to compare the locale code
        for (Map.Entry<String, Object> entry : countries.entrySet()) {

            if (entry.getValue().toString().equals(newLocaleValue)) {

                FacesContext.getCurrentInstance()
                        .getViewRoot().setLocale((Locale) entry.getValue());

            }
        }
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public void infoEmpresa(String numEmpresa) {

        try {
            //activaSession = "0";
            String uri = baseControlador.getRequest().getRequestURI();
            String nomPagina = uri.substring(21);
            baseControlador.getSession().setAttribute("numEmpresa", numEmpresa);
            baseControlador.getSession().setAttribute("nomPagina", nomPagina);
            //this.getSession().setAttribute("activaSession", activaSession);
            baseControlador.redirect(baseControlador.getContextName() + "/paginas/mantenimiento/info_empresa.xhtml");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void activaSession(String activaSession) {
        //activaSession = "1";
        baseControlador.getSession().setAttribute("activaSession", activaSession);
    }

    public void activarNuevo(String activaNuevo) {
        baseControlador.getSession().setAttribute("activaNuevo", activaNuevo);
    }

    private void createBarModel() {
        barModel = initBarModel();

        barModel.setTitle("Indice de Precios al Productor");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Carga");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Num emp");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries boys = new ChartSeries();
        boys.setLabel("Total");
        boys.set("2004", 120);
        boys.set("2005", 100);

        ChartSeries girls = new ChartSeries();
        girls.setLabel("Avance");
        girls.set("2004", 52);
        girls.set("2005", 60);

        model.addSeries(boys);
        model.addSeries(girls);

        return model;
    }

    //Getter and setter
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Login getUsuarioActual() {
        return baseControlador.usuarioActual;
    }

    public void setUsuarioActual(Login usuarioActual) {
        baseControlador.usuarioActual = usuarioActual;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public boolean isPaginaPrincipal() {
        return paginaPrincipal;
    }

    public void setPaginaPrincipal(boolean paginaPrincipal) {
        this.paginaPrincipal = paginaPrincipal;
    }

    public BaseControlador getBaseControlador() {
        return baseControlador;
    }

    public void setBaseControlador(BaseControlador baseControlador) {
        this.baseControlador = baseControlador;
    }

    public Login getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(Login loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public FechaTransporte getTransFecha() {
        return transFecha;
    }

    public void setTransFecha(FechaTransporte transFecha) {
        this.transFecha = transFecha;
    }

}
