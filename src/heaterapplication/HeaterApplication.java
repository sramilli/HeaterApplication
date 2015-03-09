/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heaterapplication;

import java.io.IOException;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Ste
 */
public class HeaterApplication extends MIDlet {
    
    Thermostat iThermostat;
    private static int GREEN = 23;
    private static int RED = 24;
    private static int YELLOW = 25;
    private static int MODE_SWITCH = 27;
    private static int MODE_SWITCH_PORT = 0;
        
    @Override
    public void startApp() {
        System.out.println("Starting HeaterApplication...");
        iThermostat = new Thermostat(MODE_SWITCH_PORT, MODE_SWITCH, GREEN, RED, YELLOW);
        try {
            iThermostat.start();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Destroying HeaterApplication...");
        try {
            iThermostat.stop();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
