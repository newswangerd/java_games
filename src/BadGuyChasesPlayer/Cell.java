package BadGuyChasesPlayer;

import java.awt.*;

public class Cell {
	private boolean is_wall = false;
	public int h = 0;
	public int g = 0;
	
	private Cell parent = null;
	
	public int x, y, width;
	private boolean isPath = false;
	
	public Cell(int x, int y, int width){
		/*
		 * x and y are the coordinates in the cells array, not the position on screen 
		 */
		this.x = x;
		this.y = y;
		this.width = width;
	}
	
	public boolean isWall(){
		return this.is_wall;
	}
	
	public void setWall(boolean state){
		this.is_wall = state;
	}
	
	
	public void flip(){
		// Toggles the cell from true to false or vice versa
		this.is_wall = !this.is_wall;
	}
	
	public void paint(Graphics g){
		if (is_wall){
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		// Set to red if this cell is on the path between the player and bad guy
		if (isPath){
			g.setColor(Color.RED);
		}
		
		g.fillRect(x * width, y * width, width, width);
	}
	
	// isPath is used to show the red line from the bad guy to the player
	public void setPath(){
		isPath = true;
	}
	
	public void rmPath(){
		isPath = false;
	}
	
	public void calcH(int targetX, int targetY){
		h = (Math.abs(x - targetX) + Math.abs(y - targetY)) * 10;
	}
	
	// Calculate the g value for this cell
	public void calcG(){
		// Note, parent must be set before calling this.
		int dx = Math.abs(parent.x - x);
		int dy = Math.abs(parent.y - y);
		
		if (dx == 1 && dy == 1){
			g = parent.g + 14;
		} else {
			g = parent.g + 10;
		}
	}
	
	// Determines if the new cell passed in is a better path.
	// if it is, set the current parent to the new cell
	public void updateBetterParent(Cell new_parent){
		Cell old_parent = parent;
		int old_g = g;
		
		parent = new_parent;
		calcG();
		if(old_g < g){
			parent = old_parent;
			g = old_g;
		}
	}
	
	public int getF(){
		return h + g;
	}
	
	public int getG(){
		return g;
	}
	
	public void setParent(Cell p){
		this.parent = p;
	}
	
	public Cell getParent(){
		return this.parent;
	}
}
