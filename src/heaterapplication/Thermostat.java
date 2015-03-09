/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heaterapplication;

import java.io.IOException;
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
    private Led iGreenLED;
    private Led iRedLED;
    private Led iYellowLED;
    private Switch iSwitch;
    
    private int iSwitchPortID;
    private int iSwitchPinID;
    private int iGreenLEDPinNumber;
    private int iRedLEDPinNumber;
    private int iYellowLEDPinNumber;
    
    public static boolean ON = true;
    public static boolean OFF = false;
    
    public Thermostat(final int switchPortID, final int switchPinID, final int aGreenLEDPinNumber, final int aRedLEDPinNumber, final int aYellowLEDPinNumber) {
        this.iSwitchPortID = switchPortID;
        this.iSwitchPinID = switchPinID;
        this.iGreenLEDPinNumber = aGreenLEDPinNumber;
        this.iRedLEDPinNumber = aRedLEDPinNumber;
        this.iYellowLEDPinNumber = aYellowLEDPinNumber;
    }
    
    public void start() throws IOException {
        iGreenLED = new Led(iGreenLEDPinNumber);
        iRedLED = new Led(iRedLEDPinNumber);
        iYellowLED = new Led(iYellowLEDPinNumber);
        iSwitch = new Switch(iSwitchPortID, iSwitchPinID);
        iSwitch.setInputListener(this);
    }

    @Override
    public void valueChanged(PinEvent event) {
        GPIOPin tPin = event.getDevice();
        if (tPin == iSwitch.getPin()){
            if (event.getValue() == ON){  // pushing down
                try {
                    iGreenLED.turnOn();
                    iRedLED.turnOn();
                    iYellowLED.turnOn();
                    //iRedLED.turnOn();
                } catch (IOException ex) {
                    Logger.getLogger(Thermostat.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(event.getValue() == OFF){
                try {
                    iGreenLED.turnOff();
                    iRedLED.turnOff();
                    iYellowLED.turnOff();
                } catch (IOException ex) {
                    Logger.getLogger(Thermostat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void stop() throws IOException{
        if (iGreenLED != null){
            iGreenLED.close();
        }
        if (iRedLED != null){
            iRedLED.close();
        }
        if (iYellowLED != null){
            iRedLED.close();
        }
        if (iSwitch != null){
            iSwitch.close();
        }
    }
}
