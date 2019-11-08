/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prodavac2;

import perzistiranje_prodavac.Artikl;
import java.util.ArrayList;
import javax.jms.ConnectionFactory;

/**
 *
 * @author Pedja
 */
public class Prodavac {
    private int keyholderID;
    private int idPro;
    public ArrayList<Artikl> artikli;
    TokenControl tc;
    ArtiklStatisticar as;
    ArtiklBrisac ab;
    
    
    public Prodavac(int idPro)
    {
       this.idPro=idPro;
       tc = new TokenControl(idPro, this);
       as = new ArtiklStatisticar(idPro, this);
       ab = new ArtiklBrisac(idPro, this);
       artikli = new ArrayList<Artikl>();
    }
    
    public void dodajArtikal(int idArt, int count,int cena)
    {
        for (Artikl a: artikli)
        {
            if (a.getIdArt() == idArt)
            {
                a.setCount(a.getCount()+count);
                return;
            }
        }
        artikli.add(new Artikl(idPro, idArt, count, cena));
    }
    
    public boolean dovoljnoArtikala(int idArt, int need_count)
    {
        boolean dovoljno = false;
        for (Artikl a: artikli)
            if (a.getIdArt() == idArt)
            {
                //System.out.print("Provera da li je "+a.getCount()+">="+need_count);
                dovoljno = (a.getCount()>=need_count);
                //if (dovoljno) System.out.println("vratila true");
                //else System.out.println(" vratila false");
                break;
            }
        return dovoljno;
    }
    
    public void uzmiArtikle(int idArt, int need_count)
    {
        for (Artikl a: artikli)
            if (a.getIdArt() == idArt)
            {
                //System.out.print("Provera da li je "+a.getCount()+">="+need_count);
                a.setCount(a.getCount()- need_count);
                //if (dovoljno) System.out.println("vratila true");
                //else System.out.println(" vratila false");
                break;
            }
    }
    
    public int cena(int idArt)
    {
        int ret = 0;
        for (Artikl a: artikli)
            if (a.getIdArt() == idArt)
            {
                ret = a.getCena();
                break;
            }
        return ret;
    }
    
    public void pokreni()
    {
        tc.start();
        as.start();
        ab.start();
    }
}
