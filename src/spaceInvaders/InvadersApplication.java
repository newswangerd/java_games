package spaceInvaders;
/*
 * David Newswanger 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class InvadersApplication extends JFrame implements Runnable, KeyListener{

	private static final Dimension WindowSize = new Dimension(800,600);
	
	// Set the number of squares to display
	private static final int NUM_X_INVADERS = 5; // TODO: this only works when x and y are the same...
	private static final int NUM_Y_INVADERS = 5;	
	
	private Alien[] Invaders = new Alien[NUM_X_INVADERS * NUM_Y_INVADERS];
	private SpaceShip PlayerShip;
	private String workingDirectory; 
	private BufferStrategy strategy;
	private List<Bullet> bullets = new ArrayList<>();
	private boolean initialized = false;
	private int round = 0;
	private Score current_score, best_score;
	private boolean game_in_progress = false;
	
	public InvadersApplication(){
		// Initialize and center the window
		this.setTitle("Assignment 4");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width/2 - WindowSize.width/2;
		int y = screensize.height/2 - WindowSize.height/2;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setVisible(true);
		
		// All images are stored in the project's root directory
		this.workingDirectory = System.getProperty("user.dir");
		
		// Initialize the player and invaders
		this.initInvaders();
		this.initPlayer();

		this.current_score = new Score("Current", 100, 60);
		this.best_score = new Score("Best", 300, 60);
		
		this.initialized = true;
		
		// Buffer to avoid screen flicker
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// LET IT RIP!
		addKeyListener(this);
		Thread t = new Thread(this);
		t.start();

	}
	
	public static void main(String []args){
		InvadersApplication w = new InvadersApplication();
		
	}
	
	public void run() {
		int counter = 0;
		while (true){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
			}
			
			if(game_in_progress){
				// if aliens are dead, reset board.
				if (!this.moveInvaders(counter)){
					this.round ++;
					initInvaders();
				}
				this.PlayerShip.move();
				this.moveBullets();
				if (checkCollions(this.PlayerShip)){
					this.game_in_progress = false;
					initInvaders();
					this.current_score.reset();
					this.round = 0;
				}
			}
			
			this.repaint();
			counter ++;
		}
	}
	
	private void initInvaders(){
		// Determine some basic positioning so that the aliens start in the center
		int startx = 0;
		int starty = 80;
		
		// Initialize the grid of aliens
		for (int y = 0; y < NUM_Y_INVADERS; y++){
			for (int x = 0; x < NUM_X_INVADERS; x++){
				Alien invader = new Alien(this.workingDirectory, WindowSize);
				invader.setX((x * invader.get_width()) + startx);
				invader.setY((y * invader.get_height()) + starty);
				invader.setXSpeed(round + 1);
				this.Invaders[(int)((y * NUM_Y_INVADERS) + x)] = invader;
			}
		}
	}
	
	private void initPlayer(){
		// Center the player
		
		this.PlayerShip = new SpaceShip(this.workingDirectory + File.separator + "sprites" + File.separator + "player_ship.png", WindowSize);
		this.PlayerShip.setX((WindowSize.width/2) - (this.PlayerShip.get_width()/2));
		this.PlayerShip.setY(WindowSize.height - this.PlayerShip.get_height());
	}
	
	// Returns false if all the aliens are dead.
	private boolean moveInvaders(int counter){
		boolean edge_reached = false;
		boolean alive = false;
		
		for (int i = 0; i < NUM_Y_INVADERS * NUM_X_INVADERS; i++){
			if(Invaders[i].is_alive()){
				alive = true;
			}
			if (counter % 50 == 0){
				Invaders[i].animate();
			}
			Invaders[i].move();
			if(!edge_reached){
				edge_reached = Invaders[i].isTouchingEdge();
			}
		}
		
		// Edge reached should only update once if any of the invaders reach the edge
		if(edge_reached){
			for (int i = 0; i < NUM_Y_INVADERS * NUM_X_INVADERS; i++){
				Invaders[i].changeDirection();
				Invaders[i].moveDown();
			}
			
			edge_reached=false;
		}
		
		return alive;
	}
	
	private void addBullet(){
		ImageIcon icon = new ImageIcon();
		Bullet bullet = new Bullet(this.workingDirectory + File.separator + "sprites" + File.separator + "bullet.png", WindowSize);
		bullet.setX(this.PlayerShip.getX() + this.PlayerShip.get_width()/2);
		bullet.setY(this.PlayerShip.getY());
		bullets.add(bullet);
	}
	
	private void moveBullets(){
		Iterator<Bullet> i = this.bullets.iterator();
		
		while(i.hasNext()){
			Bullet b = i.next();
			b.move();
			boolean collision = checkCollions(b);
			
			// bullets are killed if they are off screen.
			if (collision || !b.is_alive()){
				i.remove();
			}
			
			if (collision){
				this.current_score.icreaseScore();
				if (this.current_score.getScore() > this.best_score.getScore()){
					this.best_score.icreaseScore();
				}
			}
		}
	}
	
	private boolean checkCollions(Sprite2D b){
		// check if a sprite is in the cluster
		// 1 = cluster
		// 2 = sprite
		double x1, y1, x2, y2, h1, w1, h2, w2;
		
		// Cluster coords
		x1 = this.Invaders[0].getX();
		y1 = this.Invaders[0].getY();
		w1 = this.Invaders[0].get_width() * NUM_X_INVADERS;
		h1 = this.Invaders[0].get_height() * NUM_Y_INVADERS;
		
		// Sprite coords
		x2 = b.getX();
		y2 = b.getY();
		w2 = b.get_width();
		h2 = b.get_height();
		
	
		// Check if the sprite has entered the cluster of aliens
		if( ((x1<x2 && x1+w1 > x2) || 
			( x2<x1 && x2+w2 > x1)) && 
			((y1<y2 && y1+h1 > y2) || 
			( y2<y1 && y2+h2 > y1))   ){
		
			// If it has, convert the x/y coords into array coordinates
			int alien_x = (int)((x2 - x1) / (w1 / NUM_X_INVADERS));
			int alien_y = (int)((y2 - y1) / (h1 / NUM_Y_INVADERS));
			
			// convert two dimensional coordinates into one dimensional array index
			if (this.Invaders[(int)((alien_y * NUM_Y_INVADERS) + alien_x)].is_alive()){
				this.Invaders[(int)((alien_y * NUM_Y_INVADERS) + alien_x)].kill();
				return true;
			}
		}
		
		return false;
			
	}
		
	public void paint (Graphics g){		
		if (!this.initialized){
			return;
		}
		
		g = strategy.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WindowSize.width, WindowSize.height);

		if (this.game_in_progress){
			for (int i = 0; i < NUM_Y_INVADERS * NUM_X_INVADERS; i++){
				if (this.Invaders[i].is_alive()){
					this.Invaders[i].paint(g);
				}
			}
			this.PlayerShip.paint(g);
			this.current_score.paint(g);
			this.best_score.paint(g);
			
			for (Bullet b : this.bullets){
				b.paint(g);
			}
		} else {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Consolas", Font.PLAIN, 40));
			g.drawString("READY PLAYER ONE", WindowSize.width/2 - 200, WindowSize.height/2);
			g.setFont(new Font("Consolas", Font.PLAIN, 20));
			g.drawString("Press Enter to continue", WindowSize.width/2 - 128, (WindowSize.height/2) +50 );
		}
		
		strategy.show();
	}

	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			this.PlayerShip.setXSpeed(4);
			this.repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			this.PlayerShip.setXSpeed(-4);
			this.repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_SPACE){
			this.addBullet();
		}

	}
	
	public void keyReleased(KeyEvent e){
		if(!this.game_in_progress){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				this.game_in_progress = true;
			}
		} else {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT){
				this.PlayerShip.setXSpeed(0);
			}
		}
	}
	
	public void keyTyped(KeyEvent e){

	}
	
}


