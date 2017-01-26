package com.gammarush.engine.math;

//HELPER MATH CLASS THAT ACCEPTS FLOATS INSTEAD OF DOUBLES

public class Mathf {
	
	public static float getDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	public static float getAngle(float x1, float y1, float x2, float y2) {
		float angle = (float) Math.atan2(y2 - y1, x2 - x1);
		angle *= (180.0f / Math.PI);
		if(angle < 0.0f) {
			angle += 360.0f;
		}
		return angle;
	}

}
