/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.tools;

/**
 *
 * @author MiguelAngel
 */
public enum Meses {
    ENERO(1), FEBRERO(2), MARZO(3), ABRIL(4), MAYO(5), JUNIO(6), JULIO(7),
    AGOSTO(8), SEPTIEMBRE(9), OCTUBRE(10), NOVIEMBRE(11), DICIEMBRE(12);

    int numeroMes;

    Meses(int numMes) {
        this.numeroMes = numMes;
    }

    public int getNumeroMes() {
        return numeroMes;
    }
}
