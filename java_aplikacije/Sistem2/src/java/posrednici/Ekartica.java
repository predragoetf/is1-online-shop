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
public class Ekartica implements Serializable{
    
    public String ime;
    public String prezime;
    public String brracuna;
    public int idBan;

    public Ekartica(String ime, String prezime, String brracuna, int idBan) {
        this.ime = ime;
        this.prezime = prezime;
        this.brracuna = brracuna;
        this.idBan = idBan;
    }
    
    
}
