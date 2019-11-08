/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistem2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import posrednici.*;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.persistence.Query;
import perzistentne_klase.ArtikalB;
import perzistentne_klase.KorisnikB;
import perzistentne_klase.KorpaB;
import perzistentne_klase.ProdavacB;
import posredniciBanka.TransactionRequest;
/**
 *
 * @author Pedja
 */
public class Fetcher extends Thread{
    
    Message m;
    Korpa k;
    static int statcount=1;
    int counter;
    JMSContext context;
    JMSProducer asker;
    JMSConsumer listener;
    ArrayList<Integer> keys_held;
    
    public Fetcher(ObjectMessage m) 
    {
        keys_held = new ArrayList<Integer>();
        context = Main.connectionFactory.createContext();
        counter = statcount++;
        String idstring = "Fetcher"+counter;
        System.out.println(idstring);
        context.setClientID(idstring);
        this.m = m;
        try { 
            this.k = (Korpa)m.getObject();
        } catch (JMSException ex) {
            Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        //listener = context.createConsumer(Main.topicProdavacSistem);
        listener = context.createConsumer(Main.queueProdavacSistemTokenGive, "idFetcher="+counter, false);
        asker = context.createProducer();     
    }
    
 

    
    public static synchronized void takeKeys(Fetcher f)
    {
        System.out.println("Fetcher korpe "+f.k.countid+" usao u takeKeys");
        List<Element> list = f.k.sadrzaj;
        
        //formiranje liste potrebnih kljuceva
        LinkedList<Integer> requested_keys = new LinkedList<Integer>();
        System.out.print("Korpa " +f.k.countid+ " trazi kljuceve od prodavaca: ");
        for (Iterator<posrednici.Element> iterator = list.iterator(); iterator.hasNext();) {
            posrednici.Element next = iterator.next();
            int idPro = next.getIdPro();
            Integer container = new Integer(idPro);
            if(!requested_keys.contains(container))
            {
                requested_keys.add(container);
                int req = container.intValue();
                System.out.print(req+", ");
            }                     
        }
        System.out.println();
        //slanje zahteva za kljucevima prodavcima
        for (Iterator<Integer> iterator = requested_keys.iterator(); iterator.hasNext();) 
        {
            Integer next = iterator.next();
            int target = next.intValue();
            int sender = f.counter;
            TextMessage tm = f.context.createTextMessage(target+" "+sender);
            try 
            {
                tm.setIntProperty("idPro", target);
                System.out.println("Fetcher "+f.counter+" salje zahtev za kljuc prodavca "+target);
                f.asker.send(Main.queueSistemProdavacTokenAsk, tm);
            } 
            catch (JMSException ex) 
            {
                Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
            }                
        }
        
        
        while (!requested_keys.isEmpty()) 
        {
            try 
            {                
                TextMessage tm = (TextMessage)f.listener.receive();
                StringTokenizer st= new StringTokenizer(tm.getText());                
                int target = Integer.parseInt(st.nextToken());
                int sender = Integer.parseInt(st.nextToken());
                if (target==f.counter)
                {
                    System.out.println("Fetcher "+f.counter+" dobio kljuc od Prodavca "+sender);
                    Integer cont = new Integer(sender);
                    requested_keys.remove(cont);
                    f.keys_held.add(cont);
                }
            } catch (JMSException ex) {
                Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
            }                                   
        }
    }
    
    public void returnKeys()
    {
        for (Integer i : keys_held)
        {
            TextMessage tm = (TextMessage) context.createTextMessage(i.intValue()+" "+counter);
            try {
                tm.setIntProperty("idPro", i.intValue());
            } catch (JMSException ex) {
                Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Fetcher "+counter+" vraca kljuc prodavcu "+i.intValue());
            asker.send(Main.queueSistemProdavacTokenRet, tm);
        }
    }
    
    public boolean checkEcard()
    {
        //provera da li je dobra ekartica, salje se upit Banci za proveru
        System.out.println("Fetcher "+counter+" proverava karticu "+k.ekartica.brracuna+" u banci "+k.ekartica.idBan);
            boolean dobra_kartica = false;
            TextMessage ask_for_card_tm = (TextMessage) context.createTextMessage(counter+" "+k.ekartica.idBan+" "+k.ekartica.brracuna);
            asker.send(Main.queueSistemBankaCheckEcard, ask_for_card_tm);
            JMSConsumer listenerEcardCheck = context.createConsumer(Main.queueBankaSistemCheckEcard, "idFetcher = "+counter);
            TextMessage response = (TextMessage) listenerEcardCheck.receive();
            try {
                int check_ecard_answer = Integer.parseInt(response.getText());
                dobra_kartica = (check_ecard_answer==1);
                //System.out.println("Kartica");
            } catch (JMSException ex) {
                Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        if (dobra_kartica) System.out.println("Kartica "+k.ekartica.brracuna+" u banci "+k.ekartica.idBan+" je dobra");
        else System.out.println("Kartica "+k.ekartica.brracuna+" u banci "+k.ekartica.idBan+" nije dobra");
        return dobra_kartica;
    }
    
    public boolean tryTransaction()
    {
        boolean uspelo = false;
        
        return uspelo;
    }
    
    public void run()
    {
        System.out.println("Pokrenut fetcher broj "+counter);
        JMSProducer informer = context.createProducer();
        HashMap<Integer, Integer> price_lookup;
        //provera da li postoji ekartica u korpi
        if(k.ekartica == null)
        {
            
            TextMessage textm = context.createTextMessage();
            try {
                textm.setText("Korpa"+k.countid+" 1");// Nema podataka o kartici, korpa se odbacuje!");
            } catch (JMSException ex) {
                Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
            }
            informer.send(Main.topicSistemKupac, textm);
        }
        else//postoji podatak o ekartici u korpi
        {            
            //provera da li postoji kupac u KorisnikB sa ovom ekarticom
            
            boolean vec_postoji = false;
            boolean dobra_kartica = false;
            synchronized(Main.class)
            {
                Main.em.getTransaction().begin();
                
                Query query = Main.em.createNativeQuery("SELECT * FROM korisnikb WHERE BrRacuna = " + k.ekartica.brracuna, KorisnikB.class);           
                List<KorisnikB> rezultat = query.getResultList();
                
                if (!rezultat.isEmpty())
                {
                    vec_postoji = true;
                    dobra_kartica=true;
                    System.out.println("Korisnik sa racunom "+k.ekartica.brracuna+" vec postoji u bazi");
                }
                else
                {
                    System.out.println("Korisnik sa racunom "+k.ekartica.brracuna+" ne postoji u bazi");
                }
                                
                Main.em.getTransaction().commit();
                //Main.find(KorisnikB.class, 1337).setBrojNeuspesnih(noviBroj);
            }
                                              
            //provera da li je dobra ekartica, salje se upit Banci za proveru
            
            if(!vec_postoji)
            {
                dobra_kartica = checkEcard();//dajemo sansu dobra_kartica da postane true
                if (dobra_kartica){
                    //ubacivanje novog korisnika u bazu
                    synchronized(Main.class)
                    {
                        System.out.println("Ubacivanje korisnika racuna "+k.ekartica.brracuna);
                        Main.em.getTransaction().begin();

                        KorisnikB novi = new KorisnikB(k.ekartica.ime, k.ekartica.prezime, k.ekartica.idBan, Integer.parseInt(k.ekartica.brracuna));
                        
                        Main.em.persist(novi);                         
                        Main.em.getTransaction().commit();
                    }
                }
            }
            
            if (!dobra_kartica)//ako ni posle ovoga kartica nije dobra, treba obavestiti kupca i zavrsiti
                {
                TextMessage textm = context.createTextMessage();
                try 
                {
                    textm.setText("Korpa"+k.countid+" 2");// "Neispravna kartica";
                    informer.send(Main.topicSistemKupac, textm);
                } 
                catch (JMSException ex) 
                {
                Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }            
            else
            {
            
                
            //sinhrono atomicno uzimanje kljuceva, blokirajuce
            takeKeys(this);            
            
            //formiranje mape koja sadrzi podatke o cenama artikala
            price_lookup = new HashMap<Integer, Integer>();
            
            //ovde treba uraditi transformaciju korpe tako da artikli budu agregirani
            HashMap<Integer, Integer> owner = new HashMap<Integer, Integer>();
            HashMap<Integer, Integer> agregat = new HashMap<Integer, Integer>();
                for(Element e: k.sadrzaj)
                {
                    int broj = e.getN();
                    owner.put(e.getIdA(), e.getIdPro());
                    if (agregat.containsKey(e.getIdA()))
                    {
                        Integer iznos_do_sada = agregat.get(e.getIdA());
                        agregat.put(e.getIdA(), iznos_do_sada+broj);
                    }
                    else
                    {
                        agregat.put(e.getIdA(),broj);
                    }                                              
                }
            //sad se u agregat i owner nalaze dobre vrednosti
            
            
            //pitanje prodavaca za svaki od artikala, iterira se kroz agregat
            boolean ima_svih_dovoljno = true;
            JMSConsumer listenerStat = context.createConsumer(Main.queueProdavacSistemArtikliAsk, "idFetcher = "+counter);
            //for (Element e : k.sadrzaj)
            for(Integer key : agregat.keySet())
            {
                //int idArt = e.getIdA();
                int idArt = key;
                //int idPro = e.getIdPro();
                int idPro = owner.get(key).intValue();
                //int need_count = e.getN();
                int need_count = agregat.get(key).intValue();
                String sadrzaj_poruke = idPro+" "+counter+" "+idArt+" "+need_count;
                TextMessage tm = (TextMessage) context.createTextMessage(idPro+" "+counter+" "+idArt+" "+need_count);
                try {
                    tm.setIntProperty("idPro", idPro);
                    System.out.println("Postavljen int property na "+tm.getIntProperty("idPro"));
                } catch (JMSException ex) {
                    Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                }
                //slanje pitanja statisticara prodavca idPro za artikal idArt u broju need_count
                System.out.println("Fetcher "+counter+" salje statisticaru Prodavca "+idPro+" poruku sadrzaja "+sadrzaj_poruke);
                asker.send(Main.queueSistemProdavacArtikliAsk, tm);
                
                //cekanje na odgovor statisticara                            
                TextMessage stat_response = (TextMessage) listenerStat.receive();
                int breakpoint_var1=0;//DEBUG BREAKPOINT VARIABLE
                try {
                    System.out.println("Fetcher "+counter+" primio poruku sadrzaja "+stat_response.getText());
                } catch (JMSException ex) {
                    Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //parsiranje odgovora statisticara
                try {
                    StringTokenizer st = new StringTokenizer(stat_response.getText());
                    int target = Integer.parseInt(st.nextToken());
                    int sender = Integer.parseInt(st.nextToken());
                    int artikl = Integer.parseInt(st.nextToken());
                    int uspeh = Integer.parseInt(st.nextToken());
                    int cijena = Integer.parseInt(st.nextToken());
                    
                    if(uspeh!=1)//ako stigne odgovor da nema dovoljno nekog artikla, prekinuti obradu i javiti kupcu
                    {
                        ima_svih_dovoljno = false;
                        System.out.println("Statisticar "+sender+" javio Fetcheru "+target+" da nema dovoljno artikla "+artikl);
                        String sadrzaj_odgovora = "Korpa"+k.countid+" 3";
                        TextMessage textm = (TextMessage)context.createTextMessage();
                        textm.setText(sadrzaj_odgovora);
                        asker.send(Main.topicSistemKupac, textm);
                        break;
                    }                    
                    else//ako stigne odgovor da ima dovoljno proizvoda, sacuvati cenu u mapi
                    {
                        price_lookup.put(idArt, cijena);
                    }
                    
                } catch (JMSException ex) {
                    Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            boolean uspela_transakcija = false;
            if(ima_svih_dovoljno)//ako ima svih dovoljno, u price_lookup su cene artikala
            {
                
                int ukupna_cena = 0;
                HashMap<Integer, Integer> iznosi = new HashMap<Integer, Integer>();
                for(Element e: k.sadrzaj)
                {
                    int cena_stavke = e.getN()*price_lookup.get(new Integer(e.getIdA()));
                    
                    if (iznosi.containsKey(e.getIdPro()))
                    {
                        Integer iznos_do_sada = iznosi.get(e.getIdPro());
                        iznosi.put(e.getIdPro(), iznos_do_sada+cena_stavke);
                    }
                    else
                    {
                        iznosi.put(e.getIdPro(),cena_stavke);
                    }
                           
                    ukupna_cena += cena_stavke;
                }
                System.out.println("Ima dovoljno svih artikala iz korpe "+k.countid+", ukupna cena je "+ukupna_cena);
                
                //zahtev za transakcijom
                TransactionRequest tr = new TransactionRequest(counter, ukupna_cena, iznosi, Integer.parseInt(k.ekartica.brracuna), k.ekartica.idBan);
                ObjectMessage tr_om = context.createObjectMessage(tr);
                asker.send(Main.queueSistemBankaRequestTransaction, tr_om);
                
                //cekanje odgovora
                JMSConsumer listenerTransaction = context.createConsumer(Main.queueBankaSistemTransactionResult, "idFetcher = "+counter);
                TextMessage transaction_result_answer = (TextMessage)listenerTransaction.receive();
                
                try {
                    uspela_transakcija = (Integer.parseInt(transaction_result_answer.getText())==1);
                } catch (JMSException ex) {
                    Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                }
                //obrada odgovora
                if(!uspela_transakcija)
                {
                    System.out.println("Nije uspela transakcija Fetchera "+counter);
                    TextMessage textm = context.createTextMessage();
                    try {
                        textm.setText("Korpa"+k.countid+" 4");// "Nedovoljno novca na racunu"
                    } catch (JMSException ex) {
                        Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    informer.send(Main.topicSistemKupac, textm);                    
                }
                else{
                    System.out.println("Uspela transakcija Fetchera "+counter);
                    
                    JMSConsumer listenerEraser = context.createConsumer(Main.queueProdavacSistemArtikliErase, "idFetcher = "+counter);
                    //slanje zahteva za skidanje proizvoda sa stanja
                    for (Integer key: agregat.keySet())//prolazak kroz agregat 
                    {
                        int idArt = key;
                        int idPro = owner.get(key).intValue();
                        int need_count = agregat.get(key).intValue();
                        String sadrzaj_poruke = idPro+" "+counter+" "+idArt+" "+need_count;
                        TextMessage tm = (TextMessage) context.createTextMessage(idPro+" "+counter+" "+idArt+" "+need_count);
                        try {
                            tm.setIntProperty("idPro", idPro);
                            //System.out.println("Postavljen int property na "+tm.getIntProperty("idPro"));
                        } catch (JMSException ex) {
                        Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //slanje zahteva prodavcu idPro za artikal idArt u broju need_count
                        System.out.println("Fetcher "+counter+" salje brisacu Prodavca "+idPro+" poruku sadrzaja "+sadrzaj_poruke);
                        asker.send(Main.queueSistemProdavacArtikliErase, tm);
                        
                        //cekanje na odgovor brisaca                            
                        TextMessage stat_response = (TextMessage) listenerEraser.receive();
                        int breakpoint_var2=0;//DEBUG BREAKPOINT VARIABLE
                        try {
                            System.out.println("Fetcher "+counter+" primio odgovor na zahtev za brisanje sadrzaja "+stat_response.getText());
                        } catch (JMSException ex) {
                            Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        //informisanje kupca
                        
                        TextMessage textm = context.createTextMessage();
                        try {
                            textm.setText("Korpa"+k.countid+" 5");// "Kupovina uspesno izvrsena"
                        } catch (JMSException ex) {
                            Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        informer.send(Main.topicSistemKupac, textm);  
                        
                    }
                    //azuriranje racuna prodavaca
                    synchronized(Main.class)
                    {
                        Main.em.getTransaction().begin();
                        
                        for(Integer key: iznosi.keySet())
                        {
                            Query query = Main.em.createNativeQuery("SELECT * FROM prodavacb WHERE PIB = " + key.intValue(), ProdavacB.class);
                            List<ProdavacB> rezultat = query.getResultList();
                            double do_sada =  rezultat.get(0).getProdato();
                            rezultat.get(0).setProdato(do_sada+iznosi.get(key).intValue());
                            Main.em.persist(rezultat.get(0));
                        }                                               
                        
                        Main.em.getTransaction().commit();
                    }
                    
                }
                
                
            }//end ima svih dovoljno
            
            //perzistiranje korpe ako je dobra i azuriranje kupcevih uspelih i neuspelih pokusaja
            
            synchronized(Main.class)
            {
                Main.em.getTransaction().begin();
                    
                Query query = Main.em.createNativeQuery("SELECT * FROM korisnikb WHERE BrRacuna = " + k.ekartica.brracuna+" AND idBan = "+k.ekartica.idBan, KorisnikB.class);
                List<KorisnikB> rezultat = query.getResultList();
                KorisnikB nas_kupac = rezultat.get(0);
                    
                if (!uspela_transakcija)
                {
                    nas_kupac.setBrneusp(nas_kupac.getBrneusp()+1);
                    Main.em.persist(nas_kupac);
                }
                else
                {
                    nas_kupac.setBrusp(nas_kupac.getBrusp()+1);
                    Main.em.persist(nas_kupac);
                    
                    //perzistiranje korpe
                    KorpaB nova_korpa = new KorpaB(nas_kupac);
                    Main.em.persist(nova_korpa);
                    
                    //perzistiranje artikala iz agregata
                    for (Integer key : agregat.keySet())
                    {
                        Query q = Main.em.createNativeQuery("SELECT * FROM prodavacb WHERE PIB = " + owner.get(key), ProdavacB.class);
                        List<ProdavacB> rez = q.getResultList();
                        ProdavacB prodavac_artikla = rez.get(0);
                        ArtikalB art = new ArtikalB(key.intValue(), prodavac_artikla, nova_korpa, price_lookup.get(key), agregat.get(key));
                        Main.em.persist(art);
                    }
                    
                }
                    
                Main.em.getTransaction().commit();
            }
            
            
            
            returnKeys();
            }//end dobra_kartica
            
        }
        System.out.println("Zavrsen fetcher broj "+counter);
        try {
            m.acknowledge();
        } catch (JMSException ex) {
            Logger.getLogger(Fetcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
