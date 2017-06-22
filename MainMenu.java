package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Button;
import java.awt.color.*;

import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends JFrame {
	
	public class MyCanvas extends JComponent{
		private int radius = 5;
		private int bulletRadius = 5;
		private int x = 60, y = 60;
		public int objectX = 60, objectY = 60;
		private int backX = 0, backY = 0;
		private int height = 103, width = 100;
		private int displacement = 7;
		private Timer myTimer;
		private int bulletX = -50, bulletY = -50;
				
		public void rotateRight(){
			radius += 3;
			repaint();
		}
				
		public void rotateLeft(){
			radius -= 3;
			repaint();
		}
		
		public void rotate(int mouseX, int mouseY){
			/*double vectorX = x + width/2 - mouseX;
			double vectorY = y + height/2 - mouseY;
			double angle = Math.toDegrees(Math.atan(-vectorX/vectorY));
			radius = (int)angle;*/
			repaint();
		}
		
		public void shoot(){
			myTimer = new Timer();
			bulletX = x;
			bulletY = y;
			bulletRadius = radius;
			TimerTask task = new TimerTask(){
				public void run(){
					moveBullet();
				}
			};
			myTimer.schedule(task, 0, 10);
		}
		
		private void moveBullet(){
			double displacementX = 10*Math.sin(Math.toRadians(bulletRadius));
			double displacementY = 10*Math.cos(Math.toRadians(bulletRadius));
			bulletX += displacementX;
			bulletY -= displacementY;
			repaint();
		}
		
		public void moveForward(){
			double displacementX = displacement*Math.sin(Math.toRadians(radius));
			if(x>1000 && displacementX>0 && backX>-2160)
				backX -= displacementX;
			else if(x<200 && displacementX<0 && backX<5)
				backX -= displacementX;
			else
				x += displacementX;
			objectX += displacementX;
			if(x<0 || x>1250){
				x -= displacementX;
				objectX -= displacementX;
			}
			double displacementY = displacement*Math.cos(Math.toRadians(radius));
			objectY += displacementY;
			if(y>450 && displacementY<0 && backY>-1450)
				backY += displacementY;
			else if(y<200 && displacementY>0 && backY<5)
				backY += displacementY;
			else
				y -= displacementY;
			if(y<0 || y>600){
				y += displacementY;
				objectY -= displacementY;
			}
			repaint();
		}
				
		public void paint(Graphics g){
			super.paint(g);
			//g.drawOval(getWidth()/2-radius, getHeight()/2-radius, 2*radius, 2*radius);
			Graphics2D g2 = (Graphics2D) g;
			Image img1 = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Furkan\\workspace\\MVCTest\\bin\\Images\\AirObjects\\Aircraft.gif");
			Image img2 = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Furkan\\workspace\\MVCTest\\bin\\Images\\AirObjects\\paralax_space2.png");
			g.drawImage(img2, backX, backY, this);
			g2.fillOval(bulletX, bulletY, 10, 10);
			g2.rotate(Math.toRadians(radius), x + width/2, y + height/2);
			g2.drawImage(img1, x, y, this);
			g2.rotate(-Math.toRadians(radius), x + width/2, y + height/2);
			g2.setColor(Color.GREEN);
			g2.drawString("Player", x + 35, y - 10);
		}
	}

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private boolean UP = false, LEFT = false, RIGHT = false;
	private boolean MOUSE = false;
	private int mouseX, mouseY;
	
	public MainMenu() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				MOUSE = true;
				mouseX = e.getX();
				mouseY = e.getY();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				MOUSE = false;
			}
		});
		MyCanvas panel = new MyCanvas();
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if(keyCode==KeyEvent.VK_W)
					UP = true;
				
				if(keyCode==KeyEvent.VK_A)
					LEFT = true;
					
				if(keyCode==KeyEvent.VK_D)
					RIGHT = true;
			}
			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if(keyCode==KeyEvent.VK_W)
					UP = false;
				
				if(keyCode==KeyEvent.VK_A)
					LEFT = false;
					
				if(keyCode==KeyEvent.VK_D)
					RIGHT = false;
			}
		});
		
		Timer myTimer = new Timer();
		TimerTask movement = new TimerTask(){

			@Override
			public void run() {
				if(UP)
					panel.moveForward();
				if(LEFT)
					panel.rotateLeft();
				if(RIGHT)
					panel.rotateRight();
				if(MOUSE)
					panel.shoot();
			}
		};
		
		myTimer.schedule(movement, 0, 10);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(panel, BorderLayout.CENTER);
	}

}
