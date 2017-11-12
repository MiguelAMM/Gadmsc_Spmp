/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.entidades;

import com.cursojsf.validadores.Cedula;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MiguelAngel
 */
@Entity
@Table(name = "chofer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Chofer.findAll", query = "SELECT c FROM Chofer c")
    , @NamedQuery(name = "Chofer.findByChoferCodigo", query = "SELECT c FROM Chofer c WHERE c.choferCodigo = :choferCodigo")
    , @NamedQuery(name = "Chofer.findByChoferNombre", query = "SELECT c FROM Chofer c WHERE c.choferNombre = :choferNombre")
    , @NamedQuery(name = "Chofer.findByChoferApellido", query = "SELECT c FROM Chofer c WHERE c.choferApellido = :choferApellido")
    , @NamedQuery(name = "Chofer.findByChoferCi", query = "SELECT c FROM Chofer c WHERE c.choferCi = :choferCi")
    , @NamedQuery(name = "Chofer.findByChoferTlf", query = "SELECT c FROM Chofer c WHERE c.choferTlf = :choferTlf")
    , @NamedQuery(name = "Chofer.findByChoferDireccion", query = "SELECT c FROM Chofer c WHERE c.choferDireccion = :choferDireccion")
    , @NamedQuery(name = "Chofer.findByChoferFechaNac", query = "SELECT c FROM Chofer c WHERE c.choferFechaNac = :choferFechaNac")
    , @NamedQuery(name = "Chofer.findByChoferAsignado", query = "SELECT c FROM Chofer c WHERE c.choferAsignado = :choferAsignado")
    , @NamedQuery(name = "Chofer.findByChoferEmail", query = "SELECT c FROM Chofer c WHERE c.choferEmail = :choferEmail")})
public class Chofer implements Serializable {

    @OneToMany(mappedBy = "fkChoferCodigo")
    private List<Equipo> equipoList;
    @OneToMany(mappedBy = "fkChoferCodigo")
    private List<Usuario> usuarioList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "chofer_codigo")
    private Integer choferCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "chofer_nombre")
    private String choferNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "chofer_apellido")
    private String choferApellido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Cedula(message = "La cédula es inválida")
    @Column(name = "chofer_ci")
    private String choferCi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "chofer_tlf")
    private String choferTlf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "chofer_direccion")
    private String choferDireccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "chofer_fecha_nac")
    @Temporal(TemporalType.DATE)
    private Date choferFechaNac;
    @Basic(optional = false)
    @NotNull(message = "Elija opción de asignado")
    @Size(min = 1, max = 2)
    @Column(name = "chofer_asignado")
    private String choferAsignado;
    @Size(max = 100)
    @Column(name = "chofer_email")
    private String choferEmail;

    public Chofer() {
    }

    public Chofer(Integer choferCodigo) {
        this.choferCodigo = choferCodigo;
    }

    public Chofer(Integer choferCodigo, String choferNombre, String choferApellido, String choferCi, String choferTlf, String choferDireccion, Date choferFechaNac, String choferAsignado) {
        this.choferCodigo = choferCodigo;
        this.choferNombre = choferNombre;
        this.choferApellido = choferApellido;
        this.choferCi = choferCi;
        this.choferTlf = choferTlf;
        this.choferDireccion = choferDireccion;
        this.choferFechaNac = choferFechaNac;
        this.choferAsignado = choferAsignado;
    }

    public Integer getChoferCodigo() {
        return choferCodigo;
    }

    public void setChoferCodigo(Integer choferCodigo) {
        this.choferCodigo = choferCodigo;
    }

    public String getChoferNombre() {
        return choferNombre;
    }

    public void setChoferNombre(String choferNombre) {
        this.choferNombre = choferNombre;
    }

    public String getChoferApellido() {
        return choferApellido;
    }

    public void setChoferApellido(String choferApellido) {
        this.choferApellido = choferApellido;
    }

    public String getChoferCi() {
        return choferCi;
    }

    public void setChoferCi(String choferCi) {
        this.choferCi = choferCi;
    }

    public String getChoferTlf() {
        return choferTlf;
    }

    public void setChoferTlf(String choferTlf) {
        this.choferTlf = choferTlf;
    }

    public String getChoferDireccion() {
        return choferDireccion;
    }

    public void setChoferDireccion(String choferDireccion) {
        this.choferDireccion = choferDireccion;
    }

    public Date getChoferFechaNac() {
        return choferFechaNac;
    }

    public void setChoferFechaNac(Date choferFechaNac) {
        this.choferFechaNac = choferFechaNac;
    }

    public String getChoferAsignado() {
        return choferAsignado;
    }

    public void setChoferAsignado(String choferAsignado) {
        this.choferAsignado = choferAsignado;
    }

    public String getChoferEmail() {
        return choferEmail;
    }

    public void setChoferEmail(String choferEmail) {
        this.choferEmail = choferEmail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (choferCodigo != null ? choferCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Chofer)) {
            return false;
        }
        Chofer other = (Chofer) object;
        if ((this.choferCodigo == null && other.choferCodigo != null) || (this.choferCodigo != null && !this.choferCodigo.equals(other.choferCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return choferNombre + " " + choferApellido;
    }

    @XmlTransient
    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

}
