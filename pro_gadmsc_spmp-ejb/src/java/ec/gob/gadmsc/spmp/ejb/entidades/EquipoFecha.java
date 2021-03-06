/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    , @NamedQuery(name = "EquipoFecha.findByEqFechaObservacion", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaObservacion = :eqFechaObservacion")
    , @NamedQuery(name = "EquipoFecha.findByEqFechaHoraE", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaHoraE = :eqFechaHoraE")
    , @NamedQuery(name = "EquipoFecha.findByEqFechaHoraS", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaHoraS = :eqFechaHoraS")
    , @NamedQuery(name = "EquipoFecha.findByEqFechaKmEntrada", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaKmEntrada = :eqFechaKmEntrada")
    , @NamedQuery(name = "EquipoFecha.findByEqFechaKmSalida", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaKmSalida = :eqFechaKmSalida")
    , @NamedQuery(name = "EquipoFecha.findByEqFechaActividad", query = "SELECT e FROM EquipoFecha e WHERE e.eqFechaActividad = :eqFechaActividad")})
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
    @Size(max = 200)
    @Column(name = "eq_fecha_observacion")
    private String eqFechaObservacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eq_fecha_hora_e")
    @Temporal(TemporalType.TIME)
    private Date eqFechaHoraE;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eq_fecha_hora_s")
    @Temporal(TemporalType.TIME)
    private Date eqFechaHoraS;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eq_fecha_km_entrada")
    private int eqFechaKmEntrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eq_fecha_km_salida")
    private int eqFechaKmSalida;
    @Size(max = 200)
    @Column(name = "eq_fecha_actividad")
    private String eqFechaActividad;
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

    public EquipoFecha(Integer eqFechaCodigo, int eqFechaCombustible, Date eqFechaHoraE, Date eqFechaHoraS,
            String observacion, String actividad, int eqFechaKmEntrada, int eqFechaKmSalida) {
        this.eqFechaCodigo = eqFechaCodigo;
        this.eqFechaCombustible = eqFechaCombustible;
        this.eqFechaObservacion = observacion;
        this.eqFechaActividad = actividad;
        this.eqFechaHoraE = eqFechaHoraE;
        this.eqFechaHoraS = eqFechaHoraS;
        this.eqFechaKmEntrada = eqFechaKmEntrada;
        this.eqFechaKmSalida = eqFechaKmSalida;
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

    public Date getEqFechaHoraE() {
        return eqFechaHoraE;
    }

    public void setEqFechaHoraE(Date eqFechaHoraE) {
        this.eqFechaHoraE = eqFechaHoraE;
    }

    public Date getEqFechaHoraS() {
        return eqFechaHoraS;
    }

    public void setEqFechaHoraS(Date eqFechaHoraS) {
        this.eqFechaHoraS = eqFechaHoraS;
    }

    public int getEqFechaKmEntrada() {
        return eqFechaKmEntrada;
    }

    public void setEqFechaKmEntrada(int eqFechaKmEntrada) {
        this.eqFechaKmEntrada = eqFechaKmEntrada;
    }

    public int getEqFechaKmSalida() {
        return eqFechaKmSalida;
    }

    public void setEqFechaKmSalida(int eqFechaKmSalida) {
        this.eqFechaKmSalida = eqFechaKmSalida;
    }

    public String getEqFechaActividad() {
        return eqFechaActividad;
    }

    public void setEqFechaActividad(String eqFechaActividad) {
        this.eqFechaActividad = eqFechaActividad;
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
