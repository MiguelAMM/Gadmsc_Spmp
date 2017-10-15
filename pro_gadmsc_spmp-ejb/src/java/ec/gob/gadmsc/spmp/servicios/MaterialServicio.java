/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.servicios;

import ec.gob.gadmsc.spmp.ejb.entidades.Material;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author MiguelAngel
 */
@Local
public interface MaterialServicio {

    void create(Material material);

    void edit(Material material);

    void remove(Material material);

    Material find(Object id);

    List<Material> findAll();

    List<Material> findRange(int[] range);

    int count();
    
}
