/* Xander Siruno-Nebel
 * 5/1/22
 * Galbraith Java Programming
 * Traffic Simulation: A simulation of 3 car types on a 3 lane, looping road, attemptint to travel as fast as possible
 */

package me.xandervsn;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Traffic implements ActionListener, Runnable{
	
	JFrame frame = new JFrame("Traffic Simulation");
	Road road = new Road();
	//South container
	JButton start = new JButton("Start");
	JButton stop = new JButton("Stop");
	JLabel throughput = new JLabel("Throughput: 0");
	Container south = new Container();
	//West Container
	JButton semi = new JButton("Add Semi");
	JButton suv = new JButton("Add SUV");
	JButton sports = new JButton("Add Sports");
	Container west = new Container();
	boolean running = false;
	long startTime = 0;
	int carCount;
	
	public Traffic() {
		frame.setSize(1000, 550);
		frame.setLayout(new BorderLayout());
		frame.add(road, BorderLayout.CENTER);
		
		//config layout
		south.setLayout(new GridLayout(1,2));
		south.add(start);
		start.addActionListener(this);
		south.add(stop);
		stop.addActionListener(this);
		south.add(throughput);
		frame.add(south, BorderLayout.SOUTH);
		
		//add cars layout
		west.setLayout(new GridLayout(3, 1));
		west.add(semi);
		semi.addActionListener(this);
		west.add(suv);
		suv.addActionListener(this);
		west.add(sports);
		sports.addActionListener(this);
		frame.add(west, BorderLayout.WEST);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	

	public static void main(String[] args) {
		new Traffic();
	}


	@Override
	public void actionPerformed(ActionEvent e) {//when one of the buttons is pressed
		if(e.getSource().equals(start)) {//starts simulation
			if(!running) {
				running = true;
				road.resetCarCount();
				startTime = System.currentTimeMillis();
				Thread t = new Thread(this);
				t.start();
			}
		}
		if(e.getSource().equals(stop)) {//stops sim
			running = false;
		}
		if(e.getSource().equals(semi)){//adds semi to closest possible lane
			Semi semi = new Semi(0, 40);
			road.addCar(semi);
			for (int x = 0; x < road.ROAD_WIDTH; x = x + 20) {
				for (int y = 40; y < 480; y = y + 120) {
					semi.setX(x);
					semi.setY(y);
					if(road.collision(x, y, semi) == false){
						frame.repaint();
						return;
					}
				}
			}
		}
		if(e.getSource().equals(suv)){//adds suv to closest possible lane
			SUV suv = new SUV(0, 40);
			road.addCar(suv);
			for (int x = 0; x < road.ROAD_WIDTH; x = x + 20) {
				for (int y = 40; y < 480; y = y + 120) {
					suv.setX(x);
					suv.setY(y);
					if(road.collision(x, y, suv) == false){
						frame.repaint();
						return;
					}
				}
			}
		}
		if(e.getSource().equals(sports)){//adds sports car to closest possible lane
			Sports sports = new Sports(0, 40);
			road.addCar(sports);
			for (int x = 0; x < road.ROAD_WIDTH; x = x + 20) {
				for (int y = 40; y < 480; y = y + 120) {
					sports.setX(x);
					sports.setY(y);
					if(road.collision(x, y, sports) == false){
						frame.repaint();
						return;
					}
				}
			}
		}
	}

 
	@Override
	public void run() {//runs the game
		while(running) {
			road.step();
			carCount = road.getCarCount(); 
			double throughputCalc = carCount / (1000 * (double)(System.currentTimeMillis() - startTime));
			throughput.setText("Throughput: " + throughputCalc);
			frame.repaint();
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
