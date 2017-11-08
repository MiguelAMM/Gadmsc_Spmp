/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.jsf.converters;

import ec.gob.gadmsc.spmp.ejb.entidades.Material;
import ec.gob.gadmsc.spmp.jsf.base.BaseControlador;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 *
 * @author cquezada
 */
@ManagedBean(name = "materialConverter")
@ViewScoped
public class MaterialConverter extends BaseControlador implements Converter{
    
 @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) throws ConverterException {

        if (arg2.trim().equals("")) {
            return null;
        } else {
            return new Material(Integer.parseInt(arg2));
        }

    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
            throws ConverterException {
        if (arg2 == null || arg2.equals("")) {
            return "";
        } else {
            return String.valueOf(((Material) arg2).getMatCodigo());
        }
    }   
}
