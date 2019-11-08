/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistem2;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import perzistentne_klase.ProdavacB;

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
    
    @Resource(lookup = "TopicSistemKupac")
    public static Topic topicSistemKupac;
    
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
    
    @Resource(lookup = "QueueSistemProdavacArtikliErase")
    public static Queue queueSistemProdavacArtikliErase;
    
    @Resource(lookup = "QueueProdavacSistemArtikliErase")
    public static Queue queueProdavacSistemArtikliErase;
    
    public static EntityManagerFactory emf;
    public static EntityManager em;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("Sistem2PU");
        em = emf.createEntityManager();
        
        /*
        synchronized(Main.class)
        {
            em.getTransaction().begin();
            
            ProdavacB p1 = new ProdavacB(1, "Maxi", 0);
            ProdavacB p2 = new ProdavacB(2, "CMarket", 0);
            ProdavacB p3 = new ProdavacB(3, "Idea", 0);
            ProdavacB p4 = new ProdavacB(4, "Metro", 0);
            ProdavacB p5 = new ProdavacB(5, "Tempo", 0);   
            
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.persist(p5);
            
            em.getTransaction().commit();
        }
                */
        // TODO code application logic here
        Sistem s = new Sistem();
        //poziv init za bazu!
        
        s.start();
    }
    
}
