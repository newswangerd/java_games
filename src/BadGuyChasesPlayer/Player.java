package BadGuyChasesPlayer;


import java.awt.Graphics;
import java.awt.Image;


public class Player {

	Image myImage;
	int x=0,y=0;
	int xSpeed=0, ySpeed=0;
	int width;
	
	public Player( Image i, int x, int y, int cell_width ) {
		myImage=i;
		this.x = x;
		this.y = y;
		width = cell_width;
	}
	
	public void setXSpeed( int x ) {
		xSpeed=x;
	}
	
	public void setYSpeed( int y ) {
		ySpeed=y;
	}
	
	public void move(Cell map[][]) {
		int newx=x+xSpeed;
		int newy=y+ySpeed;
		if (!map[newx][newy].isWall()) {
			x=newx;
			y=newy;
		}
	}
	
	public void paint(Graphics g) {
		g.drawImage(myImage, x*width, y*width, null);
	}
	
}