/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.servicios;

import ec.gob.gadmsc.spmp.ejb.entidades.Chofer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author MiguelAngel
 */
@Local
public interface ChoferServicio {

    void create(Chofer chofer);

    void edit(Chofer chofer);

    void remove(Chofer chofer);

    Chofer find(Object id);

    List<Chofer> findAll();

    List<Chofer> findRange(int[] range);

    int count();
    
}
