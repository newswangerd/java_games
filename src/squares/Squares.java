package squares;
/*
 * David Newswanger
 */

import java.awt.*;
import javax.swing.*;

public class Squares extends JFrame {

	private static final Dimension WindowSize = new Dimension(600,600);
	
	// Set the number of squares to display
	private static final int NUMX = 12;
	private static final int NUMY = 12;
	
	// Set amount of space between  squares
	private static final int BORDER = 1;

	
	public Squares(){
		// Initialize the window
		
		this.setTitle("Assignment 1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width/2 - WindowSize.width/2;
		int y = screensize.height/2 - WindowSize.height/2;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setVisible(true);
	}
	
	public static void main(String []args){
		Squares w = new Squares();
	}
	
	public void paint (Graphics g){
		// Square size is window dimension/number of sqares in that dimension
		
		int width = WindowSize.width/NUMX;
		int height = WindowSize.height/NUMY;
		
		for (int y = 0; y < NUMY; y++){
			for (int x = 0; x < NUMX; x++){
				Color c = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
				g.setColor(c);
				
				// Squares are at x and y coordinate + the border and are the specified with and height minus 2 times the border
				// This makes it so that space between squares is actually 2x the border.
				g.fillRect((x * width) + BORDER, (y * height) + BORDER, width - (2* BORDER), height - (2 * BORDER));
			}
		}
		
	}
	
}
