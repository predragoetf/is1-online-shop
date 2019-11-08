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
@Table(name="artikalb")
public class ArtikalB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdArtikla")
    private int idArtikla;
    
    @ManyToOne
    @JoinColumn(name="IdProdavca", referencedColumnName="IdProdavca")
    private ProdavacB prodavac;
        
    @ManyToOne
    @JoinColumn(name="IdKorpe", referencedColumnName="IdKorpe")
    private KorpaB korpa;
    
    @Column(name = "Tip")
    private int tip;
    
    @Column(name = "Cena")
    private int cena;
    
    @Column(name = "Kolicina")
    private int kolicina;
    
    public ArtikalB(){}

    public ArtikalB(int tip, ProdavacB prodavac, KorpaB korpa, int cena, int kolicina) {
        this.prodavac = prodavac;
        this.korpa = korpa;
        this.cena = cena;
        this.kolicina = kolicina;
        this.tip = tip;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }    

    public int getIdArtikla() {
        return idArtikla;
    }

    public void setIdArtikla(int idArtikla) {
        this.idArtikla = idArtikla;
    }

    public ProdavacB getProdavac() {
        return prodavac;
    }

    public void setProdavac(ProdavacB prodavac) {
        this.prodavac = prodavac;
    }

    public KorpaB getKorpa() {
        return korpa;
    }

    public void setKorpa(KorpaB korpa) {
        this.korpa = korpa;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }
    
    
    
    
}
