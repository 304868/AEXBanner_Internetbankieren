/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aexbanner.shared;

import fontys.observer.RemotePropertyListener;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Nadiv
 */
public interface IBannerController extends Remote, RemotePropertyListener {
    public String createString() throws RemoteException;
    public IEffectenbeurs getMockBeurs() throws RemoteException;
    public void setMockBeurs(IEffectenbeurs mockBeurs) throws RemoteException;
}
