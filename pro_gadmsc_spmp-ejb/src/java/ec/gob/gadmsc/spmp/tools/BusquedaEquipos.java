/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.tools;

import ec.gob.gadmsc.spmp.ejb.entidades.Equipo;
import ec.gob.gadmsc.spmp.ejb.entidades.Usuario;
import java.util.List;

/**
 *
 * @author MiguelAngel
 */
public class BusquedaEquipos {

    public Equipo buscarEquipoChofer(List<Equipo> listaEquipos, int codigoChofer) {
        for (Equipo e : listaEquipos) {
            if (e.getFkChoferCodigo().getChoferCodigo() == codigoChofer) {
                return e;
            }
        }
        return null;
    }

    public Usuario buscarVolquetaChofer(List<Usuario> listaVolquetas, int codigoChofer) {
        for (Usuario u : listaVolquetas) {
            if (u.getFkChoferCodigo().getChoferCodigo() == codigoChofer) {
                return u;
            }
        }
        return null;
    }
}
