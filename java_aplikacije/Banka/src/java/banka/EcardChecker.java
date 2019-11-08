/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banka;

import java.util.ArrayList;
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
public class EcardChecker extends Thread{
    
    ArrayList<Banka> banke;
    JMSContext context;
    
    public EcardChecker(ArrayList<Banka> banke)
    {
        this.banke = banke;
        context = Main.connectionFactory.createContext();
    }
    
    public void run()
    {
        System.out.println("Pokrenut EcardChecker");
        JMSConsumer listener = context.createConsumer(Main.queueSistemBankaCheckEcard);
        JMSProducer answerer = context.createProducer();
        while(true)
        {
            System.out.println("EcardChecker ceka na zahtev");
            
            //cekanje na zahtev
            TextMessage tm = (TextMessage) listener.receive();
            try {
                System.out.println("EcardChecker dobio poruku sadrzaja "+tm.getText());
            } catch (JMSException ex) {
                Logger.getLogger(EcardChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                StringTokenizer st = new StringTokenizer(tm.getText());
                int idFetcher = Integer.parseInt(st.nextToken());
                int idBan = Integer.parseInt(st.nextToken());
                int idRac = Integer.parseInt(st.nextToken());
                boolean nasao = false;
                for (Banka b: banke)
                {
                    if (b.idBan == idBan)//ako nadje banku
                    {
                        Racun r = b.dohvatiRacun(idRac);
                        if (r != null) nasao = true;
                        else nasao = false;
                    }
                }
                String response;
                if(nasao) response ="1";
                else response ="0";
                TextMessage resp_tm = context.createTextMessage(response);
                resp_tm.setIntProperty("idFetcher", idFetcher);
                System.out.print("EcardChecker salje Fetcheru "+idFetcher+" ");
                if(nasao) System.out.println("potvrdan odgovor");
                else System.out.println("odrican odgovor");
                answerer.send(Main.queueBankaSistemCheckEcard, resp_tm);
                
                
            } catch (JMSException ex) {
                Logger.getLogger(EcardChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
