/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cistac;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

/**
 *
 * @author Pedja
 */
public class Main {
    @Resource(lookup ="java:comp/DefaultJMSConnectionFactory")
    public static ConnectionFactory connectionFactory;
    
    //@Resource(lookup = "jms/QueueKupacSistem")
    //public static Queue queueKupacSistem;
    
    @Resource(lookup = "jmsQueue1")
    public static Queue queueKupacSistem;
    
    @Resource(lookup = "QueueSistemProdavacTokenAsk")
    public static Queue queueSistemProdavacTokenAsk;
    
    @Resource(lookup = "QueueSistemProdavacTokenRet")
    public static Queue queueSistemProdavacTokenRet;
    
    @Resource(lookup = "QueueProdavacSistemTokenGive")
    public static Queue queueProdavacSistemTokenGive;
    
    @Resource(lookup = "QueueSistemProdavacArtikliAsk")
    public static Queue queueSistemProdavacArtikliAsk;
    
    @Resource(lookup = "QueueProdavacSistemArtikliAsk")
    public static Queue queueProdavacSistemArtikliAsk;
    
    @Resource(lookup = "QueueSistemBankaCheckEcard")
    public static Queue queueSistemBankaCheckEcard;
    
    @Resource(lookup = "QueueBankaSistemCheckEcard")
    public static Queue queueBankaSistemCheckEcard;
    
    @Resource(lookup = "QueueSistemBankaRequestTransaction")
    public static Queue queueSistemBankaRequestTransaction;
    
    @Resource(lookup = "QueueBankaSistemTransactionResult")
    public static Queue queueBankaSistemTransactionResult;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JMSException {
        // TODO code application logic here
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer1 = context.createConsumer(queueKupacSistem);
        JMSConsumer consumer2 = context.createConsumer(queueSistemProdavacTokenAsk);
        JMSConsumer consumer3 = context.createConsumer(queueSistemProdavacTokenRet);
        JMSConsumer consumer4 = context.createConsumer(queueProdavacSistemTokenGive);
        JMSConsumer consumer5 = context.createConsumer(queueSistemProdavacArtikliAsk);
        JMSConsumer consumer6 = context.createConsumer(queueProdavacSistemArtikliAsk);
        JMSConsumer consumer7 = context.createConsumer(queueSistemBankaCheckEcard);
        JMSConsumer consumer8 = context.createConsumer(queueBankaSistemCheckEcard);
        JMSConsumer consumer9 = context.createConsumer(queueSistemBankaRequestTransaction);
        JMSConsumer consumer10 = context.createConsumer(queueBankaSistemTransactionResult);
        
        Message message=null;
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;
        int counter5 = 0;
        int counter6 = 0;
        int counter7 = 0;
        int counter8 = 0;
        int counter9 = 0;
        int counter10 = 0;
        

        for(int i = 0; i<100000; i++) 
        {            
            message = consumer1.receiveNoWait();
            if(message!=null)
                counter1++;
            
            message = consumer2.receiveNoWait();
            if(message!=null)
                counter2++;
            
            message = consumer3.receiveNoWait();
            if(message!=null)
                counter3++;
            
            message = consumer4.receiveNoWait();
            if(message!=null)
                counter4++;
            
            message = consumer5.receiveNoWait();
            if(message!=null)
            {
                System.out.println(message);
                counter5++;
            }
            
            message = consumer6.receiveNoWait();
            if(message!=null)
                counter6++;
            
            message = consumer7.receiveNoWait();
            if(message!=null)
                counter7++;
            
            message = consumer8.receiveNoWait();
            if(message!=null)
                counter8++;
            
            message = consumer9.receiveNoWait();
            if(message!=null)                            
                counter9++;
            
            
            message = consumer10.receiveNoWait();
            if(message!=null)
                counter10++;
            
        }
        System.out.println("Iz queueKupacSistem obrisano "+counter1+" poruka");
        System.out.println("Iz queueSistemProdavacTokenAsk obrisano "+counter2+" poruka");
        System.out.println("Iz queueSistemProdavacTokenRet obrisano "+counter3+" poruka");
        System.out.println("Iz queueProdavacSistemTokenGive obrisano "+counter4+" poruka");
        System.out.println("Iz queueSistemProdavacArtikliAsk obrisano "+counter5+" poruka");
        System.out.println("Iz queueProdavacSistemArtikliAsk obrisano "+counter6+" poruka");
        System.out.println("Iz queueSistemBankaCheckEcard obrisano "+counter7+" poruka");
        System.out.println("Iz queueBankaSistemCheckEcard obrisano "+counter8+" poruka");
        System.out.println("Iz queueSistemBankaRequestTransaction obrisano "+counter9+" poruka");
        System.out.println("Iz queueBankaSistemTransactionResultd obrisano "+counter10+" poruka");
        
        
        }
    
}
