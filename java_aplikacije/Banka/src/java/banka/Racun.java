/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banka;

/**
 *
 * @author Pedja
 */
public class Racun {
    public int idRac;
    public int idBan;
    public double stanje;

    public Racun(int racID, int idBan, double stanje) {
        this.idRac = racID;
        this.idBan = idBan;
        this.stanje = stanje;
    }
    
    public void uplati(double iznos)
    {
        stanje+=iznos;
    }
    
    public void skini(double iznos)
    {
        stanje-=iznos;
    }
    
    public boolean checkDovoljno(double iznos)
    {
        System.out.println("Poredim da li je "+stanje+">="+iznos);
        return (stanje>=iznos);
    }
    
}
