package client;

import java.util.Arrays;

import lejos.nxt.*;
import lejos.util.Timer;
import lejos.util.TimerListener;

// Class that controls the light sensors and makes sure that the data doesn't
// become confused when being called from multiple sources
public class USPoller implements TimerListener{
	
	// private variable that stores the controlled USValue
	private int rawLightValue = 0;
	private int firstOrderDerivative = 0;
	private int secondOrderDerivative = 0;
	private int previousValue = 0;
	private int previousFirstOrderDerivative = 0;
	private Timer lightPollerTimer;
	private final int DEFAULT_PERIOD_ULTRASONIC = 20; // Period for which the timerlistener will sleep (Adjust if necessary)
	private UltrasonicSensor usSensor;


	// Constructor of lightPoller
	public USPoller(UltrasonicSensor usSensor) {
	
		this.usSensor = usSensor;
		this.lightPollerTimer = new Timer(DEFAULT_PERIOD_ULTRASONIC, this);
		this.lightPollerTimer.start();
	}
	
	// timerListener method that controls access to the light sensor
	public void timedOut() {
		// add the newly read distance to replace the oldest element in the array
		previousFirstOrderDerivative = firstOrderDerivative;
		previousValue = rawLightValue;
		rawLightValue = usSensor.getDistance();
		firstOrderDerivative = rawLightValue - previousValue;
		secondOrderDerivative = firstOrderDerivative - previousFirstOrderDerivative;
		
	}
	
	public int getSecondOrderDerivative() {
		return secondOrderDerivative;
	}
	
	// getting that returns the raw light value
	public int getRawValue() {
		return rawLightValue;
	}	
}