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
@Table(name = "carga_transportada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CargaTransportada.findAll", query = "SELECT c FROM CargaTransportada c")
    , @NamedQuery(name = "CargaTransportada.findByCargaTrCodigo", query = "SELECT c FROM CargaTransportada c WHERE c.cargaTrCodigo = :cargaTrCodigo")
    , @NamedQuery(name = "CargaTransportada.findByCargaTrViaje", query = "SELECT c FROM CargaTransportada c WHERE c.cargaTrViaje = :cargaTrViaje")
    , @NamedQuery(name = "CargaTransportada.findByCargaTrObservacion", query = "SELECT c FROM CargaTransportada c WHERE c.cargaTrObservacion = :cargaTrObservacion")
    , @NamedQuery(name = "CargaTransportada.findByCargaTrComprobante", query = "SELECT c FROM CargaTransportada c WHERE c.cargaTrComprobante = :cargaTrComprobante")})
public class CargaTransportada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "carga_tr_codigo")
    private Integer cargaTrCodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carga_tr_viaje")
    private int cargaTrViaje;
    @Basic(optional = false)
    @NotNull
//    @Size(min = 1, max = 500)
    @Column(name = "carga_tr_observacion")
    private String cargaTrObservacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "carga_tr_comprobante")
    private String cargaTrComprobante;
    @JoinColumn(name = "fk_mat_codigo", referencedColumnName = "mat_codigo")
    @ManyToOne(optional = false)
    private Material fkMatCodigo;
    @JoinColumn(name = "fk_volq_fecha_codigo", referencedColumnName = "volq_fecha_codigo")
    @ManyToOne(optional = false)
    private VolquetaFecha fkVolqFechaCodigo;

    public CargaTransportada() {
    }

    public CargaTransportada(Integer cargaTrCodigo) {
        this.cargaTrCodigo = cargaTrCodigo;
    }

    public CargaTransportada(Integer cargaTrCodigo, int cargaTrViaje, String cargaTrObservacion, String cargaTrComprobante) {
        this.cargaTrCodigo = cargaTrCodigo;
        this.cargaTrViaje = cargaTrViaje;
        this.cargaTrObservacion = cargaTrObservacion;
        this.cargaTrComprobante = cargaTrComprobante;
    }

    public Integer getCargaTrCodigo() {
        return cargaTrCodigo;
    }

    public void setCargaTrCodigo(Integer cargaTrCodigo) {
        this.cargaTrCodigo = cargaTrCodigo;
    }

    public int getCargaTrViaje() {
        return cargaTrViaje;
    }

    public void setCargaTrViaje(int cargaTrViaje) {
        this.cargaTrViaje = cargaTrViaje;
    }

    public String getCargaTrObservacion() {
        return cargaTrObservacion;
    }

    public void setCargaTrObservacion(String cargaTrObservacion) {
        this.cargaTrObservacion = cargaTrObservacion;
    }

    public String getCargaTrComprobante() {
        return cargaTrComprobante;
    }

    public void setCargaTrComprobante(String cargaTrComprobante) {
        this.cargaTrComprobante = cargaTrComprobante;
    }

    public Material getFkMatCodigo() {
        return fkMatCodigo;
    }

    public void setFkMatCodigo(Material fkMatCodigo) {
        this.fkMatCodigo = fkMatCodigo;
    }

    public VolquetaFecha getFkVolqFechaCodigo() {
        return fkVolqFechaCodigo;
    }

    public void setFkVolqFechaCodigo(VolquetaFecha fkVolqFechaCodigo) {
        this.fkVolqFechaCodigo = fkVolqFechaCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cargaTrCodigo != null ? cargaTrCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaTransportada)) {
            return false;
        }
        CargaTransportada other = (CargaTransportada) object;
        if ((this.cargaTrCodigo == null && other.cargaTrCodigo != null) || (this.cargaTrCodigo != null && !this.cargaTrCodigo.equals(other.cargaTrCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.gadmsc.spmp.ejb.entidades.CargaTransportada[ cargaTrCodigo=" + cargaTrCodigo + " ]";
    }

}
