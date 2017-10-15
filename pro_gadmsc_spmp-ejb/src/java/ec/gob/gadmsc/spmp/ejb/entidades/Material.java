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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MiguelAngel
 */
@Entity
@Table(name = "material")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Material.findAll", query = "SELECT m FROM Material m")
    , @NamedQuery(name = "Material.findByMatCodigo", query = "SELECT m FROM Material m WHERE m.matCodigo = :matCodigo")
    , @NamedQuery(name = "Material.findByMatNombre", query = "SELECT m FROM Material m WHERE m.matNombre = :matNombre")})
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mat_codigo")
    private Integer matCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "mat_nombre")
    private String matNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkMatCodigo")
    private List<CargaTransportada> cargaTransportadaList;

    public Material() {
    }

    public Material(Integer matCodigo) {
        this.matCodigo = matCodigo;
    }

    public Material(Integer matCodigo, String matNombre) {
        this.matCodigo = matCodigo;
        this.matNombre = matNombre;
    }

    public Integer getMatCodigo() {
        return matCodigo;
    }

    public void setMatCodigo(Integer matCodigo) {
        this.matCodigo = matCodigo;
    }

    public String getMatNombre() {
        return matNombre;
    }

    public void setMatNombre(String matNombre) {
        this.matNombre = matNombre;
    }

    @XmlTransient
    public List<CargaTransportada> getCargaTransportadaList() {
        return cargaTransportadaList;
    }

    public void setCargaTransportadaList(List<CargaTransportada> cargaTransportadaList) {
        this.cargaTransportadaList = cargaTransportadaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matCodigo != null ? matCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Material)) {
            return false;
        }
        Material other = (Material) object;
        if ((this.matCodigo == null && other.matCodigo != null) || (this.matCodigo != null && !this.matCodigo.equals(other.matCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return matNombre;
    }

}
