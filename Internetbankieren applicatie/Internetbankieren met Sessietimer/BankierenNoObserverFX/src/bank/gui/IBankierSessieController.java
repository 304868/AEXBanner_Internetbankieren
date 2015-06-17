/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.gui;

import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javafx.event.ActionEvent;

/**
 *
 * @author Nadiv
 */
public interface IBankierSessieController extends Remote {
    
    public void setApp(BankierClient application, IBalie balie, IBankiersessie sessie) throws RemoteException;
    
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException;
}
