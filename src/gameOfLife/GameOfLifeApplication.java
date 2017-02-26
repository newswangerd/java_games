package gameOfLife;

// David Newswanger (16103671)

import java.awt.*;
import gameOfLife.Cell;
import spaceInvaders.InvadersApplication;
import java.awt.event.*;

import javax.swing.*;

public class GameOfLifeApplication extends JFrame implements MouseListener {
	private static final int NUM_X_CELLS = 40;
	private static final int NUM_Y_CELLS = 40;
	private static final Dimension WindowSize = new Dimension(800,800);
	private static final int CELL_WIDTH = WindowSize.width / NUM_X_CELLS;
	
	private Cell[][] cells = new Cell[NUM_X_CELLS][NUM_Y_CELLS];
	
	public GameOfLifeApplication(){
		// Initialize and center the window
		this.setTitle("Invaders Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int screen_x = screensize.width/2 - WindowSize.width/2;
		int screen_y = screensize.height/2 - WindowSize.height/2;
		setBounds(screen_x, screen_y, WindowSize.width, WindowSize.height);
		setVisible(true);
		
		for (int x = 0; x < NUM_X_CELLS; x++){
			for (int y = 0; y < NUM_Y_CELLS; y++){
				cells[x][y] = new Cell(x, y, CELL_WIDTH);
			}
		}
		
		addMouseListener(this);
	}
	
	public static void main(String []args){
		GameOfLifeApplication w = new GameOfLifeApplication();
		
	}
	
	public void paint(Graphics g){
		for (int x = 0; x < NUM_X_CELLS; x++){
			for (int y = 0; y < NUM_Y_CELLS; y++){
				cells[x][y].paint(g);
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
//		this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH].flip();
//		this.repaint();
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH].flip();
		this.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
