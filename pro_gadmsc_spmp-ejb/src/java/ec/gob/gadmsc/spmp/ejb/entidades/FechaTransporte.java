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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MiguelAngel
 */
@Entity
@Table(name = "fecha_transporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FechaTransporte.findAll", query = "SELECT f FROM FechaTransporte f")
    , @NamedQuery(name = "FechaTransporte.findByFechaTrCodigo", query = "SELECT f FROM FechaTransporte f WHERE f.fechaTrCodigo = :fechaTrCodigo")
    , @NamedQuery(name = "FechaTransporte.findByFechaTrDia", query = "SELECT f FROM FechaTransporte f WHERE f.fechaTrDia = :fechaTrDia")
    , @NamedQuery(name = "FechaTransporte.findByFechaTrMes", query = "SELECT f FROM FechaTransporte f WHERE f.fechaTrMes = :fechaTrMes")
    , @NamedQuery(name = "FechaTransporte.findByFechaTrAnio", query = "SELECT f FROM FechaTransporte f WHERE f.fechaTrAnio = :fechaTrAnio")})
public class FechaTransporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fecha_tr_codigo")
    private Integer fechaTrCodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_tr_dia")
    private int fechaTrDia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_tr_mes")
    private int fechaTrMes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_tr_anio")
    private int fechaTrAnio;
    @OneToMany(mappedBy = "fkFechaTrCodigo")
    private List<VolquetaFecha> volquetaFechaList;
    @OneToMany(mappedBy = "fkFechaTrCodigo")
    private List<EquipoFecha> equipoFechaList;

    public FechaTransporte() {
    }

    public FechaTransporte(int dia, int mes, int anio) {
        this.fechaTrDia = dia;
        this.fechaTrMes = mes;
        this.fechaTrAnio = anio;
    }

    public FechaTransporte(Integer fechaTrCodigo) {
        this.fechaTrCodigo = fechaTrCodigo;
    }

    public FechaTransporte(Integer fechaTrCodigo, int fechaTrDia, int fechaTrMes, int fechaTrAnio) {
        this.fechaTrCodigo = fechaTrCodigo;
        this.fechaTrDia = fechaTrDia;
        this.fechaTrMes = fechaTrMes;
        this.fechaTrAnio = fechaTrAnio;
    }

    public Integer getFechaTrCodigo() {
        return fechaTrCodigo;
    }

    public void setFechaTrCodigo(Integer fechaTrCodigo) {
        this.fechaTrCodigo = fechaTrCodigo;
    }

    public int getFechaTrDia() {
        return fechaTrDia;
    }

    public void setFechaTrDia(int fechaTrDia) {
        this.fechaTrDia = fechaTrDia;
    }

    public int getFechaTrMes() {
        return fechaTrMes;
    }

    public void setFechaTrMes(int fechaTrMes) {
        this.fechaTrMes = fechaTrMes;
    }

    public int getFechaTrAnio() {
        return fechaTrAnio;
    }

    public void setFechaTrAnio(int fechaTrAnio) {
        this.fechaTrAnio = fechaTrAnio;
    }

    @XmlTransient
    public List<VolquetaFecha> getVolquetaFechaList() {
        return volquetaFechaList;
    }

    public void setVolquetaFechaList(List<VolquetaFecha> volquetaFechaList) {
        this.volquetaFechaList = volquetaFechaList;
    }

    @XmlTransient
    public List<EquipoFecha> getEquipoFechaList() {
        return equipoFechaList;
    }

    public void setEquipoFechaList(List<EquipoFecha> equipoFechaList) {
        this.equipoFechaList = equipoFechaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fechaTrCodigo != null ? fechaTrCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechaTransporte)) {
            return false;
        }
        FechaTransporte other = (FechaTransporte) object;
        if ((this.fechaTrCodigo == null && other.fechaTrCodigo != null) || (this.fechaTrCodigo != null && !this.fechaTrCodigo.equals(other.fechaTrCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte[ fechaTrCodigo=" + fechaTrCodigo + " ]";
    }

}
