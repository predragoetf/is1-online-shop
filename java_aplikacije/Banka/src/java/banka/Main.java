/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banka;


import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 *
 * @author Pedja
 */
public class Main {

    @Resource(lookup ="java:comp/DefaultJMSConnectionFactory")
    public static ConnectionFactory connectionFactory;
    
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
    public static void main(String[] args) {        
        
        //pravljenje i popunjavanje Banaka
        Banka b1 = new Banka(1);
        b1.dodajRacun(1111, 10000);
        b1.dodajRacun(2222, 20000);
        b1.dodajRacun(3333, 30000);
        b1.dodajRacun(7777, 100);
        b1.dodajRacun(1234, 22000);
        
        Banka b2 = new Banka(2);//privredna banka
        b2.dodajRacun(1337, 0);//racun sistema
        b2.dodajRacun(1, 0);
        b2.dodajRacun(2, 0);
        b2.dodajRacun(3, 0);
        b2.dodajRacun(4, 0);
        b2.dodajRacun(5, 0);
        
        ArrayList<Banka> banke = new ArrayList<Banka>(Arrays.asList(b1, b2));
                       
        
        //pokretanje niti za obradu zahteva
        EcardChecker ec = new EcardChecker(banke);
        ec.start();
        TransactionManager tm = new TransactionManager(banke);
        tm.start();
    }
    
}
