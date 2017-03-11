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
	
	//IMPLEMENTATION OF UNITYS SMOOTHDAMP METHOD IN JAVA
	public static float[] smoothDamp(float current, float target, float currentVelocity, float smoothTime) {
		float maxSpeed = Float.MAX_VALUE;
		float deltaTime = 0.0166666667f;
	    smoothTime = Math.max(0.0001f, smoothTime);
	    float num = 2f / smoothTime;
	    float num2 = num * deltaTime;
	    float num3 = 1f / (1f + num2 + 0.48f * num2 * num2 + 0.235f * num2 * num2 * num2);
	    float num4 = current - target;
	    float num5 = target;
	    float num6 = maxSpeed * smoothTime;
	    if(num4 < -num6) num4 = -num6;
	    if(num4 > num6) num4 = num6;
	    target = current - num4;
	    float num7 = (currentVelocity + num * num4) * deltaTime;
	    currentVelocity = (currentVelocity - num * num7) * num3;
	    float num8 = target + (num4 + num7) * num3;
	    if (num5 - current > 0f == num8 > num5) {
	        num8 = num5;
	        currentVelocity = (num8 - num5) / deltaTime;
	    }
	    return new float[] {num8, currentVelocity};
	}
	
	public static int sign(float x) {
		if(x < 0) return -1;
		if(x > 0) return 1;
		return 0;
	}

}
