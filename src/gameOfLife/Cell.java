package gameOfLife;

import java.awt.*;

public class Cell {
	public boolean alive = false;
	
	int x, y, width;
	
	public Cell(int x, int y, int width){
		/*
		 * x and y are the coordinates in the cells array, not the position on screen 
		 */
		this.x = x;
		this.y = y;
		this.width = width;
	}
	
	public boolean isAlive(){
		return this.alive;
	}
	
	public void setAlive(boolean state){
		this.alive = state;
	}
	
	public void update(int live_neighbors, boolean state){
		this.alive = state;
		if(state){
			// if less than 2 kill
			if (live_neighbors < 2){
				this.alive = false;
			
			// if more than  3 kill
			} else if(live_neighbors > 3){
				this.alive = false;
			}
			// if 2 or 3, do nothing
		} else {
			if (live_neighbors == 3){
				this.alive = true;
			}
		}
	}
	
	public void flip(){
		// Toggles the cell from true to false or vice versa
		this.alive = !this.alive;
	}
	
	public void paint(Graphics g){
		if (alive){
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		g.fillRect(x * width, y * width, width, width);
	}
}
