/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.centrale;

import bank.bankieren.IBank;
import bank.bankieren.IRekeningTbvBank;
import bank.internettoegang.IBalie;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Gijs
 */
public interface ICentraleBank extends Remote
{
    /**
     * Zoekt in de lijst van banken naar de bank die het meegeleverde rekeningnummer beheert.
     * @param reknr >= 100000000, het rekeningnummer van de tegenpartij.
     * @return de balie die bij het rekeningnummer hoort
     */
    public IBalie zoekBalie(int reknr) throws RemoteException;
    
    /**
     * Zodra een bank online komt moet hij registreerd worden bij de centrale bank, deze methode zorgt ervoor dat dat gebeurt.
     * @param balie die online gaat
     * @return true als bank succesvol is toegevoegd, anders false.
     */
    public boolean addBalie(IBalie balie) throws RemoteException;
    
    /**
     * Bank wordt verwijderd zodra bank offline gaat.
     * @param balie die offline gaat
     * @return true als bank succesvol is verwijderd, anders false.
     */
    public boolean removeBalie(IBalie balie) throws RemoteException;
    
    /**
     * Creeert een IRekeningTbvBank object aan de hand van het meegegeven rekeningnummer.
     * @param reknr > 100000000, het meegegeven rekeningnummer
     * @return rekening die bij het rekeningnummer hoort.
     */
    public IRekeningTbvBank zoekRekening(int reknr) throws RemoteException;
}
