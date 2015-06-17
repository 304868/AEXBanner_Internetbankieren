/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aexbanner.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Gijs
 */
public interface IFonds extends Remote
{
    public String getNaam() throws RemoteException;
    public double getKoers()throws RemoteException;
    public String getBeschrijving() throws RemoteException;
    public void setKoers(double koers) throws RemoteException;
}
