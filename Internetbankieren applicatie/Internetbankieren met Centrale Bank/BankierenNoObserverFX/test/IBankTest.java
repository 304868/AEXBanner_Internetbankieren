/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bank.bankieren.Bank;
import bank.bankieren.IKlant;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.util.NumberDoesntExistException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Gijs
 */
public class IBankTest 
{  
    public Bank bank;
    public IKlant klant;
    public IKlant klant2;
    public IRekening rekening;
    public IRekening rekening2;
    public Money saldo;
    
    @Before
    public void setUp() 
    {
        bank = new Bank("ING");
    }
    
    @Test
    public void openRekeningTest()
    {
        assertEquals("Klantnaam is een lege String",-1,bank.openRekening("","Rijen"));
        assertEquals("Plaats is een lege String",-1,bank.openRekening("Gijs Hendrickx",""));
        assertEquals("Klantnaam en plaats zijn een lege String",-1,bank.openRekening("",""));
        assertNull(bank.getRekening(100000000));
        assertEquals("Rekening niet correct aangemaakt",100000000,bank.openRekening("Nadiv Tjong A Hung","Rotterdam"));
        rekening = bank.getRekening(100000000);
        assertEquals("Klant niet goed aangemaakt, naam niet correct.","Nadiv Tjong A Hung",rekening.getEigenaar().getNaam());
        assertEquals("Klant niet goed aangemaakt, plaats niet correct.","Rotterdam",rekening.getEigenaar().getPlaats());    
    }
    
    @Test 
    public void maakOverTest() throws NumberDoesntExistException {
        
        bank.openRekening("Gijs Hendrickx", "Rijen");
        bank.openRekening("Nadiv Tjong A Hung", "Rotterdam");
        
        try{
            bank.maakOver(100000000, 100000000, new Money(2000, Money.EURO));
            fail("Bron mag niet hetzelfde zijn als bestemming.");
        }
        catch(RuntimeException e) {
            
        }
        
        try{
            bank.maakOver(100000000, 100000001, new Money(-2000, Money.EURO));
            fail("Bedrag mag niet negatief zijn.");
        }
        catch(RuntimeException e){
            
        }
        
        try{
            bank.maakOver(15, 100000001, new Money(3000, Money.EURO));
            fail("Bron rekeningnummer moet bestaan.");
        }
        catch(NumberDoesntExistException e){
            
        }
        
        try{
            bank.maakOver(100000000, 15, new Money(3000, Money.EURO));
            fail("Bestemming rekeningnummer moet bestaan.");
        }
        catch(NumberDoesntExistException e){
            
        }
        
        assertFalse("Bronrekening gaat niet over kredietlimiet heen.", bank.maakOver(100000000, 100000001, new Money(20000, Money.EURO)));
        assertTrue("Bedrag niet succesvol overgemaakt.", bank.maakOver(100000000, 100000001, new Money(1000, Money.EURO)));
    }
    
}
