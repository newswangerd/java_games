package gameOfLife;

import java.awt.*;

public class Cell {
	private boolean alive = false;
	
	int x, y, width;
	
	public Cell(int x, int y, int width){
		/*
		 * x and y are the coordinates in the cells array, not the position on screen 
		 */
		this.x = x;
		this.y = y;
		this.width = width;
	}
	
	public void flip(){
		if (this.alive){
			this.alive = false;
		} else {
			this.alive = true;
		}
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
