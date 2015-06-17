/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aexbanner.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;

/**
 *
 * @author Gijs en Nadiv
 */
public interface IEffectenbeurs extends Remote
{
    public ArrayList<Fonds> getKoersen() throws RemoteException;
    public void setKoersen() throws RemoteException;
}
