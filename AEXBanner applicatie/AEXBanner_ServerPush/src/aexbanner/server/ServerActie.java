package aexbanner.server;

import aexbanner.shared.IBannerController;
import aexbanner.shared.IEffectenbeurs;
import aexbanner.shared.IFonds;
import java.rmi.RemoteException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gijs
 */
public class ServerActie extends TimerTask {

    private IEffectenbeurs mock;

    public ServerActie(IEffectenbeurs mock) {
        this.mock = mock;
    }

    @Override
    public void run() {
        try {
            this.mock.setKoersen();
        } catch (RemoteException ex) {
            Logger.getLogger(ServerActie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
