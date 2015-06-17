/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.Money;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gijs
 */
public class IBankiersessieTest 
{
    private IBalie balie;
    private IBank bank;
    private IBankiersessie bankiersessie;
    
    @Before
    public void setUp() throws RemoteException 
    {
        bank = new Bank("ING");
        balie = new Balie(bank);
        String s1 = balie.openRekening("Gijs Hendrickx","Rijen","wwwww");
        String s2 = balie.openRekening("Nadiv Tjong A Hung","Rotterdam","wwwww");
        bankiersessie = balie.logIn(s1,"wwwww");
    }
    
    @Test
    public void testIsGeldig() throws InterruptedException, RemoteException
    {
//        @returns true als de laatste aanroep van getRekening of maakOver voor deze
//	 *          sessie minder dan GELDIGHEIDSDUUR geleden is
//	 *          en er geen communicatiestoornis in de tussentijd is opgetreden, 
//	 *          anders false
        try 
        {
            bankiersessie.getRekening();            
        } 
        catch (InvalidSessionException | RemoteException ex) 
        {
            
        }
        Thread.sleep(61000);
         assertFalse(bankiersessie.isGeldig());
    }
    
    @Test
    (expected = RuntimeException.class)
    public void testMaakOver()
    {
        try 
        {
//            @param bestemming
//	 *            is ongelijk aan rekeningnummer van deze bankiersessie
            assertFalse(bankiersessie.maakOver(100000000,new Money(100,Money.EURO)));
            assertFalse(bankiersessie.maakOver(100000001,new Money(-100,Money.EURO)));
            assertTrue(bankiersessie.maakOver(100000001,new Money(100,Money.EURO)));
        } 
        catch (NumberDoesntExistException | InvalidSessionException | RemoteException ex) 
        {
            if (ex instanceof RemoteException == false)
            {
                assertNull(ex);
            }   
        }      
        try
        {
            assertFalse("Doelrekening niet bekend.",bankiersessie.maakOver(100000002,new Money(100,Money.EURO)));
        }
        catch (Exception ex)
        {
//            @throws NumberDoesntExistException
//	 *             als bestemming onbekend is
            assertTrue(ex instanceof NumberDoesntExistException);
        }
        try
        {
            bankiersessie.getRekening();
            Thread.sleep(61000);
            assertFalse("Sessie is niet meer geldig.",bankiersessie.maakOver(100000001,new Money(100,Money.EURO)));
        }
        catch (Exception ex)
        {
//            @throws InvalidSessionException
//	 *             als sessie niet meer geldig is 
            assertTrue(ex instanceof InvalidSessionException);
        }
    }
    
    @Test
    public void testGetRekening()
    {
        try 
        {
            bankiersessie.getRekening();
            Thread.sleep(61000);
            bankiersessie.getRekening();
        } 
        catch (Exception ex) 
        {
//            @throws InvalidSessionException
//	 *             als de sessieId niet geldig of verlopen is
            assertTrue(ex instanceof InvalidSessionException);
        }
    }
    
    @Test
    public void testLoguit()
    {
        try 
        {
            bankiersessie.logUit();
            //Als het goed is kan de sessie niet nog een keer uitgelogd worden.
            bankiersessie.logUit();
        } 
        catch (Exception ex) 
        {
            assertTrue(ex instanceof NoSuchObjectException);
        }       
    }
}
