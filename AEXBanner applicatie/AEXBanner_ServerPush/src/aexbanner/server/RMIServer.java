package aexbanner.server;

import aexbanner.client.AEXBanner;
import aexbanner.client.BannerController;
import aexbanner.shared.IBannerController;
import aexbanner.shared.IEffectenbeurs;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.Scanner;
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
    private static final String bindingName = "ClientX";

    private IBannerController clientBannerController;

    private boolean correcteInvoer;

    private String ipAdres;
    private int portNummer;

    public RMIServer() {

        try {
            mock = new MockEffectenbeurs();
        } catch (RemoteException exc) {
            System.out.println(exc.getMessage());
        }

        correcteInvoer = false;

        while (!correcteInvoer) {
            try {
                Scanner input = new Scanner(System.in);
                System.out.print("Server: Voer het IP-adres van de client in: ");
                String ipAdres = input.nextLine();

                System.out.print("Server: Voer het poortnummer in: ");
                int portNummer = input.nextInt();

                this.registry = LocateRegistry.getRegistry(ipAdres, portNummer);
                
            } catch (java.rmi.UnknownHostException e) {
                System.out.println("Kan geen client vinden bij dit ip-adres en poortnummer.");
                System.out.println("Check of de invoer wel klopt.");
            } catch (ConnectException e) {
                System.out.println("Het duurt te lang om te verbinden.");
                System.out.println("Probeer het aub opnieuw.");
            } catch (InputMismatchException e) {
                System.out.println("Incorrecte invoer. Voer aub een geldige ip-adres en poort in.");
            } catch (RemoteException e) {
                Logger.getLogger(AEXBanner.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("Fout bij opzoeken van client: " + e.getMessage());
            }
        

            try {
                clientBannerController = (IBannerController) registry.lookup(bindingName);
                clientBannerController.setMockBeurs(mock);
                mock.addListener(clientBannerController, "koersen");
                correcteInvoer = true;
            } catch (NotBoundException ex) {
                
            } catch (AccessException ex) {
                
            } catch (java.rmi.UnknownHostException e) {
                System.out.println("Kan geen client vinden bij dit ip-adres en poortnummer.");
                System.out.println("Check of de invoer wel klopt.");
            } catch (ConnectException e) {
                System.out.println("Het duurt te lang om te verbinden.");
                System.out.println("Probeer het aub opnieuw.");
            } catch (RemoteException e) {
                System.out.println("Fout bij opzoeken van client: " + e.getMessage());
            }
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
