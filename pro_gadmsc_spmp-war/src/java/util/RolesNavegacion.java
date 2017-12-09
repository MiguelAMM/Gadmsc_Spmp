/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import ec.gob.gadmsc.spmp.jsf.base.BaseControlador;
import ec.gob.gadmsc.spmp.jsf.base.NavegacionControlador;
import java.io.IOException;
import javax.faces.bean.ManagedProperty;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaraujo Clase que maneja la navegación entre las páginas de edición
 * de empresa(formulario_empresa), la página que muestra la información de la
 * empresa seleccionada(info_empresa) y la página que se enlaza con info_empresa
 * la cual es (reporte_cobertura_reemplazos)
 */
public class RolesNavegacion implements Filter {

    private static final boolean debug = true;
    private String uri1, uri2;
    private boolean flag;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        flag = false;
        String uri = req.getRequestURI();
        //System.err.println("uri " + uri);
        String ingresoVolqueta = "/pro_gadmsc_spmp-war/paginas/ingresos/formulario_volqueta.xhtml";
        String ingresoEquipo = "/pro_gadmsc_spmp-war/paginas/ingresos/ingreso_equipo.xhtml";
        String repoCarga = "/pro_gadmsc_spmp-war/paginas/reportes/reporte_carga_transportada.xhtml";
        String repoEquipo = "/pro_gadmsc_spmp-war/paginas/reportes/reporte_equipos_chofer.xhtml";
        String repoResumen = "/pro_gadmsc_spmp-war/paginas/reportes/reporte_resumen.xhtml";
        String manualAdmin = "/pro_gadmsc_spmp-war/paginas/manuales/ver_manual_admin.xhtml";
        String manualVolq = "/pro_gadmsc_spmp-war/paginas/manuales/ver_manual_volqueta.xhtml";
        String manualEquipo = "/pro_gadmsc_spmp-war/paginas/manuales/ver_manual_equipo.xhtml";
        
        NavegacionControlador loginBean = (NavegacionControlador) req.getSession().getAttribute("navegacionControlador");

        if(loginBean != null){
            switch (loginBean.getLoginUsuario().getTipoUsuario()) {
                case "secretaria":
                    if (uri.equals(repoCarga) || uri.equals(repoEquipo) || uri.equals(repoResumen) || uri.equals(manualAdmin)) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                    if (flag == true) {
                        res.sendRedirect(req.getContextPath() + "/paginas/reportes/reporte_equipos_chofer.xhtml");
                    }
                    break;
                case "volqueta":
                    if (uri.equals(ingresoVolqueta) || uri.equals(manualVolq)) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                    if (flag == true) {
                        res.sendRedirect(req.getContextPath() + "/paginas/ingresos/formulario_volqueta.xhtml");
                    }
                    break;
                case "equipo":
                    if (uri.equals(ingresoEquipo) || uri.equals(manualEquipo)) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                    if (flag == true) {
                        res.sendRedirect(req.getContextPath() + "/paginas/ingresos/ingreso_equipo.xhtml");
                    }
                    break;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("LoginFilter:Initializing filter");
            }
        }
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
