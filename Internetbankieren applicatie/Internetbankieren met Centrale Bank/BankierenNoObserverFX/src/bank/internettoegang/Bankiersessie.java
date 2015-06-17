package bank.internettoegang;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.centrale.ICentraleBank;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bankiersessie extends UnicastRemoteObject implements
        IBankiersessie, RemotePublisher {

    private static final long serialVersionUID = 1L;
    private long laatsteAanroep;
    private int reknr;
    private IBank bank;
    private BasicPublisher bp;

    public Bankiersessie(int reknr, IBank bank) throws RemoteException {
        laatsteAanroep = System.currentTimeMillis();
        this.reknr = reknr;
        this.bank = bank;
        bp = new BasicPublisher(new String[]{"bank"});
    }

    public boolean isGeldig() {
        return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
    }

    @Override
    public boolean maakOver(int bestemming, Money bedrag)
            throws NumberDoesntExistException, InvalidSessionException,
            RemoteException, AccessException {

        updateLaatsteAanroep();

        if (reknr == bestemming) {
            throw new RuntimeException(
                    "source and destination must be different");
        }
        if (!bedrag.isPositive()) {
            throw new RuntimeException("amount must be positive");
        }
        String best = Integer.toString(bestemming);
        String src = Integer.toString(reknr);
//        if (best.substring(0,1) != src.substring(0,1))
//        {
//            Registry r = java.rmi.registry.LocateRegistry.getRegistry("127.0.0.1", 1201);
//            try {
//                ICentraleBank cb = (ICentraleBank) r.lookup("centrale");
//                IBalie bal = cb.zoekBalie(Integer.parseInt(best.substring(0,1)));
//                IBankiersessie bs = (IBankiersessie) r.lookup(bestemming + "");
//                bs.informeer(this, bal.getBank());
//            } catch (NotBoundException ex) {
//                Logger.getLogger(Bankiersessie.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        boolean success = bank.maakOver(reknr, bestemming, bedrag);

        if (success) {
            bp.inform(this, "bank", null, this.bank);
        }

        return success;
    }

    private void updateLaatsteAanroep() throws InvalidSessionException {
        if (!isGeldig()) {
            throw new InvalidSessionException("session has been expired");
        }

        laatsteAanroep = System.currentTimeMillis();
    }

    @Override
    public IRekening getRekening() throws InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        return bank.getRekening(reknr);
    }

    @Override
    public void logUit() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeListener(listener, property);
    }
}
