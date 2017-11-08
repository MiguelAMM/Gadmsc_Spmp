/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.jsf.base;

//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import ec.gob.gadmsc.spmp.ejb.entidades.Usuario;
import ec.gob.gadmsc.spmp.servicios.UsuarioServicio;
import ec.gob.gadmsc.spmp.tools.Login;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author desoeco
 */
@ManagedBean
@SessionScoped
@Startup
@Singleton
public class BaseControlador implements Serializable {

    /**
     * Creates a new instance of BaseControlador
     */
    private static final Logger LOGGER = Logger.getLogger(BaseControlador.class.getName());
    public Date fechaActual;

    public HttpSession session;
    public String codigoUsuario;

    public Login usuarioActual;
    public int contadorIngresos;
    public boolean disableIngresoVolq;
    public boolean ingresarVolq;

    //Servicios
    @EJB
    private UsuarioServicio usuarioServicio;

    public BaseControlador() {
        System.out.println("Inicio Base controlador");
    }

    @PostConstruct
    public void init() {
        System.out.println("Inicio Base post controlador");
        usuarioActual = obtenerUsuarioAutenticado();
        System.out.println("usuarioActual: " + usuarioActual);

    }

    /**
     * Se encarga de recuperar el nombre del contexto de la apliacacion web.
     *
     * @return Una cadena con el nombre del contexto
     */
    public final String getContextName() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    /**
     * Se encarga de ejecutar una redireccion.
     *
     * @param url url de destino
     * @throws IOException en caso de no poder hacer la redireccion
     */
    public void redirect(final String url) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

    /**
     * Se encarga de recuperar HttpServletRequest.
     *
     * @return El objeto HttpServletRequest encontrado
     */
    public static HttpServletRequest getRequest() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request == null) {
            request.getRequestedSessionId();
            request.isRequestedSessionIdValid();
            throw new RuntimeException(
                    "No se pudo recuperar HttpServletRequest");
        }
        return request;
    }

    /**
     * Retorna la session http.
     *
     * @return session
     */
    public HttpSession getSession() {
        //HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);  
        //System.err.println("el usuario es"+obtenerUsuarioAutenticado());
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.TRUE);

        return session;
    }

    public void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public void addErrorMessage(String msg1, String msg2) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg1, msg2);
        FacesContext.getCurrentInstance().addMessage("", facesMsg);
    }

    public void addWarningMessage(String msg1, String msg2) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg1, msg2);
        FacesContext.getCurrentInstance().addMessage("", facesMsg);
    }

    public void addSuccessMessage(String msg1, String msg2) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg1, msg2);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
        FacesContext.getCurrentInstance().addMessage("", facesMsg);
    }

    public void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg);
        FacesContext.getCurrentInstance().addMessage("", facesMsg);
    }

    public void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    /**
     * Obtiene usuario autenticado.
     *
     * @return Usuario
     */
    public Login obtenerUsuarioAutenticado() {
        try {
            return (Login) this.getSession().getAttribute("usuario");
        } catch (Exception e) {
            return null;
        }
    }

    public Date obtenerFechaDiaria() {
        return (Date) this.getSession().getAttribute("fechaDiaria");
    }

    public String obtenerIp() {
        //String ipCliente = getRequest().getRemoteAddr();
        String ipCliente = getRequest().getHeader("X-FORWARDED-FOR");
        if (ipCliente == null) {
            ipCliente = getRequest().getRemoteAddr();
        }
        return ipCliente;
    }

    public String obtenerIdUsuario() {
        return (String) this.getSession().getAttribute("codigoUsuario");
    }

    public static void mensajeErrorComponente(String idElemento, String resumen, String detalle) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(idElemento, new FacesMessage(FacesMessage.SEVERITY_ERROR, resumen, detalle));
    }

    public void resetearFitrosTabla(String idFormulario) {
        DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(idFormulario);
        table.reset();
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public Login getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Login usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public int getContadorIngresos() {
        return contadorIngresos;
    }

    public void setContadorIngresos(int contadorIngresos) {
        this.contadorIngresos = contadorIngresos;
    }

    public boolean isDisableIngresoVolq() {
        return disableIngresoVolq;
    }

    public void setDisableIngresoVolq(boolean disableIngresoVolq) {
        this.disableIngresoVolq = disableIngresoVolq;
    }

    public boolean isIngresarVolq() {
        return ingresarVolq;
    }

    public void setIngresarVolq(boolean ingresarVolq) {
        this.ingresarVolq = ingresarVolq;
    }
}
