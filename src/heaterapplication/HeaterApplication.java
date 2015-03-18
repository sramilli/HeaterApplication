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
    private static int STATUS = 18;
    private static int HEATER = 7;
    private static int GREEN = 23;
    private static int YELLOW = 25;
    private static int RED = 24;
    private static int MODE_SWITCH = 27;
    private static int MODE_SWITCH_PORT = 0;
    private static int MANUAL_THERMOSTAT = 22;
    private static int MANUAL_THERMOSTAT_PORT = 0;
        
    @Override
    public void startApp() {
        System.out.println("Starting HeaterApplication...");
        try {
            iThermostat = new Thermostat(MODE_SWITCH_PORT, MODE_SWITCH, MANUAL_THERMOSTAT_PORT, MANUAL_THERMOSTAT, STATUS, GREEN, YELLOW, RED, HEATER);
            //iThermostat.testRelay();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void destroyApp(boolean unconditional) {
        System.out.println("Destroying HeaterApplication...");
        try {if (iThermostat != null)
                        iThermostat.stop();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
