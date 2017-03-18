package aStar;

// David Newswanger (16103671)

import java.awt.*;
import aStar.Cell;

import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class aStarApplication extends JFrame implements MouseMotionListener, MouseListener, Runnable, KeyListener {
	private static final int NUM_X_CELLS = 40;
	private static final int NUM_Y_CELLS = 40;
	private static final Dimension WindowSize = new Dimension(800,800);
	private static final int CELL_WIDTH = WindowSize.width / NUM_X_CELLS;
	
	
	private boolean run_simulation = false;
	private BufferStrategy strategy;
	private boolean initialized = false;
	private Button start, load, save;
	private boolean last_cell_clicked = false;
	private String save_file;
	private Cell[][] cells = new Cell[NUM_X_CELLS][NUM_Y_CELLS];
	private List<Cell> open = new ArrayList<>();
	
	private int targetX = 5;
	private int targetY = 5;
	
	private int startX = 35;
	private int startY = 28;
	
	private boolean target_reached = false;
	
	
	public aStarApplication(){
		// Initialize and center the window
		this.setTitle("A* Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int screen_x = screensize.width/2 - WindowSize.width/2;
		int screen_y = screensize.height/2 - WindowSize.height/2;
		setBounds(screen_x, screen_y, WindowSize.width, WindowSize.height);
		setVisible(true);
		
		// Initialize the cells
		for (int x = 0; x < NUM_X_CELLS; x++){
			for (int y = 0; y < NUM_Y_CELLS; y++){
				cells[x][y] = new Cell(x, y, CELL_WIDTH);
			}
		}
				
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		start = new Button("Toggle Simulation", 30, 40, this.getGraphics());
		save = new Button("Save", start.getX() + start.getWidth() + 10, 40, this.getGraphics());
		load = new Button("Load", save.getX() + save.getWidth() + 10, 40, this.getGraphics());

		save_file = System.getProperty("user.dir") + File.separator + "game_of_life_data" + File.separator + "state.txt";
		
		open.add(cells[startX][startY]);
		cells[startX][startY].openCell();
		
		cells[startX][startY].setStart();
		cells[targetX][targetY].setTarget();
		
		initialized = true;
		
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		
		Thread t = new Thread(this);
		t.start();

	}
	
	public static void main(String []args){
		aStarApplication w = new aStarApplication();
	}
	
	public void saveState(){
		// Save live cells as 1 and dead cells as 0
		String out = "";
		for(int x = 0; x < NUM_X_CELLS; x ++){
			for (int y = 0; y < NUM_Y_CELLS; y++){
				if (cells[x][y].isWall()){
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
			cells[i / NUM_X_CELLS][i % NUM_Y_CELLS].setWall(data.charAt(i) == '1');
		}
	}
	
	public void update_cells(){
		Cell lowest_f = open.get(open.size() - 1);
		Cell check;				
		for(int i = open.size() - 1; i >= 0; i--){
			if(open.get(i).getF() < lowest_f.getF()){
				lowest_f = open.get(i);
			}
		}
				
		if(lowest_f.x == targetX && lowest_f.y == targetY){
			target_reached = true;
			return;
		}
		
		for (int xx = -1; xx <= 1; xx++){
			for (int yy = -1; yy <= 1; yy++){
				int x_ind = lowest_f.x + xx;
				int y_ind = lowest_f.y + yy;
				
				if(xx!=0 || yy!=0){
					if(x_ind >= 0 && y_ind >= 0 && x_ind < NUM_X_CELLS && y_ind < NUM_Y_CELLS){
						check = cells[x_ind][y_ind];
						if(!check.isWall() && !check.isClosed()){
							if(check.isOpen()){
								check.updateBetterParent(lowest_f);
							} else {
								check.setParent(lowest_f);
								check.calcG();
								check.calcH(targetX, targetY);
								check.openCell();
								open.add(check);
							}
							
						}
					}
				}
			}
		}
		
		lowest_f.closeCell();
		open.remove(lowest_f);
	}

	public void run() {
		while(!target_reached){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();				
			}
			
			if (this.run_simulation){
				this.update_cells();
			}
			this.repaint();
		}
		
		paintPath(cells[targetX][targetY]);
		
	}
	
	public void paintPath(Cell c){
		c.setPath();
		Cell p = c.getParent();
		if (p == null){
			return;
		} else {
			paintPath(p);
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
				cells[x][y].paint(g);
			}
		}
		
		start.paint(g);
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
			this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH].flip();
			last_cell_clicked = this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH].isWall();
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
			this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH].setWall(last_cell_clicked);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
