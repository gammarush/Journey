package com.gammarush.engine.math.line;

import com.gammarush.engine.math.vector.*;

//LINE SEGMENT CLASS USED BY GRAPHIC2D DRAW METHODS

public class LineSegment {
	public Vector2f a;
	public Vector2f b;
	
	public LineSegment(float x1, float y1, float x2, float y2) {
		this.a = new Vector2f(x1, y1);
		this.b = new Vector2f(x2, y2);
	}
	
	public LineSegment(Vector2f a, Vector2f b) {
		this.a = a;
		this.b = b;
	}
	
	public LineSegment(Vector2i a, Vector2i b) {
		float ax = a.x * 1.0f;
		float ay = a.y * 1.0f;
		float bx = b.x * 1.0f;
		float by = b.y * 1.0f;
		this.a = new Vector2f(ax, ay);
		this.b = new Vector2f(bx, by);
	}
	
	public Vector2f getIntersection(LineSegment l) {
		float min = -Float.MAX_VALUE;
		float x = min;
		float y = min;
		
	    float sx1 = b.x - a.x;     
	    float sy1 = b.y - a.y;
	    float sx2 = l.b.x - l.a.x;     
	    float sy2 = l.b.y - l.a.y;
	    
	    float s = (-sy1 * (a.x - l.a.x) + sx1 * (a.y - l.a.y)) / (-sx2 * sy1 + sx1 * sy2);
	    float t = ( sx2 * (a.y - l.a.y) - sy2 * (a.x - l.a.x)) / (-sx2 * sy1 + sx1 * sy2);

	    if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
	    	x = a.x + (t * sx1);
	        y = a.y + (t * sy1);
	    }
	    Vector2f v = null;
	    if(x != min && y != min) {
	    	v = new Vector2f(x, y);
	    }
		return v;
	}
	
	public Vector2f getBetweenSides(LineSegment s1, LineSegment s2, LineSegment s3, LineSegment s4) {
		float min = -Float.MAX_VALUE;
		float max = Float.MAX_VALUE;
		Vector2f p1, p2, p3, p4;
		p1 = getIntersection(s1);
		p2 = getIntersection(s2);
		p3 = getIntersection(s3);
		p4 = getIntersection(s4);
		float xmax = min;
		float xmin = max;
		if(p1 != null) {
			float x = p1.x;
			if(x > xmax) xmax = x;
			if(x < xmin) xmin = x;
		}
		if(p2 != null) {
			float x = p2.x;
			if(x > xmax) xmax = x;
			if(x < xmin) xmin = x;
		}
		if(p3 != null) {
			float x = p3.x;
			if(x > xmax) xmax = x;
			if(x < xmin) xmin = x;
		}
		if(p4 != null) {
			float x = p4.x;
			if(x > xmax) xmax = x;
			if(x < xmin) xmin = x;
		}
		if(xmax == min || xmin == max) {
			return null;
		}
		return new Vector2f(xmin, xmax);
	}
	
}
