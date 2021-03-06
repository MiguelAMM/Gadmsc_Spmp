/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MiguelAngel
 */
@Entity
@Table(name = "equipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equipo.findAll", query = "SELECT e FROM Equipo e")
    , @NamedQuery(name = "Equipo.findByEqCodigo", query = "SELECT e FROM Equipo e WHERE e.eqCodigo = :eqCodigo")
    , @NamedQuery(name = "Equipo.findByEqTipo", query = "SELECT e FROM Equipo e WHERE e.eqTipo = :eqTipo")
    , @NamedQuery(name = "Equipo.findByEqPlaca", query = "SELECT e FROM Equipo e WHERE e.eqPlaca = :eqPlaca")
    , @NamedQuery(name = "Equipo.findByEqPass", query = "SELECT e FROM Equipo e WHERE e.eqPass = :eqPass")
    , @NamedQuery(name = "Equipo.findByEqTipoUs", query = "SELECT e FROM Equipo e WHERE e.eqTipoUs = :eqTipoUs")})
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eq_codigo")
    private Integer eqCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100, message = "Ingrese equipo caminero")
    @Column(name = "eq_tipo")
    private String eqTipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8, message = "Los caracteres de la placa deben estar entre 1 y 8")
    @Column(name = "eq_placa")
    private String eqPlaca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50, message = "Ingrese contraseña")
    @Column(name = "eq_pass")
    private String eqPass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "eq_tipo_us")
    private String eqTipoUs;
    @OneToMany(mappedBy = "fkEqCodigo")
    private List<EquipoFecha> equipoFechaList;
    @JoinColumn(name = "fk_chofer_codigo", referencedColumnName = "chofer_codigo")
    @ManyToOne(optional = false)
    private Chofer fkChoferCodigo;

    public Equipo() {
    }

    public Equipo(Integer eqCodigo) {
        this.eqCodigo = eqCodigo;
    }

    public Equipo(Integer eqCodigo, String eqTipo, String eqPlaca, String eqPass, String eqTipoUs) {
        this.eqCodigo = eqCodigo;
        this.eqTipo = eqTipo;
        this.eqPlaca = eqPlaca;
        this.eqPass = eqPass;
        this.eqTipoUs = eqTipoUs;
    }

    public Integer getEqCodigo() {
        return eqCodigo;
    }

    public void setEqCodigo(Integer eqCodigo) {
        this.eqCodigo = eqCodigo;
    }

    public String getEqTipo() {
        return eqTipo;
    }

    public void setEqTipo(String eqTipo) {
        this.eqTipo = eqTipo;
    }

    public String getEqPlaca() {
        return eqPlaca;
    }

    public void setEqPlaca(String eqPlaca) {
        this.eqPlaca = eqPlaca;
    }

    public String getEqPass() {
        return eqPass;
    }

    public void setEqPass(String eqPass) {
        this.eqPass = eqPass;
    }

    public String getEqTipoUs() {
        return eqTipoUs;
    }

    public void setEqTipoUs(String eqTipoUs) {
        this.eqTipoUs = eqTipoUs;
    }

    @XmlTransient
    public List<EquipoFecha> getEquipoFechaList() {
        return equipoFechaList;
    }

    public void setEquipoFechaList(List<EquipoFecha> equipoFechaList) {
        this.equipoFechaList = equipoFechaList;
    }

    public Chofer getFkChoferCodigo() {
        return fkChoferCodigo;
    }

    public void setFkChoferCodigo(Chofer fkChoferCodigo) {
        this.fkChoferCodigo = fkChoferCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eqCodigo != null ? eqCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipo)) {
            return false;
        }
        Equipo other = (Equipo) object;
        if ((this.eqCodigo == null && other.eqCodigo != null) || (this.eqCodigo != null && !this.eqCodigo.equals(other.eqCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return eqTipo;
    }

}
