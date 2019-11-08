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
@Table(name = "korpab")
public class KorpaB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdKorpe")
    private int idKorpe;
    
    @ManyToOne
    @JoinColumn(name="IdKorisnika", referencedColumnName="IdKorisnika")
    private KorisnikB mojkupac;

    public KorpaB(KorisnikB mojkupac) {
        this.mojkupac = mojkupac;
    }

    public KorpaB() {}

    public int getIdKorpe() {
        return idKorpe;
    }

    public void setIdKorpe(int idKorpe) {
        this.idKorpe = idKorpe;
    }

    public KorisnikB getMojkupac() {
        return mojkupac;
    }

    public void setMojkupac(KorisnikB mojkupac) {
        this.mojkupac = mojkupac;
    }
}
