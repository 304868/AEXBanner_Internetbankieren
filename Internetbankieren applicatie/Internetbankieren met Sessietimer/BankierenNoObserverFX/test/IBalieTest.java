/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
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
public class IBalieTest 
{
    private IBalie balie;
    private IBank bank;
    private IBankiersessie bankiersessie;
    
    @Before
    public void setUp() throws RemoteException 
    {
        bank = new Bank("ING");
        balie = new Balie(bank);
    }
    
    @Test
    public void testOpenRekening()
    { 
        //   @return null zodra naam of plaats een lege string of wachtwoord minder dan 
        //   * vier of meer dan acht karakters lang is en anders de gegenereerde 
        //   * accountnaam(8 karakters lang) waarmee er toegang tot de nieuwe bankrekening
        //   * kan worden verkregen
        try 
        {      
            assertNull("Naam is lege String",balie.openRekening("","Rijen","wwwww"));
            assertNull("Plaats is lege String",balie.openRekening("Gijs Hendrickx","","wwwww"));
            assertNull("Wachtwoord te kort",balie.openRekening("Gijs Hendrickx","Rijen","www"));
            assertNull("Wachtwoord te lang",balie.openRekening("Gijs Hendrickx","Rijen","wwwwwwwww"));
        } 
        catch (Exception ex) 
        {
            assertTrue(ex instanceof RemoteException);
        }
    }
    
    @Test
    public void testLogIn()
    {
        //        @return de gegenereerde sessie waarbinnen de gebruiker 
        //   * toegang krijgt tot de bankrekening die hoort bij het betreffende login-
        //   * account mits accountnaam en wachtwoord matchen, anders null
        String s = null;
        try 
        {
            s = balie.openRekening("Gijs Hendrickx","Hendrickx","wwwww");
            assertNull("Wachtwoord incorrect",balie.logIn(s,"wwww"));
            assertNull("Accountnaam incorrect",balie.logIn("bla","wwwww"));
        } 
        catch (Exception ex) 
        {
            assertTrue(ex instanceof RemoteException);
        }
    }
}
