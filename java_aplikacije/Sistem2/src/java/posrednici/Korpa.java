/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posrednici;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Pedja
 */
public class Korpa implements Serializable{
    
    public List<Element> sadrzaj;
    public Ekartica ekartica;
    public static int statid=1;
    public int countid;
    
    public Korpa(List<Element> sadrzaj, Ekartica ekartica)
    {
        countid = statid++;
        this.sadrzaj = sadrzaj;
        this.ekartica = ekartica;
    }
}
