package T07;

import lejos.nxt.*;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class DPM {
	
	public enum USSensor {MIDDLE, RIGHT};
	public enum LSensor {LEFT, MIDDLE, RIGHT};
	static private final double LEFT_RADIUS = 12;
	static private final double RIGHT_RADIUS = 12;
	static private final double WIDTH = 12;
	static private final NXTRegulatedMotor LEFTMOTOR = Motor.A;
	static private final NXTRegulatedMotor RIGHTMOTOR = Motor.B;
	static private final LightSensor LEFT_LIGHT_SENSOR = new LightSensor(SensorPort.S1);
	static private final LightSensor RIGHT_LIGHT_SENSOR = new LightSensor(SensorPort.S2);
	static private final UltrasonicSensor MIDDLE_ULTRASONIC_SENSOR = new UltrasonicSensor(SensorPort.S3);
	static private final UltrasonicSensor RIGHT_ULTRASONIC_SENSOR = new UltrasonicSensor(SensorPort.S4);
	
	public static void main(String[] args){
		
		// Instantiate classes for basic components testing
		TwoWheeledRobot robot = new TwoWheeledRobot(LEFTMOTOR, RIGHTMOTOR, MIDDLE_ULTRASONIC_SENSOR, RIGHT_ULTRASONIC_SENSOR, 
				LEFT_LIGHT_SENSOR, RIGHT_LIGHT_SENSOR, LEFT_RADIUS, RIGHT_RADIUS, WIDTH);
		Odometer odo = new Odometer(robot);
		// Navigation navi = new Navigation(odo);
		
		int buttonChoice;
		RConsole.openBluetooth(20000);
		RConsole.println("Connected!");
		do {
			LCD.clear();
			
			LCD.drawString(" Left  |  Right ", 0, 0);
			LCD.drawString("                ", 0, 1);
			LCD.drawString("       To       ", 0, 2);
			LCD.drawString("      start     ", 0, 3);
			LCD.drawString("     program    ", 0, 4);
			
			buttonChoice = Button.waitForAnyPress();
			
		} while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
		
		USPoller usPoller = new USPoller(robot);
		LightPoller lp1 = new LightPoller(robot, LSensor.LEFT);
		LightPoller lp2 = new LightPoller(robot, LSensor.RIGHT);
		
		LCDInfo lcd = new LCDInfo(odo, usPoller, lp1, lp2);
		
		odo.timedOut();
		lcd.timedOut();	
		
		Navigation navi = new Navigation(odo, usPoller);
		LightLocalizer ll = new LightLocalizer (odo, lp1, lp2, navi);
		
		Delay.msDelay(1000);
		ll.doLocalization();
		
		// once the escape button is pressed, the robot will 
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}

}
