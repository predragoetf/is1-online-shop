/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prodavac2;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 *
 * @author Pedja
 */
public class Main {
    @Resource(lookup ="java:comp/DefaultJMSConnectionFactory")
    public static ConnectionFactory connectionFactory;
    
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
    
    @Resource(lookup = "QueueSistemProdavacArtikliErase")
    public static Queue queueSistemProdavacArtikliErase;
    
    @Resource(lookup = "QueueProdavacSistemArtikliErase")
    public static Queue queueProdavacSistemArtikliErase;

    //@Resource(lookup = "TopicSistemProdavac")
    //public static Topic topicSistemProdavac;
    
    //@Resource(lookup = "TopicProdavacSistem")
    //public static Topic topicProdavacSistem;
    
    //@Resource(lookup = "TopicSistemProdavacRet")
    //public static Topic topicSistemProdavacRet;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Prodavac p1 = new Prodavac(1);
        Prodavac p2 = new Prodavac(2);
        Prodavac p3 = new Prodavac(3);
        Prodavac p4 = new Prodavac(4);
        Prodavac p5 = new Prodavac(5);
        
        p1.dodajArtikal(1, 10, 100);
        p1.dodajArtikal(2, 5, 200);
        p3.dodajArtikal(3, 10, 300);
        p4.dodajArtikal(4, 10, 400);
        p5.dodajArtikal(5, 10, 500);
        
        p1.pokreni();
        p2.pokreni();
        p3.pokreni();
        p4.pokreni();
        p5.pokreni();
    }
    
}
