package spaceInvaders;

import java.awt.*;
import java.io.File;

import javax.swing.ImageIcon;

public class Sprite2D {

	protected double x, y;
	protected Image SpriteImage;
	protected Dimension window;
	protected int xspeed = 0;
	
	public Sprite2D(String i, Dimension window){
		this.window = window;
		this.set_image(i);
	}
	
	protected void set_image(String i){
		ImageIcon icon = new ImageIcon(i);
		this.SpriteImage = icon.getImage();
	}
	
	
	public void paint(Graphics g){
		g.drawImage(this.SpriteImage, (int)this.x, (int)this.y, null);
	}
	
	public void move(){
		this.x += xspeed;
	}
	
	public int get_width(){
		return this.SpriteImage.getWidth(null);
	}
	
	public int get_height(){
		return this.SpriteImage.getHeight(null);
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
}
