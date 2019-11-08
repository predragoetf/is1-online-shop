/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posredniciBanka;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Pedja
 */
public class TransactionRequest implements Serializable{
    public int idFetcher;
    public int ukupan_iznos;
    public HashMap<Integer, Integer> iznosi;
    public int idRac;
    public int idBan;

    public TransactionRequest(int idFetcher, int ukupan_iznos, HashMap<Integer, Integer> iznosi, int idRac, int idBan) {
        this.idFetcher = idFetcher;
        this.ukupan_iznos = ukupan_iznos;
        this.iznosi = iznosi;
        this.idRac = idRac;
        this.idBan = idBan;
    }
    
    
}
