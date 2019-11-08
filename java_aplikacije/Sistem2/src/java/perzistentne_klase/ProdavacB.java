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
@Table(name = "prodavacb")
public class ProdavacB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdProdavca")
    private int idProdavca;
    
    @Column(name = "PIB", unique = true, nullable = false)
    private int PIB;
    
    @Column(name = "Naziv")
    private String naziv;
    
    @Column(name = "Prodato")
    private double prodato;
    
    public ProdavacB(){}

    public ProdavacB(int PIB, String naziv, double prodato) {
        this.PIB = PIB;
        this.naziv = naziv;
        this.prodato = prodato;
    }        

    public int getPIB() {
        return PIB;
    }

    public void setPIB(int PIB) {
        this.PIB = PIB;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getProdato() {
        return prodato;
    }

    public void setProdato(double prodato) {
        this.prodato = prodato;
    }
    
    
    
}
