package spaceInvaders;

import java.awt.Dimension;
import java.awt.Image;

public class Bullet extends Sprite2D {
	private boolean alive = true;
	
	public Bullet(String i, Dimension window){
		super(i, window);	
	}
	
	public void move(){
		if (this.y < 0){
			this.alive = false;
		}
		this.y -= 10;
	}
	
	public boolean is_alive(){
		return this.alive;
	}
}
