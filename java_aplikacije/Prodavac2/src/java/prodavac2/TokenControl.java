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
public class TokenControl extends Thread{
    
    private JMSContext context;
    private int idPro;
    private JMSProducer producer;
    private JMSConsumer listener;
    private JMSConsumer listenerRet;
    private int idFetcher;
    private Prodavac prodavac;
    
    public TokenControl(int idPro, Prodavac prodavac)
    {
        this.idPro = idPro;
        this.prodavac = prodavac;
        context = Main.connectionFactory.createContext();
        context.setClientID("TokenControl"+idPro);
        producer = context.createProducer();
        listener = context.createConsumer(Main.queueSistemProdavacTokenAsk, "idPro = "+idPro);
        listenerRet = context.createConsumer(Main.queueSistemProdavacTokenRet, "idPro = "+idPro);
        idFetcher = 0;
        
    }
    
    public void run()
    {
        System.out.println("Pokrenut TokenControl za Prodavca "+idPro);
        while(true)
        {
            System.out.println("TokenControl Prodavca "+idPro+" ceka na zahtev za kljucem");
            boolean waiting_for_request = true;
            //cekanje na zahtev za token
            while(waiting_for_request)
            {
                TextMessage request = (TextMessage) listener.receive(); 
                try {
                    System.out.println("TokenControl Prodavca "+idPro+" primio poruku sadrzaja: "+request.getText());
                } catch (JMSException ex) {
                    Logger.getLogger(TokenControl.class.getName()).log(Level.SEVERE, null, ex);
                }
                StringTokenizer st;
                int a = 0;//DEBUG BREAKPOINT
                try 
                {
                    st = new StringTokenizer(request.getText());
                    int target = Integer.parseInt(st.nextToken());
                    int sender = Integer.parseInt(st.nextToken());
                    if (target==idPro)
                    {
                        waiting_for_request = false;
                        idFetcher = sender;
                        System.out.println("Prodavac "+idPro+" dobio zahtev za kljuc od Fetcher-a "+idFetcher);
                    }
                    
                } 
                catch (JMSException ex) 
                {
                    Logger.getLogger(TokenControl.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            //davanje tokena
            TextMessage tm = context.createTextMessage(idFetcher+" "+idPro);
            try {
                tm.setIntProperty("idFetcher", idFetcher);
                System.out.println("Prodavac "+idPro+" salje kljuc Fetcher-u "+idFetcher);
                producer.send(Main.queueProdavacSistemTokenGive, tm);
            } catch (JMSException ex) {
                Logger.getLogger(TokenControl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
            
            
            
            boolean waiting_for_token = true;
            //cekanje na povratak tokena
            while(waiting_for_token)
            {
                TextMessage returnToken = (TextMessage) listenerRet.receive();
                StringTokenizer st;
                try 
                {
                    st = new StringTokenizer(returnToken.getText());
                    int target = Integer.parseInt(st.nextToken());
                    int sender = Integer.parseInt(st.nextToken());
                    if ((target==idPro)&&(sender==idFetcher))
                    {
                        System.out.println("Fetcher "+idFetcher+" vratio prodavcu "+idPro+" kljuc");
                        waiting_for_token = false;
                        idFetcher = 0;
                    }
                } 
                catch (JMSException ex) 
                {
                    Logger.getLogger(TokenControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
