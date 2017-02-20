package spaceInvaders;

import java.awt.*;

public class Score {
	public String title;
	public int value, x, y;
	
	public Score(String title, int x, int y){
		this.title = title;
		this.x = x;
		this.y = y;
	}
	
	public void icreaseScore(){
		this.value ++;
	}
	
	public void paint(Graphics g){
		g.setColor(Color.WHITE);
		g.setFont(new Font("Consolas", Font.PLAIN, 20));
		String to_draw = this.title + ": " + this.value;
		g.drawString(to_draw, x, y);
	}
	
	public int getScore(){
		return this.value;
	}
	
	public void reset(){
		this.value = 0;
	}
}
