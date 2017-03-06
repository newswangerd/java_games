package gameOfLife;

import java.awt.*;

public class Button {

	private static Font FONT = new Font("Consolas", Font.PLAIN, 20);
	private String text;
	private int x, y, w, h;
	private int padding = 5;
	
	public Button(String text, int x, int y, Graphics g){
		this.text = text;
		this.x = x;
		this.y = y;
		
		this.w = g.getFontMetrics(FONT).stringWidth(text) + padding * 2;
		this.h = g.getFontMetrics(FONT).getHeight();
	}
	
	public void paint(Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(this.x, this.y, this.w, this.h);
		g.setColor(Color.WHITE);
		g.setFont(FONT);
		g.drawString(this.text, this.x + padding, this.y + (this.h/4) * 3);
	}
	
	public boolean isClicked(int mx, int my){
		return (mx > x && mx < x + w) && (my > y && my < y + h);
	}
	
	public int getWidth(){
		return this.w;
	}
	
	public int getX(){
		return this.x;
	}
	
}
