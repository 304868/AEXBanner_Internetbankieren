package aexbanner.client;

import aexbanner.server.MockEffectenbeurs;
import aexbanner.shared.Fonds;
import aexbanner.shared.IBannerController;
import aexbanner.shared.IEffectenbeurs;
import fontys.observer.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gijs en Nadiv
 */
public class BannerController extends UnicastRemoteObject implements IBannerController, RemotePropertyListener {

    private IEffectenbeurs mockBeurs = null;
    private String returnstring = "";

    public BannerController() throws RemoteException {
        
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException{
        mockBeurs = (IEffectenbeurs) evt.getNewValue();
    }
    
    @Override
    public String createString() {
        try {
            returnstring = "";
            for (Fonds f : mockBeurs.getKoersen()) {
                returnstring += f.getBeschrijving();
            }
            return returnstring;
        } catch (RemoteException e) {
            Logger.getLogger(BannerController.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e.getMessage());
        }
        return "";
    }
    
    @Override
    public IEffectenbeurs getMockBeurs(){
        return this.mockBeurs;
    }
    
    @Override
    public void setMockBeurs(IEffectenbeurs mockBeurs){
        this.mockBeurs = mockBeurs;
    }
}
