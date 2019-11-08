/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistem2;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import posrednici.Korpa;

/**
 *
 * @author Pedja
 */
public class Sistem extends Thread{
    JMSContext context;
    
    public Sistem()
    {
        //context = Main.connectionFactory.createContext(JMSContext.CLIENT_ACKNOWLEDGE);
        context = Main.connectionFactory.createContext();
    }
    
    //TODO: neka funkcija za inicijalizaciju baze koja se pozove pre poziva run()
    
    public void run()
    {
        JMSConsumer consumer = context.createConsumer(Main.queueKupacSistem);
        //context.recover();
        while(true)
        {
            try 
            {
                ObjectMessage message = (ObjectMessage) consumer.receive();
                //context.recover
                Korpa k = (Korpa) message.getObject();
                System.out.println("Sistemu pristigla korpa "+k.countid+", predaje se Fetcher-u "+Fetcher.statcount);
                Fetcher f = new Fetcher(message);
                f.start();
            } 
            catch (JMSException ex) 
            {
                Logger.getLogger(Sistem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
