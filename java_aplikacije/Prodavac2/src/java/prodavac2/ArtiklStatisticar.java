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
public class ArtiklStatisticar extends Thread{
    private JMSContext context;
    private int idPro;
    private int idFetcher;
    private Prodavac prodavac;
    
    
     public ArtiklStatisticar(int idPro, Prodavac prodavac)
    {
        this.idPro = idPro;
        this.prodavac = prodavac;
        context = Main.connectionFactory.createContext();
        idFetcher = 0;
        
    }
     
    public void run()
    {
        JMSConsumer listenerArtAsk = context.createConsumer(Main.queueSistemProdavacArtikliAsk, "idPro = "+idPro);
        JMSProducer pr = context.createProducer();
        System.out.println("Pokrenut statisticar Prodavca "+idPro);
        //JMSConsumer listenerArtAsk = context.createConsumer(Main.queueSistemProdavacArtikliAsk, "idPro = "+idPro);
        while(true)
        {
            
            System.out.println("Statisticar "+idPro+" ceka na upit");
            //cekanje zahteva za broj artikala
            TextMessage artikli_ask;
            //synchronized(Main.class)
            
                artikli_ask = (TextMessage) listenerArtAsk.receive();
            
            int t=0;
            int s=0;
            int idArt=0;
            int need_count=0;
            try {
                StringTokenizer st = new StringTokenizer(artikli_ask.getText());
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
                System.out.println("Fetcher "+idFetcher+" pita Prodavca "+idPro+" da li ima barem "+need_count+" artikala tipa "+idArt);
                        
            }
            //provera da li ima dovoljno artikala
            int success_code;
            if (prodavac.dovoljnoArtikala(idArt, need_count)) success_code = 1;
            else success_code = 0;
            //dohvatanje cene artikla
            int cena = prodavac.cena(idArt);
            
            //slanje odgovora sistemu
            System.out.println("Statisticar "+idPro+" salje Fetcheru "+idFetcher+" poruku sadrzaja: "+idFetcher+" "+idPro+" "+idArt+" "+success_code+" "+cena);
            TextMessage art_ask_response = context.createTextMessage(idFetcher+" "+idPro+" "+idArt+" "+success_code+" "+cena);
            try {
                art_ask_response.setIntProperty("idFetcher", idFetcher);
            } catch (JMSException ex) {
                Logger.getLogger(ArtiklStatisticar.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            pr.send(Main.queueProdavacSistemArtikliAsk, art_ask_response);
            
        }
    }
}
