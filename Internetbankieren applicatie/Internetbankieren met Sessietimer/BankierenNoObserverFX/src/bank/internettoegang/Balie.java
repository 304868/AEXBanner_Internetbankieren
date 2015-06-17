package bank.internettoegang;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import bank.bankieren.*;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;
import java.beans.PropertyChangeEvent;

public class Balie extends UnicastRemoteObject implements IBalie, RemotePublisher {

    private static final long serialVersionUID = -4194975069137290780L;
    private IBank bank;
    private HashMap<String, ILoginAccount> loginaccounts;
    //private Collection<IBankiersessie> sessions;
    private java.util.Random random;
    private BasicPublisher bp;

    public Balie(IBank bank) throws RemoteException {
        this.bank = bank;
        loginaccounts = new HashMap<String, ILoginAccount>();
        //sessions = new HashSet<IBankiersessie>();
        random = new Random();
        bp = new BasicPublisher(new String[2]);
    }

    public String openRekening(String naam, String plaats, String wachtwoord) {
        if (naam.equals("")) {
            return null;
        }
        if (plaats.equals("")) {
            return null;
        }

        if (wachtwoord.length() < 4 || wachtwoord.length() > 8) {
            return null;
        }

        int nr = bank.openRekening(naam, plaats);
        if (nr == -1) {
            return null;
        }

        String accountname = generateId(8);
        while (loginaccounts.containsKey(accountname)) {
            accountname = generateId(8);
        }
        loginaccounts.put(accountname, new LoginAccount(accountname,
                wachtwoord, nr));

        return accountname;
    }

    public IBankiersessie logIn(String accountnaam, String wachtwoord)
            throws RemoteException
    {
        ILoginAccount loginaccount = loginaccounts.get(accountnaam);
        if (loginaccount == null) {
            return null;
        }
        if (loginaccount.checkWachtwoord(wachtwoord)) {
            IBankiersessie sessie = new Bankiersessie(loginaccount
                    .getReknr(), bank);
            
            sessie.addListener(this, "bank");

            return sessie;
        } else {
            return null;
        }
    }

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private String generateId(int x) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < x; i++) {
            int rand = random.nextInt(36);
            s.append(CHARS.charAt(rand));
        }
        return s.toString();
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeListener(listener, property);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        bp.inform(this, null, null, (IBank)evt.getNewValue());
    }

}
