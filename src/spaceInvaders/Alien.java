package spaceInvaders;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Alien extends Sprite2D {
	private List<String> sprites = new ArrayList<>();
	private Iterator<String> animation;
	private boolean alive = true;
	
	public Alien(String root_dir, Dimension window){
		super(root_dir + File.separator + "sprites" + File.separator + "alien_ship_1.png", window);
		this.sprites.add(root_dir + File.separator + "sprites" + File.separator + "alien_ship_1.png");
		this.sprites.add(root_dir + File.separator + "sprites" + File.separator + "alien_ship_2.png");
		
		this.animation = sprites.iterator();
		
		xspeed = 1;
	}

	public void moveDown(){
		this.y += this.SpriteImage.getHeight(null);
	}	
	
	public boolean isTouchingEdge(){
		
		// Alive aliens are the only ones allowed to touch the edge.
		if (this.x <= 0 && this.alive){
			return true;
		}
		
		if (this.x + this.SpriteImage.getHeight(null) >= window.width && this.alive){
			return true;
		}
		
		return false;
	}
	
	public void changeDirection(){
		this.xspeed = this.xspeed * -1;
	}
	
	public void animate(){
		this.set_image(animation.next());
		if (!this.animation.hasNext()){
			this.animation = sprites.iterator();
		}
	}
	
	public boolean is_alive(){
		return this.alive;
	}
	
	public void kill(){
		this.alive = false;
	}
	
	public void setXSpeed(int speed){
		this.xspeed = speed;
	}
}
