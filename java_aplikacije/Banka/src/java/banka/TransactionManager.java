/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banka;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import posredniciBanka.TransactionRequest;

/**
 *
 * @author Pedja
 */
public class TransactionManager extends Thread{
    ArrayList<Banka> banke;
    JMSContext context;

    public TransactionManager(ArrayList<Banka> banke) {
        this.banke = banke;
        context = Main.connectionFactory.createContext();
    }
    
    public void run()
    {
        System.out.println("Pokrenut TrasactionManager");
        JMSConsumer listener = context.createConsumer(Main.queueSistemBankaRequestTransaction);
        JMSProducer answerer = context.createProducer();
        while(true)
        {
            System.out.println("TrasactionManager ceka na zahtev");
            
            boolean moze_transakcija = false;
            //cekanje na zahtev za transakcijom
            ObjectMessage transaction_request = (ObjectMessage) listener.receive();
            try {
                TransactionRequest tr = (TransactionRequest) transaction_request.getObject();
                int ukupan_iznos = tr.ukupan_iznos;
                for(Banka b: banke)
                {
                    if(b.idBan == tr.idBan)
                    {
                        Racun r = b.dohvatiRacun(tr.idRac);
                        if(!r.checkDovoljno(ukupan_iznos))
                        {
                            moze_transakcija = false;
                            TextMessage transaction_answer = context.createTextMessage("0");
                            transaction_answer.setIntProperty("idFetcher", tr.idFetcher);
                            answerer.send(Main.queueBankaSistemTransactionResult, transaction_answer);
                        }
                        else
                        {
                            moze_transakcija = true;
                            //obrada transakcija
                            
                            //uplate prodavcima
                            Banka privredna_banka=null;
                            for(Banka tracer :banke)
                            {
                                if(tracer.idBan==2)
                                {
                                    privredna_banka = tracer;
                                    break;
                                }
                            }
                            Racun racun_sistema = privredna_banka.dohvatiRacun(1337);
                            for (Integer key : tr.iznosi.keySet())
                            {
                                int vrednost = tr.iznosi.get(key).intValue();                              
                                Racun racun_prodavca = privredna_banka.dohvatiRacun(key.intValue());
                                racun_prodavca.uplati(vrednost);                                                                                                                                      
                            }
                            
                            //isplata sa racuna kupca
                            r.skini(ukupan_iznos);
                            racun_sistema.uplati(0.05*ukupan_iznos);
                            
                            TextMessage transaction_answer = context.createTextMessage("1");
                            transaction_answer.setIntProperty("idFetcher", tr.idFetcher);
                            answerer.send(Main.queueBankaSistemTransactionResult, transaction_answer);
                        }
                    }
                }
                
            } catch (JMSException ex) {
                Logger.getLogger(TransactionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
