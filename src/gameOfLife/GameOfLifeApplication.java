package gameOfLife;

// David Newswanger (16103671)

import java.awt.*;
import gameOfLife.Cell;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

import javax.swing.*;

public class GameOfLifeApplication extends JFrame implements MouseListener, Runnable, KeyListener {
	private static final int NUM_X_CELLS = 40;
	private static final int NUM_Y_CELLS = 40;
	private static final Dimension WindowSize = new Dimension(800,800);
	private static final int CELL_WIDTH = WindowSize.width / NUM_X_CELLS;
	private boolean run_simulation = false;
	private int current_buffer = 0;
	private BufferStrategy strategy;
	private boolean initialized = false;

	
	private Cell[][][] cells = new Cell[NUM_X_CELLS][NUM_Y_CELLS][2];
	
	public GameOfLifeApplication(){
		// Initialize and center the window
		this.setTitle("Game of Life");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int screen_x = screensize.width/2 - WindowSize.width/2;
		int screen_y = screensize.height/2 - WindowSize.height/2;
		setBounds(screen_x, screen_y, WindowSize.width, WindowSize.height);
		setVisible(true);
		
		// Initialize the cells
		for (int x = 0; x < NUM_X_CELLS; x++){
			for (int y = 0; y < NUM_Y_CELLS; y++){
				cells[x][y][0] = new Cell(x, y, CELL_WIDTH);
				cells[x][y][1] = new Cell(x, y, CELL_WIDTH);
			}
		}
				
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		initialized = true;
		
		addMouseListener(this);
		addKeyListener(this);
		Thread t = new Thread(this);
		t.start();

	}
	
	public static void main(String []args){
		GameOfLifeApplication w = new GameOfLifeApplication();
	}
	
	public void randomize(){
		for (int x = 0; x < NUM_Y_CELLS; x ++){
			for (int y = 0; y < NUM_X_CELLS; y++){
				if (Math.random() < .5){
					this.cells[x][y][this.current_buffer].flip();
				}
			}
		}
	}
	
	public void update_cells(){
		// Cycle to next buffer
		int old_buffer = this.current_buffer;
		this.current_buffer = (this.current_buffer + 1) % 2;
		
		for (int x = 0; x < NUM_Y_CELLS; x ++){
			for (int y = 0; y < NUM_X_CELLS; y++){
				int live_neighbors = 0;
				
				// count number of live neighbors in old buffer
				for (int xx = -1; xx <= 1; xx++){
					for (int yy = -1; yy <= 1; yy++){
						int x_ind = (((x + xx) % NUM_X_CELLS) + NUM_X_CELLS) % NUM_X_CELLS;
						int y_ind = (((y + yy) % NUM_Y_CELLS) + NUM_Y_CELLS) % NUM_Y_CELLS;
						if(xx!=0 || yy!=0){
							if (this.cells[x_ind][y_ind][old_buffer].is_alive()){
								live_neighbors ++;
							}
						}
					}
				}
				
				// Get state from old buffer and use it to update new buffer
				boolean state = this.cells[x][y][old_buffer].is_alive();
				this.cells[x][y][this.current_buffer].update(live_neighbors, state);
			}
		}
	}

	public void run() {
		while(true){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();				
			}
			
			if (this.run_simulation){
				this.update_cells();
			}
			this.repaint();
		}
		
	}
	
	public void draw_buttons(Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(30, 30, 200, 50);
		g.fillRect(280, 30, 150, 50);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Consolas", Font.PLAIN, 20));
		g.drawString("Toggle Simulation", 40, 60);
		
		g.drawString("Randomize", 300, 60);
		
		
		
	}
	
	public void paint(Graphics g){
		if (!initialized){
			return;
		}
		g = strategy.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WindowSize.width, WindowSize.height);
		
		for (int x = 0; x < NUM_X_CELLS; x++){
			for (int y = 0; y < NUM_Y_CELLS; y++){
				cells[x][y][this.current_buffer].paint(g);
			}
		}
		
		draw_buttons(g);
		
		strategy.show();
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		
		// Detect click on randomize button
		if((e.getX() > 280 && e.getX() < 280 + 150) &&
			e.getY() > 30 && e.getY() < 30 + 50){
			
			randomize();
			return;
		}
		
		// Detect click on run simulation button
		if((e.getX() > 30 && e.getX() < 30 + 200) &&
				e.getY() > 30 && e.getY() < 30 + 50){
				if(this.run_simulation){
					this.run_simulation = false;
				} else {
					this.run_simulation = true;
				}
				
				return;
			}
		
		// Detect click on cell
		if(!this.run_simulation){
			this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH][current_buffer].flip();
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	// Enter can be used to toggle game state too.
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(this.run_simulation){
				this.run_simulation = false;
			} else {
				this.run_simulation = true;
			}
		}
	}

	public void keyTyped(KeyEvent e) {

	}
}
