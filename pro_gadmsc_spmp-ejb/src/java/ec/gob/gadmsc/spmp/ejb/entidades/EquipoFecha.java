/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MiguelAngel
 */
@Entity
@Table(name = "equipo_fecha")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipoFecha.findAll", query = "SELECT e FROM EquipoFecha e")
    , @NamedQuery(name = "EquipoFecha.findByEqFechaCodigo", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaCodigo = :eqFechaCodigo")
    , @NamedQuery(name = "EquipoFecha.findByEqFechaCombustible", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaCombustible = :eqFechaCombustible")
    , @NamedQuery(name = "EquipoFecha.findByEqFechaObservacion", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaObservacion = :eqFechaObservacion")})
public class EquipoFecha implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eq_fecha_codigo")
    private Integer eqFechaCodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eq_fecha_combustible")
    private int eqFechaCombustible;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "eq_fecha_observacion")
    private String eqFechaObservacion;
    @JoinColumn(name = "fk_eq_codigo", referencedColumnName = "eq_codigo")
    @ManyToOne(optional = false)
    private Equipo fkEqCodigo;
    @JoinColumn(name = "fk_fecha_tr_codigo", referencedColumnName = "fecha_tr_codigo")
    @ManyToOne(optional = false)
    private FechaTransporte fkFechaTrCodigo;

    public EquipoFecha() {
    }

    public EquipoFecha(Integer eqFechaCodigo) {
        this.eqFechaCodigo = eqFechaCodigo;
    }

    public EquipoFecha(Integer eqFechaCodigo, int eqFechaCombustible, String eqFechaObservacion) {
        this.eqFechaCodigo = eqFechaCodigo;
        this.eqFechaCombustible = eqFechaCombustible;
        this.eqFechaObservacion = eqFechaObservacion;
    }

    public Integer getEqFechaCodigo() {
        return eqFechaCodigo;
    }

    public void setEqFechaCodigo(Integer eqFechaCodigo) {
        this.eqFechaCodigo = eqFechaCodigo;
    }

    public int getEqFechaCombustible() {
        return eqFechaCombustible;
    }

    public void setEqFechaCombustible(int eqFechaCombustible) {
        this.eqFechaCombustible = eqFechaCombustible;
    }

    public String getEqFechaObservacion() {
        return eqFechaObservacion;
    }

    public void setEqFechaObservacion(String eqFechaObservacion) {
        this.eqFechaObservacion = eqFechaObservacion;
    }

    public Equipo getFkEqCodigo() {
        return fkEqCodigo;
    }

    public void setFkEqCodigo(Equipo fkEqCodigo) {
        this.fkEqCodigo = fkEqCodigo;
    }

    public FechaTransporte getFkFechaTrCodigo() {
        return fkFechaTrCodigo;
    }

    public void setFkFechaTrCodigo(FechaTransporte fkFechaTrCodigo) {
        this.fkFechaTrCodigo = fkFechaTrCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eqFechaCodigo != null ? eqFechaCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipoFecha)) {
            return false;
        }
        EquipoFecha other = (EquipoFecha) object;
        if ((this.eqFechaCodigo == null && other.eqFechaCodigo != null) || (this.eqFechaCodigo != null && !this.eqFechaCodigo.equals(other.eqFechaCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.gadmsc.spmp.ejb.entidades.EquipoFecha[ eqFechaCodigo=" + eqFechaCodigo + " ]";
    }
    
}
