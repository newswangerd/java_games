package gameOfLife;

// David Newswanger (16103671)

import java.awt.*;
import gameOfLife.Cell;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.*;

import javax.swing.*;

public class GameOfLifeApplication extends JFrame implements MouseMotionListener, MouseListener, Runnable, KeyListener {
	private static final int NUM_X_CELLS = 40;
	private static final int NUM_Y_CELLS = 40;
	private static final Dimension WindowSize = new Dimension(800,800);
	private static final int CELL_WIDTH = WindowSize.width / NUM_X_CELLS;
	
	
	private boolean run_simulation = false;
	private int current_buffer = 0;
	private BufferStrategy strategy;
	private boolean initialized = false;
	private Button start, rnd_button, load, save;
	private boolean last_cell_clicked = false;
	private String save_file;
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
		
		start = new Button("Toggle Simulation", 30, 40, this.getGraphics());
		rnd_button = new Button("Randomize", start.getX() + start.getWidth() + 10, 40, this.getGraphics());
		save = new Button("Save", rnd_button.getX() + rnd_button.getWidth() + 10, 40, this.getGraphics());
		load = new Button("Load", save.getX() + save.getWidth() + 10, 40, this.getGraphics());

		save_file = System.getProperty("user.dir") + File.separator + "game_of_life_data" + File.separator + "state.txt";
		
		initialized = true;
		
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		
		Thread t = new Thread(this);
		t.start();

	}
	
	public static void main(String []args){
		GameOfLifeApplication w = new GameOfLifeApplication();
	}
	
	public void saveState(){
		// Save live cells as 1 and dead cells as 0
		String out = "";
		for(int x = 0; x < NUM_X_CELLS; x ++){
			for (int y = 0; y < NUM_Y_CELLS; y++){
				if (cells[x][y][current_buffer].isAlive()){
					out += "1";
				} else {
					out += "0";
				}
			}
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(save_file));
			writer.write(out);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadState(){
		// Read live cells as 1 and dead as 0
		String data = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(save_file));
			data = reader.readLine();
			reader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		for (int i = 0; i < data.length(); i ++){
			cells[i / NUM_X_CELLS][i % NUM_Y_CELLS][current_buffer].setAlive(data.charAt(i) == '1');
		}
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
							if (this.cells[x_ind][y_ind][old_buffer].isAlive()){
								live_neighbors ++;
							}
						}
					}
				}
				
				// Get state from old buffer and use it to update new buffer
				boolean state = this.cells[x][y][old_buffer].isAlive();
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
		
		start.paint(g);
		rnd_button.paint(g);
		load.paint(g);
		save.paint(g);
		
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
		if(rnd_button.isClicked(e.getX(), e.getY())){
			randomize();
			return;
		}
		
		// Detect click on run simulation button
		if(start.isClicked(e.getX(), e.getY())){
			this.run_simulation = !this.run_simulation;
			return;
		}
		
		if (save.isClicked(e.getX(), e.getY())){
			saveState();
			return;
		}
		
		if (load.isClicked(e.getX(), e.getY())){
			loadState();
			return;
		}
		
		// Detect click on cell
		if(!this.run_simulation){
			this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH][current_buffer].flip();
			last_cell_clicked = this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH][current_buffer].isAlive();
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
			run_simulation = !run_simulation;
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!this.run_simulation){
			this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH][current_buffer].setAlive(last_cell_clicked);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
