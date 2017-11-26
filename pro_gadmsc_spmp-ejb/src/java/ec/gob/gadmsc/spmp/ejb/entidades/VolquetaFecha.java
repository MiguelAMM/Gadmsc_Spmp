/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.ejb.entidades;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MiguelAngel
 */
@Entity
@Table(name = "volqueta_fecha")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VolquetaFecha.findAll", query = "SELECT v FROM VolquetaFecha v")
    , @NamedQuery(name = "VolquetaFecha.findByVolqFechaCodigo", query = "SELECT v FROM VolquetaFecha v WHERE v.volqFechaCodigo = :volqFechaCodigo")
    , @NamedQuery(name = "VolquetaFecha.findByVolqFechaCombustible", query = "SELECT v FROM VolquetaFecha v WHERE v.volqFechaCombustible = :volqFechaCombustible")
    , @NamedQuery(name = "VolquetaFecha.findByVolqFechaKm", query = "SELECT v FROM VolquetaFecha v WHERE v.volqFechaKm = :volqFechaKm")
    , @NamedQuery(name = "VolquetaFecha.findByVolqHoraE", query = "SELECT v FROM VolquetaFecha v WHERE v.volqHoraE = :volqHoraE")
    , @NamedQuery(name = "VolquetaFecha.findByVolqHoraS", query = "SELECT v FROM VolquetaFecha v WHERE v.volqHoraS = :volqHoraS")})
public class VolquetaFecha implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "volq_fecha_codigo")
    private Integer volqFechaCodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "volq_fecha_combustible")
    private int volqFechaCombustible;
    @Basic(optional = false)
    @NotNull
    @Column(name = "volq_fecha_km")
    private int volqFechaKm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "volq_hora_e")
    @Temporal(TemporalType.TIME)
    private Date volqHoraE;
    @Basic(optional = false)
    @NotNull
    @Column(name = "volq_hora_s")
    @Temporal(TemporalType.TIME)
    private Date volqHoraS;
    @OneToMany(mappedBy = "fkVolqFechaCodigo")
    private List<CargaTransportada> cargaTransportadaList;
    @JoinColumn(name = "fk_fecha_tr_codigo", referencedColumnName = "fecha_tr_codigo")
    @ManyToOne(optional = false)
    private FechaTransporte fkFechaTrCodigo;
    @JoinColumn(name = "fk_usu_codigo", referencedColumnName = "usu_codigo")
    @ManyToOne(optional = false)
    private Usuario fkUsuCodigo;

    public VolquetaFecha() {
    }

    public VolquetaFecha(Integer volqFechaCodigo) {
        this.volqFechaCodigo = volqFechaCodigo;
    }

    public VolquetaFecha(Integer volqFechaCodigo, int volqFechaCombustible, int volqFechaKm, Date volqHoraE, Date volqHoraS) {
        this.volqFechaCodigo = volqFechaCodigo;
        this.volqFechaCombustible = volqFechaCombustible;
        this.volqFechaKm = volqFechaKm;
        this.volqHoraE = volqHoraE;
        this.volqHoraS = volqHoraS;
    }

    public Integer getVolqFechaCodigo() {
        return volqFechaCodigo;
    }

    public void setVolqFechaCodigo(Integer volqFechaCodigo) {
        this.volqFechaCodigo = volqFechaCodigo;
    }

    public int getVolqFechaCombustible() {
        return volqFechaCombustible;
    }

    public void setVolqFechaCombustible(int volqFechaCombustible) {
        this.volqFechaCombustible = volqFechaCombustible;
    }

    public int getVolqFechaKm() {
        return volqFechaKm;
    }

    public void setVolqFechaKm(int volqFechaKm) {
        this.volqFechaKm = volqFechaKm;
    }

    public Date getVolqHoraE() {
        return volqHoraE;
    }

    public void setVolqHoraE(Date volqHoraE) {
        this.volqHoraE = volqHoraE;
    }

    public Date getVolqHoraS() {
        return volqHoraS;
    }

    public void setVolqHoraS(Date volqHoraS) {
        this.volqHoraS = volqHoraS;
    }

    @XmlTransient
    public List<CargaTransportada> getCargaTransportadaList() {
        return cargaTransportadaList;
    }

    public void setCargaTransportadaList(List<CargaTransportada> cargaTransportadaList) {
        this.cargaTransportadaList = cargaTransportadaList;
    }

    public FechaTransporte getFkFechaTrCodigo() {
        return fkFechaTrCodigo;
    }

    public void setFkFechaTrCodigo(FechaTransporte fkFechaTrCodigo) {
        this.fkFechaTrCodigo = fkFechaTrCodigo;
    }

    public Usuario getFkUsuCodigo() {
        return fkUsuCodigo;
    }

    public void setFkUsuCodigo(Usuario fkUsuCodigo) {
        this.fkUsuCodigo = fkUsuCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (volqFechaCodigo != null ? volqFechaCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VolquetaFecha)) {
            return false;
        }
        VolquetaFecha other = (VolquetaFecha) object;
        if ((this.volqFechaCodigo == null && other.volqFechaCodigo != null) || (this.volqFechaCodigo != null && !this.volqFechaCodigo.equals(other.volqFechaCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.gadmsc.spmp.ejb.entidades.VolquetaFecha[ volqFechaCodigo=" + volqFechaCodigo + " ]";
    }

}
