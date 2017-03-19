package BadGuyChasesPlayer;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class BadGuy {
	
	Image myImage;
	int x=0,y=0;
	boolean hasPath=false;
	int cell_width;
	Stack<Cell> path = new Stack<>();

	public BadGuy( Image i, int x, int y, int width) {
		myImage = i;
		cell_width = width;
		this.x = x;
		this.y = y;
		
	}
	
	public void move() {
		// if there are cells in the path, move the the x,y coords of the next cell
		if (!path.empty()){
			Cell next = path.pop();
			x = next.x;
			y = next.y;
			next.rmPath();
		}
	}
	
	public void calculatePath(Cell[][] cells, int tx, int ty){
		
		// Clean up the red path from the bad guy to the player
		for (int x = 0; x < cells.length; x++){
			for (int y = 0; y < cells[0].length; y++){
				cells[x][y].rmPath();
			}
		}
		
		List<Cell> open = new ArrayList<>();
		List<Cell> closed = new ArrayList<>();
		Stack<Cell> path = new Stack<>();
		
		open.add(cells[this.x][this.y]);
		
		Cell lowest_f = open.get(open.size() - 1);
		Cell check;
		
		boolean target_reached = false;
		
		while (open.size() > 0){
			lowest_f = open.get(open.size() - 1);
			
			// get the cell with the lowest f value.
			// This should probably be upgraded to something a little more efficient than
			// binary search
			for(int i = open.size() - 1; i >= 0; i--){
				if(open.get(i).getF() < lowest_f.getF()){
					lowest_f = open.get(i);
				}
			}
					
			if(lowest_f.x == tx && lowest_f.y == ty){
				target_reached = true;
				break;
			}
			
			// loops through the 8 cells surrounding the current cell
			for (int xx = -1; xx <= 1; xx++){
				for (int yy = -1; yy <= 1; yy++){
					int x_ind = lowest_f.x + xx;
					int y_ind = lowest_f.y + yy;
					
					if(xx!=0 || yy!=0){
						
						// ignore if out of bounds
						if(x_ind >= 0 && y_ind >= 0 && x_ind < cells.length && y_ind < cells[0].length){
							check = cells[x_ind][y_ind];
							// ignore cell if wall, or closed
							if(!check.isWall() && !closed.contains(check)){
								
								// if open check for better routes
								if(open.contains(check)){
									check.updateBetterParent(lowest_f);
								} else {
								// if not open, open up this cell
									check.setParent(lowest_f);
									check.calcG();
									check.calcH(tx, ty);
									open.add(check);
								}
								
							}
						}
					}
				}
			}
			// close the current cell
			closed.add(lowest_f);
			open.remove(lowest_f);
		}
		
		// Builds a stack of cells that can be used to guide the bad guy to the player
		if (target_reached){
			lowest_f = cells[tx][ty];
			while(lowest_f.getParent() != null){
				lowest_f.setPath();
				path.add(lowest_f);
				lowest_f = lowest_f.getParent();
			}
		}
		
		// Reset parents
		for (int x = 0; x < cells.length; x++){
			for (int y = 0; y < cells[0].length; y++){
				cells[x][y].setParent(null);
			}
		}
		
		this.path = path;
	}
	
	public void paint(Graphics g) {
		g.drawImage(myImage, x*cell_width, y*cell_width, null);
	}
	
}
