/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posrednici;

import java.io.Serializable;

/**
 *
 * @author Pedja
 */
public class Element implements Serializable{
    private int idA;
    private int n;
    private int idPro;
    
    public Element(int idA, int n, int idPro)
    {
        this.idA = idA;
        this.idPro = idPro;
        this.n = n;
    }

    public int getIdA() {
        return idA;
    }

    public int getIdPro() {
        return idPro;
    }

    public int getN() {
        return n;
    }
    
}
