package aexbanner.client;

import aexbanner.server.MockEffectenbeurs;
import aexbanner.shared.Fonds;
import aexbanner.shared.IEffectenbeurs;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gijs en Nadiv
 */
public class BannerController {

    private static final String bindingName = "MockEffectenbeurs";

    private IEffectenbeurs mockBeurs = null;
    private String returnstring = "";

    private Registry registry = null;

    private String ipAdres;
    private int portNummer;

    public BannerController(String ipAdres, int portNummer) throws RemoteException {
        this.ipAdres = ipAdres;
        this.portNummer = portNummer;

        try {
            this.registry = LocateRegistry.getRegistry(ipAdres, portNummer);
        } catch (RemoteException ex) {
            Logger.getLogger(BannerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            mockBeurs = (IEffectenbeurs) registry.lookup(bindingName);
        } catch (NotBoundException ex) {
            Logger.getLogger(BannerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(BannerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(null, e);
        }
        catch (ConnectException e){
            Logger.getLogger(AEXBanner.class.getName()).log(Level.SEVERE, null, e);
            throw new ConnectException(null, e);
        }
    }

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
}
