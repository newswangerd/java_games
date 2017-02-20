package spaceInvaders;

import java.awt.Dimension;
import java.awt.Image;

public class SpaceShip extends Sprite2D{

	public SpaceShip(String i, Dimension window){
		super(i, window);
		xspeed=0;
	}
	
	public void setXSpeed(int x){
		this.xspeed = x;
	}
	
}
