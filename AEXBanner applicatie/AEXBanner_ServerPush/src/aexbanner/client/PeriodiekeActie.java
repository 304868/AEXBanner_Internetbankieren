/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aexbanner.client;

import java.util.TimerTask;
import javafx.application.Platform;

/**
 *
 * @author Gijs en Nadiv
 */
public class PeriodiekeActie extends TimerTask
{
    private AEXBanner banner;
    
    public PeriodiekeActie(AEXBanner banner)
    {
        this.banner = banner;
    }
    
    @Override
    public void run() 
    {
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                banner.moveText();
            }         
        });
    }
    
}
