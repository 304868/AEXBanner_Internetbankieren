package aexbanner.server;

import aexbanner.shared.Fonds;
import aexbanner.shared.IEffectenbeurs;
import aexbanner.shared.IFonds;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gijs en Nadiv
 */
public final class MockEffectenbeurs extends UnicastRemoteObject implements IEffectenbeurs, RemotePublisher {

    private final ArrayList<Fonds> koersen;
    private final double rangeMin = -100.00;
    private final double rangeMax = 100.00;
    private BasicPublisher publisher;
    
    public MockEffectenbeurs() throws RemoteException {
        koersen = new ArrayList<>();
        koersen.add(new Fonds("Sony", generateRandom()));
        koersen.add(new Fonds("Acer", generateRandom()));
        koersen.add(new Fonds("Apple", generateRandom()));
        koersen.add(new Fonds("Durex", generateRandom()));
        koersen.add(new Fonds("Coca-cola", generateRandom()));
        koersen.add(new Fonds("Tori Inc.", generateRandom()));
        
        String[] listenableProps = new String[1];
        listenableProps[0] = "koersen";
        publisher = new BasicPublisher(listenableProps);
    }

    @Override
    public ArrayList<Fonds> getKoersen() {
        return this.koersen;
    }
    
    public BasicPublisher getPublisher(){
        return this.publisher;
    }
    
    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException{
        publisher.addListener(listener, property);
    }
    
    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException{
        publisher.removeListener(listener, property);
    }

    @Override
    public void setKoersen() {
        for (IFonds f : this.getKoersen()) {
            try {
                f.setKoers(generateRandom());
            } catch (RemoteException ex) {
                Logger.getLogger(MockEffectenbeurs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        publisher.inform(this, "koersen", null, this);
    }

    public double generateRandom() {
        Random random = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        randomValue = Math.floor(randomValue * 100) / 100;
        return randomValue;
    }
}
