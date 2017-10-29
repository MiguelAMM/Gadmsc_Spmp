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
public class Login {

    private String usuario;
    private String password;
    private String tipoUsuario;

    public boolean validarUsuario(List<Usuario> usuarios, List<Equipo> equipos) {
        for (Usuario u : usuarios) {
            if (u.getUsuNombre().equals(usuario) && u.getUsuPass().equals(password)) {
                tipoUsuario = u.getUsuTipo();
                return true;
            }
        }

        for (Equipo eq : equipos) {
            if (eq.getEqTipo().equals(usuario) && eq.getEqPass().equals(password)) {
                tipoUsuario = eq.getEqTipoUs();
                return true;
            }
        }
        return false;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

}
