/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banka;

import java.util.ArrayList;

/**
 *
 * @author Pedja
 */
public class Banka {
    public int idBan;
    ArrayList<Racun> racuni;
    
    public Banka(int idBan)
    {
        this.idBan = idBan;
        racuni = new ArrayList<Racun>();
    }
    
    
    public Racun dohvatiRacun(int idRac)
    {
        
        for(Racun r: racuni)
        {
            if (r.idRac==idRac) return r;
        }
        return null;
    }
    
    public void dodajRacun(int idRac, double stanje)
    {
        Racun r = new Racun(idRac, idBan, stanje);
        racuni.add(r);
    }
}
