package BadGuyChasesPlayer;

// David Newswanger (16103671)

import java.awt.*;
import BadGuyChasesPlayer.Cell;

import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class BadGuyChase extends JFrame implements MouseMotionListener, MouseListener, Runnable, KeyListener {
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
	
	private BadGuy badGuy;
	private Player player;
	
	public BadGuyChase(){
		// Initialize and center the window
		this.setTitle("Bad Guy Chase");
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
		
		// Init buttons
		start = new Button("Start", 30, 40, this.getGraphics());
		save = new Button("Save", start.getX() + start.getWidth() + 10, 40, this.getGraphics());
		load = new Button("Load", save.getX() + save.getWidth() + 10, 40, this.getGraphics());

		// Init sprites
		save_file = System.getProperty("user.dir") + File.separator + "game_of_life_data" + File.separator + "state.txt";
		
		ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + File.separator + "chase" + File.separator + "badguy.png");
		Image bg = icon.getImage();
		
		badGuy = new BadGuy(bg, 5, 5, CELL_WIDTH);
		
		icon = new ImageIcon(System.getProperty("user.dir") + File.separator + "chase" + File.separator + "player.png");
		bg = icon.getImage();
		
		player = new Player(bg, 20, 20, CELL_WIDTH);
		
		initialized = true;
		
		// Init mouse + keyboard
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		
		// Let it rip!
		Thread t = new Thread(this);
		t.start();

	}
	
	public static void main(String []args){
		BadGuyChase w = new BadGuyChase();
	}
	
	public void saveState(){
		// Save walls as 1 and open space as 0
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
		// Read walls cells as 1 and open as 0
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

	public void run() {
		while(true){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();				
			}
			
			if (this.run_simulation){
				badGuy.calculatePath(cells, player.x, player.y);
				badGuy.move();
				player.move(cells);	
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
				cells[x][y].paint(g);
			}
		}
		
		if(!run_simulation){
			start.paint(g);
			load.paint(g);
			save.paint(g);	
		}
		badGuy.paint(g);
		player.paint(g);
		
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

    public void keyPressed(KeyEvent e) {
		 if (e.getKeyCode()==KeyEvent.VK_LEFT) 
			 player.setXSpeed(-1);
		 else if (e.getKeyCode()==KeyEvent.VK_RIGHT) 
			 player.setXSpeed(1);
		 else if (e.getKeyCode()==KeyEvent.VK_UP) 
			 player.setYSpeed(-1);
		 else if (e.getKeyCode()==KeyEvent.VK_DOWN) 
			 player.setYSpeed(1);
    }
	
    public void keyReleased(KeyEvent e) { 
	   	 if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT)
	   		 player.setXSpeed(0);
	   	 else if (e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_DOWN)
	   		 player.setYSpeed(0);
    }

	public void keyTyped(KeyEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		if(!this.run_simulation){
			this.cells[e.getX() / CELL_WIDTH][e.getY()/CELL_WIDTH].setWall(last_cell_clicked);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
