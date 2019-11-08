/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perzistiranje_prodavac;

/**
 *
 * @author Pedja
 */
public class Artikl {
    private int idPro;
    private int idArt;
    private int count;
    private int cena;

    public Artikl(int idPro, int idArt, int count, int cena) {
        this.idPro = idPro;
        this.idArt = idArt;
        this.count = count;
        this.cena = cena;
    }
    
    

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIdArt() {
        return idArt;
    }

    public void setIdArt(int idArt) {
        this.idArt = idArt;
    }

    public int getIdPro() {
        return idPro;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }
    
    
    
}
