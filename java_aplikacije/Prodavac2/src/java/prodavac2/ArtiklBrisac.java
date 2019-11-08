/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prodavac2;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

/**
 *
 * @author Pedja
 */
public class ArtiklBrisac extends Thread{
    private JMSContext context;
    private int idPro;
    private int idFetcher;
    private Prodavac prodavac;
    
    public ArtiklBrisac(int idPro, Prodavac prodavac)
    {
        this.idPro = idPro;
        this.prodavac = prodavac;
        context = Main.connectionFactory.createContext();
        idFetcher = 0;
        
    }
    
    public void run()
    {
        JMSConsumer listenerArtErase = context.createConsumer(Main.queueSistemProdavacArtikliErase, "idPro = "+idPro);
        JMSProducer pr = context.createProducer();
        System.out.println("Pokrenut brisac artikala Prodavca "+idPro);
        
        while(true)
        {
            
            System.out.println("Brisac "+idPro+" ceka na upit");
            //cekanje zahteva za broj artikala
            TextMessage artikli_erase;
            //synchronized(Main.class)
            
                artikli_erase = (TextMessage) listenerArtErase.receive();
            
            int t=0;
            int s=0;
            int idArt=0;
            int need_count=0;
            try {
                StringTokenizer st = new StringTokenizer(artikli_erase.getText());
                t = Integer.parseInt(st.nextToken());
                s = Integer.parseInt(st.nextToken());
                idFetcher = s;
                idArt = Integer.parseInt(st.nextToken());
                need_count = Integer.parseInt(st.nextToken());
            } catch (JMSException ex) {
                Logger.getLogger(TokenControl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (t==idPro)
            {
                System.out.println("Fetcheru "+idFetcher+" Prodavac "+idPro+" daje "+need_count+" artikala tipa "+idArt);
                        
            }
            //uzimanje artikala
            prodavac.uzmiArtikle(idArt, need_count);
            
            //slanje odgovora sistemu
            
            TextMessage art_erase_response = context.createTextMessage(idFetcher+" "+idPro+" "+idArt+" 1 ");
            try {
                art_erase_response.setIntProperty("idFetcher", idFetcher);
            } catch (JMSException ex) {
                Logger.getLogger(ArtiklStatisticar.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            pr.send(Main.queueProdavacSistemArtikliErase, art_erase_response);
            
        }
    }
}
