package bank.bankieren;

import bank.centrale.ICentraleBank;
import bank.internettoegang.IBalie;
import fontys.util.*;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank implements IBank, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8728841131739353765L;
	private Map<Integer,IRekeningTbvBank> accounts;
	private Collection<IKlant> clients;
	private int nieuwReknr;
	private String name;
                  private ICentraleBank cb;

	public Bank(String name) throws RemoteException, UnknownHostException, NotBoundException {
		accounts = new HashMap<Integer,IRekeningTbvBank>();
		clients = new ArrayList<IKlant>();                              
		this.name = name;	
                                   if (this.getName().equals("ING"))
                                   {
                                        nieuwReknr = 100000000;
                                   }
                                   else if (this.getName().equals("RaboBank"))
                                   {
                                       nieuwReknr = 200000000;
                                   }
                                   else if (this.getName().equals("SNS"))
                                   {
                                       nieuwReknr = 300000000;
                                   }
                                   else if (this.getName().equals("ABN AMRO"))
                                   {
                                       nieuwReknr = 400000000;
                                   }
                                   else if (this.getName().equals("ASN"))
                                   {
                                       nieuwReknr = 500000000;
                                   }
                                   Registry r = java.rmi.registry.LocateRegistry.getRegistry("127.0.0.1",1201);
                                   cb = (ICentraleBank) r.lookup("centrale");
	}

	public int openRekening(String name, String city) {
		if (name.equals("") || city.equals(""))
			return -1;

		IKlant klant = getKlant(name, city);
		IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
		accounts.put(nieuwReknr,account);
		nieuwReknr++;
		return nieuwReknr-1;
	}

	private IKlant getKlant(String name, String city) {
		for (IKlant k : clients) {
			if (k.getNaam().equals(name) && k.getPlaats().equals(city))
				return k;
		}
		IKlant klant = new Klant(name, city);
		clients.add(klant);
		return klant;
	}

	public IRekening getRekening(int nr) {
		return accounts.get(nr);
	}

	public synchronized boolean maakOver(int source, int destination, Money money) 
			throws NumberDoesntExistException {
		if (source == destination)
			throw new RuntimeException(
					"cannot transfer money to your own account");
		if (!money.isPositive())
			throw new RuntimeException("money must be positive");

		IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
		if (source_account == null)
			throw new NumberDoesntExistException("account " + source
					+ " unknown at " + name);

		Money negative = Money.difference(new Money(0, money.getCurrency()),
				money);
		boolean success = source_account.muteer(negative);
		if (!success)
			return false;

		IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
		if (dest_account == null) { 
                                    String dest = Integer.toString(destination);
                                    String eersteCijfer = dest.substring(0,1);
                                    int zoekCijfer = 0;
                                    zoekCijfer = Integer.parseInt(eersteCijfer);
                                    IBalie ba = null;                                       
                                        try {
                                            ba = cb.zoekBalie(zoekCijfer);
                                        } catch (RemoteException ex) {
                                            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        try {
                                            dest_account = (IRekeningTbvBank) ba.getBank().getRekening(destination);
                                        } catch (RemoteException ex) {
                                            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                                        }                                
                                   if (dest_account == null)
                                   {
                                                throw new NumberDoesntExistException("account " + destination
					+ " unknown at " + name);
                                   }
                                    }
		success = dest_account.muteer(money);

		if (!success) // rollback
			source_account.muteer(money);
		return success;
	}

	@Override
	public String getName() {
		return name;
	}

}
