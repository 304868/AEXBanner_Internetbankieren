/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.gui;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import fontys.observer.RemotePropertyListener;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author frankcoenen
 */
public class BankierSessieController extends UnicastRemoteObject implements Initializable, RemotePropertyListener, IBankierSessieController {

    @FXML
    private Hyperlink hlLogout;

    @FXML
    private TextField tfNameCity;
    @FXML
    private TextField tfAccountNr;
    @FXML
    private TextField tfBalance;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextField tfToAccountNr;
    @FXML
    private Button btTransfer;
    @FXML

    private TextArea taMessage;

    private BankierClient application;
    private IBalie balie;
    private IBankiersessie sessie;
    private Timer timer;

    public BankierSessieController() throws RemoteException {

    }

    @Override
    public void setApp(BankierClient application, IBalie balie, IBankiersessie sessie) throws RemoteException {
        this.balie = balie;
        this.sessie = sessie;
        this.application = application;
        this.timer = new Timer();
        IRekening rekening = null;
        try {
            rekening = sessie.getRekening();
            tfAccountNr.setText(rekening.getNr() + "");
            tfBalance.setText(rekening.getSaldo().getValue() + "");
            String eigenaar = rekening.getEigenaar().getNaam() + " te "
                    + rekening.getEigenaar().getPlaats();
            tfNameCity.setText(eigenaar);
            Registry r = java.rmi.registry.LocateRegistry.getRegistry("127.0.0.1", 1201);
            r.rebind(rekening.getNr() + "", sessie);
            timer.schedule(new PeriodiekeActie(this), 0, 1000);
        } catch (InvalidSessionException ex) {
            taMessage.setText("bankiersessie is verlopen");
            Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (RemoteException ex) {
            taMessage.setText("verbinding verbroken");
            Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void logout(ActionEvent event)
    {
        try 
        {
            sessie.logUit();
            application.gotoLogin(balie, "");
            this.balie.removeListener(this, null);
            this.sessie.removeListener(this.balie, null);
        } 
        catch (RemoteException e) 
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void transfer(ActionEvent event) {
        try {
            int from = Integer.parseInt(tfAccountNr.getText());
            int to = Integer.parseInt(tfToAccountNr.getText());
            if (from == to) {
                taMessage.setText("can't transfer money to your own account");
            }
            long centen = (long) (Double.parseDouble(tfAmount.getText()) * 100);
            sessie.maakOver(to, new Money(centen, Money.EURO));
        } catch (RemoteException e1) {
            e1.printStackTrace();
            taMessage.setText("verbinding verbroken");
        } catch (NumberDoesntExistException | InvalidSessionException e1) {
            e1.printStackTrace();
            taMessage.setText(e1.getMessage());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException
    {
        IBank tempBank = (IBank)evt.getNewValue();
        Platform.runLater(new Runnable()
                {
                    @Override
                    public void run() {
                        try 
                        {
                            tfBalance.setText(tempBank.getRekening(sessie.getRekening().getNr()).getSaldo().getValue());
                        } 
                        catch (InvalidSessionException | RemoteException ex)
                        {
                            Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });        
    }
    
    public IBankiersessie getSessie()
    {
        return this.sessie;
    }
    
    public IBalie getBalie()
    {
        return this.balie;
    }
    
    public void setWarningText()
    {
        this.taMessage.setText("Sessie is verlopen, ga terug en log opnieuw in.");
    }
}
