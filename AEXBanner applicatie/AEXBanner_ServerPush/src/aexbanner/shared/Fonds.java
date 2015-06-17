package aexbanner.shared;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author Gijs en Nadiv
 */
public class Fonds implements IFonds, Serializable {

    private String naam;
    private double koers;

    public Fonds(String naam, double koers) {
        this.naam = naam;
        this.koers = koers;
    }

    @Override
    public String getBeschrijving() {
        return this.naam + ": " + this.koers + "     ";
    }

    @Override
    public String getNaam() {
        return this.naam;
    }

    @Override
    public double getKoers() {
        return this.koers;
    }
    
    @Override
    public void setKoers(double koers)
    {
        this.koers=koers;
    }
}
