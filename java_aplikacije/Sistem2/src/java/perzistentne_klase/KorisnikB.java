/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perzistentne_klase;

import javax.persistence.*;

/**
 *
 * @author Pedja
 */
@Entity
@Table(name = "korisnikb")
public class KorisnikB {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="IdKorisnika")
    private int idKorisnika;
    
    @Column(name="Ime")
    private String ime;
    
    @Column(name="Prezime")
    private String prezime;
    
    @Column(name="IdBan", nullable = false)
    private int idBan;
    
    @Column(name="BrRacuna")
    private int brRacuna;
    
    @Column(name="BrojUspesnihKupovina")
    private int brusp=0;
    
    @Column(name="BrojNeuspesnihKupovina")
    private int brneusp=0;

    public KorisnikB(){}
    
    public KorisnikB(int idBan, int brRacuna) {
        this.idBan = idBan;
        this.brRacuna = brRacuna;
    }

    public KorisnikB(String ime, String prezime, int idBan, int brRacuna) {
        this.ime = ime;
        this.prezime = prezime;
        this.idBan = idBan;
        this.brRacuna = brRacuna;
    }
        
    public int getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(int idKorisnika) {
        this.idKorisnika = idKorisnika;
    }  

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public int getIdBan() {
        return idBan;
    }

    public void setIdBan(int idBan) {
        this.idBan = idBan;
    }

    public int getBrRacuna() {
        return brRacuna;
    }

    public void setBrRacuna(int brRacuna) {
        this.brRacuna = brRacuna;
    }

    public int getBrusp() {
        return brusp;
    }

    public void setBrusp(int brusp) {
        this.brusp = brusp;
    }

    public int getBrneusp() {
        return brneusp;
    }

    public void setBrneusp(int brneusp) {
        this.brneusp = brneusp;
    }
    
    
    
    
}
