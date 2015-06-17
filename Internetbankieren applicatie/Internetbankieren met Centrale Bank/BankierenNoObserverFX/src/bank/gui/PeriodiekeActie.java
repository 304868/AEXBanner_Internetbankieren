/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.gui;

import bank.internettoegang.Balie;
import java.rmi.RemoteException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Gijs
 */
public class PeriodiekeActie extends TimerTask
{   
    private final BankierSessieController controller;
    
    public PeriodiekeActie(BankierSessieController controller)
    {
        this.controller = controller;
    }
    
    @Override
    public void run() 
    {
        try 
        {
            if (!this.controller.getSessie().isGeldig())
            {
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        controller.setWarningText();
                    }
                }); 
                this.controller.getBalie().removeListener(this.controller, null);
                this.controller.getSessie().removeListener(this.controller.getBalie(), null);
             }
        } 
        catch (RemoteException ex) 
        {
            
        }
    }
    
}
