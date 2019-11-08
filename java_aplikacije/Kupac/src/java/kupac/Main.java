/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kupac;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import posrednici.Element;

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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      
        
        //idA, n, idPro
        Element e1 = new Element(1, 5, 1);
        Element e2 = new Element(2, 3, 1);
        Element e3 = new Element(3, 4, 3);
        Element e4 = new Element(4, 3, 4);
        Element e5 = new Element(5, 2, 5);
        
        
        Element e6 = new Element(1, 100, 1);//nema ga dovoljno na stanju
        Element e7 = new Element(2, 5, 1);
        
        LinkedList<Element> l1 = new LinkedList<Element>(Arrays.asList(e1, e2, e3, e4));
        LinkedList<Element> l2 = new LinkedList<Element>(Arrays.asList(e4, e5));
        LinkedList<Element> l3 = new LinkedList<Element>(Arrays.asList(e5));
        LinkedList<Element> l4 = new LinkedList<Element>(Arrays.asList(e6));//kupac treba da dobije poruku da nema dovoljno artikala
        LinkedList<Element> l7 = new LinkedList<Element>(Arrays.asList(e7));
        LinkedList<Element> dugacka = new LinkedList<Element>(Arrays.asList(e1, e3, e4, e2));
        
        //konstruktor kupca: ime, prezime, lista artikala, broj racuna, banka
        Kupac k1 = new Kupac("Ana", "Aleksic", l1, "1111", 1);   
        //k1.start();
        
//        //DEBUG ONLY
//        Kupac k10 = new Kupac("Boze", "Pomozi", l3, "2222", 1);
//        k10.start();
        
        Kupac k2 = new Kupac("Branko",  "Bertic", l2, "2222", 1);
        //k2.start();
        
        Kupac k3 = new Kupac("Zeljko", "Zderonjic", l4, "3333", 1);//trazi previse artikala
        k3.start();
        
        Kupac k4 = new Kupac("Losa", "Losic", l1, "9999", 2);//nema karticu
        k4.setBezKartice(); 
        //k4.start();
        
        Kupac k5 = new Kupac("Losa2", "Losic2", l1, "9999", 2);//losa kartica 
        //k5.start();
        
        Kupac k6 = new Kupac("Siromah", "Siroma", l7, "7777", 1);//nema dovoljno novca na racunu
        //k6.start();
        
        Kupac k7 = new Kupac("Dragan", "Dragic", dugacka, "1234", 1);//ok
        //k7.start();
                
        
    }
    
}
