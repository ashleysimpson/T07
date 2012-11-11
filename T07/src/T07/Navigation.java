package T07;

import lejos.nxt.*;


public class Navigation {
	// initialize all variables, both class and instance variables
	private Odometer odometer;
	private TwoWheeledRobot robot;
	private double epsilon = 2.0, thetaEpsilon = 1.0;
	private boolean  isTurning = false;
	private double forwardSpeed = 5, rotationSpeed = 30;
	private USPoller rightSensor;
	private USPoller middleSensor;

	
		public Navigation(Odometer odometer, USPoller middleSensor, USPoller rightSensor) {
			// constructor
			this.odometer = odometer;
			this.robot = odometer.getTwoWheeledRobot();
			this.rightSensor = rightSensor;
			this.middleSensor = middleSensor;
		}
		
		public void travelTo(double x, double y) {
			// define minimal angle variable
			double minAng;
			// unless I have reached the target X and Y position
		    while (Math.abs(x - odometer.getX() ) > epsilon || Math.abs(y - odometer.getY() ) > epsilon ) {
		    	
		    	// avoid block checker (detects if an object is located in front of the robot and then moves into wall following mode)
		    	if (middleSensor.getFilteredData() < 30) { //TODO test the wall detection value so that robot doesn't hit wall
		    		avoidBlock();
		    	}
		    	
		    	// compute the turning angle
			    minAng = Math.toDegrees(Math.atan2(x - odometer.getX(),y - odometer.getY()));
			    minAng = Odometer.fixDegAngle(minAng);
			    // turn to that angle
			    turnTo(minAng);
			    // while turning, do not proceed
			    while (isTurning) {}
			    // set robot to move forward
			    robot.setForwardSpeed(forwardSpeed);
		    }
		    // now we have reached the final destination, we can stop and relax now
		    robot.setForwardSpeed(0);
		    // DEBUG: beep to signal that we have reached the final destination
		    Sound.beep();
		}
	
		public void turnTo(double angle) {
			// isTurning is a flag to indicate whether the robot is now turning
			isTurning = true;
			// if target angle is within 180 degree to the left of the current heading, make sure to turn to the right direction
			// this is to turn the minimal angle with the odometer given
			angle = Odometer.fixDegAngle(angle);		// takes care of negative angles
			if ((angle < odometer.getTheta() && Math.abs(odometer.getTheta() - angle) < 180) || 
					(angle > odometer.getTheta() && Math.abs(odometer.getTheta() - angle) > 180)){
				// before reaching the wanted angle, let's rotate it
				while (Math.abs(odometer.getTheta() - angle) > thetaEpsilon) {
					robot.setRotationSpeed(-rotationSpeed);
				}
			}
			else {
				// same idea with the one above, except in opposite direction
				while (Math.abs(odometer.getTheta() - angle) > thetaEpsilon) {
					robot.setRotationSpeed(rotationSpeed);
				}
			}
			// stop the robot
			robot.setRotationSpeed(0);
			// change the status of the flag
			isTurning = false;
		}
		
		public void avoidBlock() {
			
		}
	}