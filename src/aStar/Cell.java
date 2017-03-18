package aStar;

import java.awt.*;

public class Cell {
	private boolean is_wall = false;
	public int h = 0;
	public int g = 0;
	
	private Cell parent = null;
	private boolean open = false;
	private boolean closed = false;
	private boolean is_target = false;
	private boolean is_start = false;
	private boolean is_path = false;
	
	public int x, y, width;
	
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
		
		if(open){
			g.setColor(Color.CYAN);
		}
		
		if(closed){
			g.setColor(Color.DARK_GRAY);
		}
		
		if (is_path){
			g.setColor(Color.RED);
		}
		
		if (is_target){
			g.setColor(Color.ORANGE);
		}
		
		if (is_start){
			g.setColor(Color.YELLOW);
		}
		
		g.fillRect(x * width, y * width, width, width);
	}
	
	public void setStart(){
		is_start = true;
	}
	
	public void setTarget(){
		is_target = true;
	}
	
	public void setPath(){
		is_path = true;
	}
	
	public void calcH(int targetX, int targetY){
		h = (Math.abs(x - targetX) + Math.abs(y - targetY)) * 10;
	}
	
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
	
	public void closeCell(){
		closed = true;
		open = false;
	}
	
	public void openCell(){
		closed = false;
		open = true;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	public boolean isClosed(){
		return closed;
	}
}
