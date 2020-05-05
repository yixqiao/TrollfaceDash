import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;

import processing.core.PApplet;
import processing.core.PImage;


 
public class MovingImage extends Rectangle2D.Double {
	
	// FIELDS
	public PImage image;
	
	// CONSTRUCTORS
	
	
	public MovingImage(PImage img, int x, int y, int w, int h) {
		super(x,y,w,h);
		image = img;
		image.resize(w, h);
	}
	
	
	// METHODS	
	public void moveToLocation(double x, double y) {
		super.x = x;
		super.y = y;
	}
	
	public void moveByAmount(double x, double y) {
		super.x += x;
		super.y += y;
	}
	
	public void applyWindowLimits(int windowWidth, int windowHeight) {
		x = Math.min(x,windowWidth-width);
		y = Math.min(y,windowHeight-height);
		x = Math.max(0,x);
		y = Math.max(0,y);
	}
	
	
	public void draw(PApplet marker) {
		marker.image(image, (float)((int)(this.getX())), (float)((int)(this.getY())));
	}
	
	public MovingImage act(ArrayList<MovingImage> list){
		return null;
	}
	
	public boolean isInWindow(){
		if(this.getX()<0 || this.getX()+this.getWidth()>=DrawingSurface.WIDTH ||this.getY()<0||this.getY()+this.getHeight()+this.getHeight()>=DrawingSurface.HEIGHT){
			return false;
		}else{
			return true;
		}
	}
}