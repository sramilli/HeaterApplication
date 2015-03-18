/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heaterapplication;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.PinEvent;
import jdk.dio.gpio.PinListener;

/**
 *
 * @author Ste
 */
public class Thermostat implements PinListener{
    private Led iStatusLED;
    private Relay iHeaterRelay;
    private Led iGreenLED;
    private Led iYellowLED;
    private Led iRedLED;
    private Switch iModeSwitch;
    private Switch iManualTherostat;
    private Controller iController;
    
    public static boolean ON = true;
    public static boolean OFF = false;
    
    public Thermostat(int aModeSwitchPortID,int aModeSwitchPinID, int aManualThermostatPortID, int aManualThermostatPinID, int aStatusLEDPinNumber,int aGreenLEDPinNumber,int aYellowLEDPinNumber,int aRedLEDPinNumber, int aHeaterRELAYPinNumber) throws IOException {
        iStatusLED = new Led(aStatusLEDPinNumber);
        iHeaterRelay = new Relay(aHeaterRELAYPinNumber);
        iGreenLED = new Led(aGreenLEDPinNumber);
        iYellowLED = new Led(aYellowLEDPinNumber);
        iRedLED = new Led(aRedLEDPinNumber);
        iModeSwitch = new Switch(aModeSwitchPortID, aModeSwitchPinID);
        iModeSwitch.setInputListener(this);
        iManualTherostat = new Switch(aManualThermostatPortID, aManualThermostatPinID);
        iController = new Controller(iStatusLED, iGreenLED, iYellowLED, iRedLED, iHeaterRelay, iManualTherostat);
        iManualTherostat.setInputListener(iController);
    }
    
    public void testRelay(){
        for (int i=1; i<8; i++){
            try {   System.out.println("Turning on "+i);
                    iHeaterRelay.turnOn();
                    sleep(500);
                    System.out.println("Turning off "+i);
                    iHeaterRelay.turnOff();
                    sleep(500);
            } catch (IOException ex) {
                Logger.getLogger(Thermostat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Thermostat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean bouncing = false;
    @Override
    public void valueChanged(final PinEvent event) {
        if (!bouncing) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bouncing = true;
                    GPIOPin tPin = event.getDevice();
                    if (tPin == iModeSwitch.getPin()){
                        if (event.getValue() == ON){  // pushing down
                            try {
                                System.out.println("Switch MODE!!!");
                                iController.switchMode();
                                Thread.sleep(600);
                            } catch (InterruptedException | IOException ex) {
                                Logger.getLogger(Thermostat.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            bouncing = false;
                        }
                    }
                }
            }).start();
        }
    }
    
    public void stop() throws IOException{
        if (iStatusLED != null){
            iStatusLED.close();
        }
        if (iHeaterRelay != null){
            iHeaterRelay.close();
        }
        if (iGreenLED != null){
            iGreenLED.close();
        }
        if (iYellowLED != null){
            iYellowLED.close();
        }
        if (iRedLED != null){
            iRedLED.close();
        }
        if (iModeSwitch != null){
            iModeSwitch.close();
        }
        if (iManualTherostat != null){
            iManualTherostat.close();
        }
    }
}
