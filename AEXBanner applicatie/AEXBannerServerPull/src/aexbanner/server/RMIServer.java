package aexbanner.server;

import aexbanner.shared.IEffectenbeurs;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;

/**
 *
 * @author Gijs en Nadiv
 */
public class RMIServer {

    private Registry registry = null;
    private MockEffectenbeurs mock = null;
    private static final int portNumber = 667;
    private static final String bindingName = "MockEffectenbeurs";

    public RMIServer() {
        try {
            mock = new MockEffectenbeurs();
        } catch (RemoteException exc) {
            System.out.println(exc.getMessage());
        }

        try {
            registry = LocateRegistry.createRegistry(portNumber);
        } catch (RemoteException exc) {
            System.out.println(exc.getMessage());
        }

        try {
            registry.rebind(bindingName, mock);
        } catch (RemoteException exc) {
            System.out.println(exc.getMessage());
        }
    }

    public IEffectenbeurs getMock() {
        return this.mock;
    }

    public static void main(String[] args) {
        RMIServer server = new RMIServer();
        System.out.println("Server actief");
        Timer timer = new Timer();
        timer.schedule(new ServerActie(server.getMock()), 0, 4000);
    }
}
