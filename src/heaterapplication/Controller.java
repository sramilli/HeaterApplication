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
class Controller implements PinListener{
    private int iState;
    private Led iHeaterStatus;
    private Relay iHeaterRelay;
    private Led iLedGreen;
    private Led iLedYellow;
    private Led iLedRed;
    private Switch iManualThermostat;
    
    public static boolean ON = true;
    public static boolean OFF = false;
    
    public Controller(Led aHeaterStatus, Led aGreen, Led aYellow, Led aRed, Relay aRelay, Switch aManualThermostat){
        iState = 3;
        iHeaterStatus = aHeaterStatus;
        iHeaterRelay = aRelay;
        iLedGreen = aGreen;
        iLedYellow = aYellow;
        iLedRed = aRed;
        iManualThermostat = aManualThermostat;
        try {
            activateOutput();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int switchMode() throws IOException{
        //System.out.println("switching mode");
        if(iState >= 3){
            iState = 1;
        } else iState++;
        activateOutput();
        return iState;
    }
    
    private void activateOutput() throws IOException{
        switch (iState){
            case 1: 
                iHeaterStatus.turnOn();
                iHeaterRelay.turnOn();
                iLedGreen.turnOn();
                iLedYellow.turnOff();
                iLedRed.turnOff();
                break;
            case 2:
                iHeaterStatus.turnOff();
                iHeaterRelay.turnOff();
                iLedGreen.turnOff();
                iLedYellow.turnOn();
                iLedRed.turnOff();
                break;
            case 3:
                iHeaterStatus.turnOff();
                iHeaterRelay.turnOff();
                iLedGreen.turnOff();
                iLedYellow.turnOff();
                iLedRed.turnOn();
                break;
            default:
                System.out.println("This should never happen");
        }
    }

    private boolean bouncing = false;
    @Override
    public void valueChanged(final PinEvent event) {
        System.out.println("Switch heater relay!!!");
        if (!bouncing) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bouncing = true;
                    GPIOPin tPin = event.getDevice();
                    if (tPin == iManualThermostat.getPin()){
                        if (event.getValue() == ON){  // pushing down
                            System.out.println("Switch heater relay ON!!!");
                            try {
                                if (iState == 2){
                                    iHeaterRelay.turnOn();
                                }
                                Thread.sleep(600);
                            } catch (InterruptedException | IOException ex) {
                                Logger.getLogger(Thermostat.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            bouncing = false;
                        }else if (event.getValue() == OFF){
                            System.out.println("Switch heater relay OFF!!!");
                            try {
                                if (iState == 2){
                                    iHeaterRelay.turnOff();
                                }
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
    
}
