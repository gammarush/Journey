package com.gammarush.engine.graphics;

import com.gammarush.engine.math.line.LineSegment;
import com.gammarush.engine.math.vector.Vector2f;

//STATIC GRAPHICS CLASS TO RENDER SHAPES AND OTHER GRAPHICAL OPERATIONS

public class Graphic2D {
	
	//RENDER SINGLE PIXEL TO SCREEN
	public static void drawPixel(int x, int y, int color, Renderer renderer) {
		if(x < 0 || x >= renderer.width || y < 0 || y >= renderer.height) return;
		renderer.pixels[x + y * renderer.width] = color;
	}
	
	//SAME METHOD AS ABOVE WITH OPTIONAL TRANSPARENCY
	public static void drawPixel(int x, int y, int color, double alpha, Renderer renderer) {
		if(!renderer.useTransparency) {
			drawPixel(x, y, color, renderer);
			return;
		}
		if(x < 0 || x >= renderer.width || y < 0 || y >= renderer.height) return;
		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = (color) & 0xff;
		int old_color = renderer.pixels[x + y * renderer.width];
		int old_r = (old_color >> 16) & 0xff;
		int old_g = (old_color >> 8) & 0xff;
		int old_b = (old_color) & 0xff;
		int new_r = (int) ((r * alpha) + (old_r * (1.0 - alpha)));
		int new_g = (int) ((g * alpha) + (old_g * (1.0 - alpha)));
		int new_b = (int) ((b * alpha) + (old_b * (1.0 - alpha)));
		int new_color = ((new_r & 0x0ff) << 16) | ((new_g & 0x0ff) << 8) | (new_b & 0x0ff);
		renderer.pixels[x + y * renderer.width] = new_color;
	}
	
	//RENDER LINE TO SCREEN USING BRESENHAM'S LINE ALGORITHM
	public static void drawLine(int x1, int y1, int x2, int y2, int color, Renderer renderer) {
		if (x1 == x2 && y1 == y2) {
		   drawPixel(x1, y1, color, renderer);
		} else {           
		    int dx = Math.abs(x2 - x1);
		    int dy = Math.abs(y2 - y1);
		    int d = dx - dy;
		    int xp, yp;
		    if (x1 < x2) xp = 1; else xp = -1;
		    if (y1 < y2) yp = 1; else yp = -1;
		    while (x1 != x2 || y1 != y2) {
		        int p = 2 * d;
		        if (p > -dy) {
		            d -= dy;
		            x1 = x1 + xp;
		        }
		        if (p < dx) {
		            d += dx;
		            y1 = y1 + yp;
		        }
		        drawPixel(x1, y1, color, renderer);
		    }
		} 
		
	}
	
	//RENDER BASIC CIRCLE
	public static void drawCircle(int x1, int y1, int radius, int color, Renderer renderer) {
		for(int y = -radius; y <= radius; y++) {
		    for(int x = -radius; x <= radius; x++) {
		        if(x * x + y * y <= radius * radius) drawPixel(x1 + x, y1 + y, color, renderer);
		    }
		}
	}
	
	//RENDER AXIS ALIGNED RECTANGLE
	public static void drawRect(int x, int y, int width, int height, int color, Renderer renderer) {
		for(int yp = y; yp < y + height; yp++) {
			for(int xp = x; xp < x + width; xp++) {
				drawPixel(xp, yp, color, renderer);
			}
		}
	}
	
	//SAME METHOD AS ABOVE WITH OPTIONAL TRANSPARENCY
	public static void drawRect(int x, int y, int width, int height, int color, double alpha, Renderer renderer) {
		for(int yp = y; yp < y + height; yp++) {
			for(int xp = x; xp < x + width; xp++) {
				drawPixel(xp, yp, color, alpha, renderer);
			}
		}
	}
	
	//RENDER QUAD USING COORDINATES OF CORNERS
	public static void drawQuad(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int color, Renderer renderer) {
		LineSegment s1 = new LineSegment(x1, y1, x2, y2);
		LineSegment s2 = new LineSegment(x2, y2, x3, y3);
		LineSegment s3 = new LineSegment(x3, y3, x4, y4);
		LineSegment s4 = new LineSegment(x4, y4, x1, y1);
		int xmin = Math.min(Math.min(x1, x2), Math.min(x3, x4));
		int xmax = Math.max(Math.max(x1, x2), Math.max(x3, x4));
		int ymin = Math.min(Math.min(y1, y2), Math.min(y3, y4));
		int ymax = Math.max(Math.max(y1, y2), Math.max(y3, y4));
		for(int y = ymin; y < ymax; y++) {
			LineSegment line = new LineSegment(xmin, y, xmax, y);
			Vector2f range = line.getBetweenSides(s1, s2, s3, s4);
			for(int x = (int) range.x; x < (int) range.y; x++) {
				drawPixel(x, y, color, renderer);
			}
		}
	}
	
	//SAME METHOD AS ABOVE WITH OPTIONAL TRANSPARENCY
	public static void drawQuad(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int color, double alpha, Renderer renderer) {
		LineSegment s1 = new LineSegment(x1, y1, x2, y2);
		LineSegment s2 = new LineSegment(x2, y2, x3, y3);
		LineSegment s3 = new LineSegment(x3, y3, x4, y4);
		LineSegment s4 = new LineSegment(x4, y4, x1, y1);
		int xmin = Math.min(Math.min(x1, x2), Math.min(x3, x4));
		int xmax = Math.max(Math.max(x1, x2), Math.max(x3, x4));
		int ymin = Math.min(Math.min(y1, y2), Math.min(y3, y4));
		int ymax = Math.max(Math.max(y1, y2), Math.max(y3, y4));
		for(int y = ymin; y < ymax; y++) {
			LineSegment line = new LineSegment(xmin, y, xmax, y);
			Vector2f range = line.getBetweenSides(s1, s2, s3, s4);
			for(int x = (int) range.x; x < (int) range.y; x++) {
				drawPixel(x, y, color, alpha, renderer);
			}
		}
	}
	
	//POINTS TO FONT METHOD
	public static void drawString(String string, int x, int y, int scale, Renderer renderer) {
		renderer.font.drawString(string, x, y, scale, renderer);
	}
	
	//SAME METHOD AS ABOVE WITH OPTIONAL TRANSPARENCY
	public static void drawString(String string, int x, int y, int scale, float alpha, Renderer renderer) {
		renderer.font.drawString(string, x, y, scale, alpha, renderer);
	}
	
	//RENDER BEZIER CURVE TO SCREEN
	public static void drawBezierCurve(int x1, int y1, int x2, int y2, int x3, int y3, int color, Renderer renderer) {
		int last_x = x1, last_y = y1;

		for(float i = 0; i < 1; i += 0.01) {
			int xa = getBezierPoint(x1, x2, i);
			int ya = getBezierPoint(y1, y2, i);
			int xb = getBezierPoint(x2, x3, i);
			int yb = getBezierPoint(y2, y3, i);
			
			int x = getBezierPoint(xa, xb, i);
			int y = getBezierPoint(ya, yb, i);
			
			drawLine(x, y, last_x, last_y, color, renderer);
			last_x = x;
			last_y = y;
		}
	}
	
	private static int getBezierPoint(int a, int b, float p) {
		return (int) (a + (b - a) * p);
	}

}
