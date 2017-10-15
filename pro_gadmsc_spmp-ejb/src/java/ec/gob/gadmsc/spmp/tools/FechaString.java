/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.tools;

import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author desoeco
 */
public class FechaString {

    //<editor-fold desc="Atributos-propiedades" defaultstate="collapsed">
    private static List<SimpleDateFormat> dateFormats;
    private Integer diaInicio;
    private Integer mesInicio;
    private Integer anioInicio;
    private Integer diaFin;
    private Integer mesFin;
    private Integer anioFin;
    //</editor-fold>

    //<editor-fold desc="constructor" defaultstate="collapsed">
    public FechaString() {
        dateFormats = new ArrayList<SimpleDateFormat>();/* {
            {
                add(new SimpleDateFormat("M/dd/yyyy"));
                add(new SimpleDateFormat("dd.M.yyyy"));
                add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
                add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));

                add(new SimpleDateFormat("MM/dd/yyyy"));
                add(new SimpleDateFormat("dd/MM/yyyy"));
                add(new SimpleDateFormat("yyyy/MM/dd"));
                add(new SimpleDateFormat("yyyy/dd/MM"));

                add(new SimpleDateFormat("MM-dd-yyyy"));
                add(new SimpleDateFormat("dd-MM-yyyy"));
                add(new SimpleDateFormat("yyyy-MM-dd"));
                add(new SimpleDateFormat("yyyy-dd-MM"));

                add(new SimpleDateFormat("MM.dd.yyyy"));
                add(new SimpleDateFormat("dd.MM.yyyy"));
                add(new SimpleDateFormat("yyyy.MM.dd"));
                add(new SimpleDateFormat("yyyy.dd.MM"));

                add(new SimpleDateFormat("MMddyyyy"));
                add(new SimpleDateFormat("ddMMyyyy"));
                add(new SimpleDateFormat("yyyyMMdd"));
                add(new SimpleDateFormat("yyyyddMM"));

                add(new SimpleDateFormat("dd-MMM-yyyy"));
                add(new SimpleDateFormat("dd-MMM-yyyy"));
                add(new SimpleDateFormat("dd-MMM-yyyy"));
                add(new SimpleDateFormat("dd-MMM-yyyy"));

            }
        };*/
        String[] separadores = new String[]{".", "-", "/", "", "_"};
        String[] diaFecha = new String[]{"dd", "ddd"};
        String[] mesFecha = new String[]{"MM", "MMM", "MMMM"};
        String[] anioFecha = new String[]{"yy", "yyyy"};
        String[] timeFecha = new String[]{"", " hh:mm:ss a", " hh:mm:ss", " HH:mm:ss", " h:m:s a", " h:m:s", " H:m:s"};
        for (String separador : separadores) {
            for (String dia : diaFecha) {
                for (String mes : mesFecha) {
                    for (String anio : anioFecha) {
                        for (String tiempo : timeFecha) {
                            dateFormats.add(new SimpleDateFormat(dia + separador + mes + separador + anio + tiempo));
                            dateFormats.add(new SimpleDateFormat(mes + separador + dia + separador + anio + tiempo));
                            dateFormats.add(new SimpleDateFormat(anio + separador + mes + separador + dia + tiempo));
                            dateFormats.add(new SimpleDateFormat(anio + separador + dia + separador + mes + tiempo));
                        }
                    }
                }
            }
        }

    }
    //</editor-fold>

    //<editor-fold desc="get and set" defaultstate="collapsed">
    public Integer getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(Integer dia) {
        this.diaInicio = dia;
    }

    public Integer getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(Integer mes) {
        this.mesInicio = mes;
    }

    public Integer getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(Integer anio) {
        this.anioInicio = anio;
    }

    public Integer getDiaFin() {
        return diaFin;
    }

    public void setDiaFin(Integer diaFin) {
        this.diaFin = diaFin;
    }

    public Integer getMesFin() {
        return mesFin;
    }

    public void setMesFin(Integer mesFin) {
        this.mesFin = mesFin;
    }

    public Integer getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Integer anioFin) {
        this.anioFin = anioFin;
    }
    //</editor-fold>

    //<editor-fold desc="Metodos" defaultstate="collapsed">
    public boolean isvalidDate(String dateString) {
        Date date = null;
        if (null == dateString) {
            return false;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = format.parse(dateString);
                return true;
            } catch (ParseException e) {
                //return false;
            }
        }
        return false;
    }

    public Date convertToDate(String input) {
        Date date = null;
        if (null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = format.parse(input);
            } catch (ParseException e) {
                //Shhh.. try other formats
            }
            if (date != null) {
                break;
            }
        }
        return date;
    }

    public void separarFecha(Date fechaInicio, Date fechaFin) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        diaInicio = cal.get(Calendar.DAY_OF_MONTH);
        mesInicio = cal.get(Calendar.MONTH) + 1;
        anioInicio = cal.get(Calendar.YEAR);
        cal.setTime(fechaFin);
        diaFin = cal.get(Calendar.DAY_OF_MONTH);
        mesFin = cal.get(Calendar.MONTH) + 1;
        anioFin = cal.get(Calendar.YEAR);
    }

    public String formatearFecha(FechaTransporte fechaTrans) {
        String diaCadena, mesCadena, anioCadena;
        int diaFechaTrans = fechaTrans.getFechaTrDia();
        int mesFechaTrans = fechaTrans.getFechaTrMes();
        int anioFechaTrans = fechaTrans.getFechaTrAnio();
        if (diaFechaTrans <= 9) {
            diaCadena = "0" + diaFechaTrans;
        } else {
            diaCadena = String.valueOf(diaFechaTrans);
        }
        if (mesFechaTrans <= 9) {
            mesCadena = "0" + mesFechaTrans;
        } else {
            mesCadena = String.valueOf(mesFechaTrans);
        }
        anioCadena = String.valueOf(anioFechaTrans);
        return diaCadena + "/" + mesCadena + "/" + anioCadena;
    }

    public void comprobarFechas(Date fechaInicio, Date fechaFin) throws ReporteException {
        long restaFechas = fechaFin.getTime() - fechaInicio.getTime();
        if (restaFechas < 0) {
            throw new ReporteException("La FECHA INICIAL debe ser menor a la FECHA FINAL");
        }
    }
    //</editor-fold>    
}
