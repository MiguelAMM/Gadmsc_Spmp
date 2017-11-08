/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import ec.gob.gadmsc.spmp.jsf.base.NavegacionControlador;
        
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
 * @author desoeco
 */
public class LoginFilter implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        // Obtengo el bean que representa el usuario desde el scope sesión
        NavegacionControlador loginBean = (NavegacionControlador) req.getSession().getAttribute("navegacionControlador");
        
        //Proceso la URL que está requiriendo el cliente
        String urlStr = req.getRequestURL().toString().toLowerCase();
        boolean noProteger = noProteger(urlStr);
        //System.out.println(urlStr + " - desprotegido=[" + noProteger + "]");

        //Si no requiere protección continúo normalmente.
        if (noProteger(urlStr)) {
            chain.doFilter(request, response);
            return;
        }

        //El usuario no está logueado
        if (loginBean == null || !loginBean.isLogueado()) {
            res.sendRedirect(req.getContextPath() + "/index.xhtml");
            return;
        }

        //El recurso requiere protección, pero el usuario ya está logueado.
        chain.doFilter(request, response);
    }

    private boolean noProteger(String urlStr) {
        /*
		 * Este es un buen lugar para colocar y programar todos los patrones que
		 * creamos convenientes para determinar cuales de los recursos no
		 * requieren protección. Sin duda que habría que crear un mecanismo tal
		 * que se obtengan de un archivo de configuración o algo que no requiera
		 * compilación.
         */
        if (urlStr.contains("/index.xhtml")) {
            return true;
        }
        if (urlStr.contains("/javax.faces.resource/")) {
            return true;
        }
        if (urlStr.contains("/css/")) {
            return true;
        }
        if (urlStr.contains("/imagenes/")) {
            return true;
        }
        return urlStr.contains("/js/");
    }

    /**
     * Return the filter configuration object for this filter.
     */
    /*public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }*/
    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    /*public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }*/
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

    /**
     * Return a String representation of this object.
     *
     * @param msg
     */
    /*@Override
    public String toString() {
        if (filterConfig == null) {
            return ("LoginFilter()");
        }
        StringBuffer sb = new StringBuffer("LoginFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }*/
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
