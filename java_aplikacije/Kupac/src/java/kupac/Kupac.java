/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kupac;

import posrednici.*;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.*;


/**
 *
 * @author Pedja
 */

public class Kupac extends Thread{
    
    private boolean bez_kartice;
    public String ime;
    public String prezime;
    private List<Element> elem_list;
    private String brracuna;
    private int idBan;
    JMSContext context;
    
    
    public Kupac(String ime, String prezime, List<Element>start_list)
    {
        bez_kartice = false;
        this.ime = ime;
        this.prezime = prezime;
        elem_list = start_list;
        context = Main.connectionFactory.createContext();
        
    }

    public Kupac(String ime, String prezime, List<Element> elem_list, String brracuna, int idBan) {
        bez_kartice = false;
        this.ime = ime;
        this.prezime = prezime;
        this.elem_list = elem_list;
        this.brracuna = brracuna;
        this.idBan = idBan;
        context = Main.connectionFactory.createContext();
        
    }
    
    public void setBezKartice()
    {
        bez_kartice = true;
    }
    
    
    public void addElem(Element e)
    {
        elem_list.add(e);
    }
    
    public void run()
    {
        //kreiranje korpe
        Korpa k;
        if(bez_kartice)
            k = new Korpa(elem_list, null);
        else
            k = new Korpa(elem_list, new Ekartica(ime, prezime, brracuna, idBan));
        
        //slanje korpe
        ObjectMessage message = context.createObjectMessage(k);
        JMSProducer producer = context.createProducer();
        producer.send(Main.queueKupacSistem, message);
        System.out.println("Kupac " +ime+" "+prezime+ " poslao korpu " +k.countid+" sistemu ");
        
        //osluskivanje odgovora
        boolean received_response = false;
        while(!received_response)
        {
            JMSConsumer listener = context.createConsumer(Main.topicSistemKupac);
            TextMessage tm = (TextMessage) listener.receive();
            try 
            {
                //tokenizacija i parsiranje poruke
                String text = tm.getText();
                StringTokenizer st = new StringTokenizer(text);
                String korpaid = st.nextToken();
                String message_code = st.nextToken();
                int code = Integer.parseInt(message_code);
            
                                 
                if(korpaid.contentEquals("Korpa"+k.countid))//poruka treba da se razmatra samo ako je za ovu korpu
                {
                    received_response = true;
                    String message_text;
                    switch(code)
                    {
                        case 1:
                            message_text = "Nema podataka o kartici, korpa se odbacuje!";
                            break;
                        case 2:
                            message_text = "Neispravna ekartica";
                            break;
                        case 3:
                            message_text = "Nema dovoljno artikala, morate pokusati ponovo kasnije";
                            break;
                        case 4:
                            message_text = "Nemate dovoljno novca na racunu";
                            break;
                        case 5:
                            message_text = "Kupovina uspesno obavljena";
                            break;
                        
                        default:
                            message_text = "Nepoznata poruka";
                    }
                    System.out.println("Odgovor kupcu korpe "+k.countid+": "+message_text);
                
                    //sad neki kod u zavisnosti od koda poruke, za lose napravljenu korpu npr. kraj izvrsavanja kupca
                }//end dobra korpa           
            }//end try 
            catch (JMSException ex) 
            {
                Logger.getLogger(Kupac.class.getName()).log(Level.SEVERE, null, ex);
            }       
        }//end while(!received_response)
    }
    
}
