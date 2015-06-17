/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.centrale;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekeningTbvBank;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gijs
 */
public class CentraleBank extends UnicastRemoteObject implements ICentraleBank
{
    private ArrayList<IBalie> balies;
    
    public CentraleBank() throws RemoteException
    {
        balies = new ArrayList<>();
         try 
        {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1201);
            r.rebind("centrale", this);
        } 
        catch (RemoteException ex) 
        {
            System.out.println("Iets gaat goed mis");
            System.out.println(ex.getMessage());
        } 
    }
    
    @Override
    public IBalie zoekBalie(int nr) throws RemoteException
    {
        if(nr == 1)
        {
            for(IBalie b : balies)
            {
                if(b.getNameBank().equals("ING"))
                {
                    return b;
                }
            }
        }
        else if(nr == 2)
        {
            for(IBalie b : balies)
            {
                if(b.getNameBank().equals("RaboBank"))
                {
                    return b;
                }
            }
        }
        else if(nr == 3)
        {
            for(IBalie b : balies)
            {
                if(b.getNameBank().equals("SNS"))
                {
                    return b;
                }
            }
        }
        else if(nr == 4)
        {
            for(IBalie b : balies)
            {
                if(b.getNameBank().equals("ABN AMRO"))
                {
                    return b;
                }
            }
        }
        else if(nr == 5)
        {
            for(IBalie b : balies)
            {
                if(b.getNameBank().equals("ASN"))
                {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public boolean addBalie(IBalie balie)  throws RemoteException
    {
        for (IBalie b : balies)
        {
            if (b.getNameBank() == balie.getNameBank())
            {
                return false;
            }
        }
        balies.add(balie);
        return true;
    }

    @Override
    public boolean removeBalie(IBalie balie)  throws RemoteException
    {
        for (IBalie b : balies)
        {
            if (b.getNameBank() == balie.getNameBank())
            {
                balies.remove(b);
                return true;
            }
        }
        return false;
    }

    @Override
    public IRekeningTbvBank zoekRekening(int reknr)  throws RemoteException
    {
        return null;
    }
    
    public static void main(String[] args) throws RemoteException
    {
        CentraleBank cb = new CentraleBank();
        System.out.println("Centrale bank online");
    }
    
}
