import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class SolarSystem {
	static final double G = 0.000667;           //universal gravitational constant
	ArrayList<Planet> list = new ArrayList<Planet>();
	float timestep=0;

	public SolarSystem(){
	
		Planet Sun = new Planet("Sun",19.89,0,0);

	
		Planet Mercury = new Planet("Mercury",0.00000330,- 5.7,0);
		Planet Venus = new Planet("Venus",0.0000487,- 10.8,0);
		Planet Earth = new Planet("Earth", 0.00005974, 15.2, 0, 0, -0.029291);
		Planet Mars = new Planet("Mars",0.00000642,+ 22.8,0);
		Planet Jupiter = new Planet("Jupiter",0.01899,- 77.8,0);
		Planet Saturn = new Planet("Saturn",0.00568,+ 142.4,0);
		//Planet Uranus = new Planet("Uranus",0.000866,+ 287.2,0);
		//Planet Neptune = new Planet("Neptune",0.00103, + 449.9,0);

		Planet Moon = new Planet("Moon",0.0000007342, 15.2+0.040550, 0, 0, -0.029291+0.000964);
		
		list = new ArrayList<Planet>();
		list.add(Sun);
		list.add(Mercury);
		list.add(Venus);
		list.add(Earth);
		list.add(Mars);
		list.add(Jupiter);
		list.add(Saturn);
		//list.add(Uranus);
		//list.add(Neptune);
		
		for(Planet p : list){
			if(p.name!="Sun")
				p.velY = Math.sqrt(G * Sun.mass / Math.abs(p.x - Sun.x));
			System.out.println("v = "+p.velY);
			if (p.x > Sun.x)
				p.velY = -p.velY;
		}
		

		getAccelerations();
	}

	void move() {
		for (Planet p1 : list) {
			// update position according to dr = v·dt + a·dt²/2
			p1.x += timestep * (p1.velX + timestep * p1.accX / 2);
			p1.y += timestep * (p1.velY + timestep * p1.accY / 2);
			// keep accelerations for velocity calculation
			p1.oldAccX = p1.accX;
			p1.oldAccY = p1.accY;
		}
	}
	void getAccelerations(){
		double dx,dy,dz,D,A;
		for(Planet p1 : list) {
			p1.accX = 0;
			p1.accY = 0;
			for (Planet p2 : list) {
				if (p1 != p2) {
					dx = p2.x - p1.x;
					dy = p2.y - p1.y;
					D = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
					A = G * p2.mass / Math.pow(D, 2);
					p1.accX += dx * A / D;
					p1.accY += dy * A / D;
				}
			}
		}
	}
	void accelerate()
	{
		for(Planet p1 : list)
		{
			//update velocity according to dv=(a+oldA)*dt/2
			p1.velX+=(p1.accX+p1.oldAccX)*timestep/2;
			p1.velY+=(p1.accY+p1.oldAccY)*timestep/2;
		}
	}
	void updatePositions(){
		move();
		getAccelerations();
		accelerate();
	}
}


class SolarGUI extends JPanel implements Runnable{
	int offX=400, offY=400;
	double scale=2;
	SolarSystem s;
	public SolarGUI(){
		s = new SolarSystem();
		setFocusable(true);
	    requestFocus();
	}

	public void paintComponent(Graphics g) {
		int i=0;
		int diameter=5;
//		double centraY = s.list.get(0).y;
//		double centraX = s.list.get(0).x;
		for(Planet p : s.list){
			if(i!=0) diameter=2;
			int x = (int)(offX-diameter/2+p.x*scale);
			int y = (int)(offY-diameter/2+p.y*scale);
			g.fillOval(x, y,diameter,diameter);
			//g.drawString(p.name, x, y);
			i++;
			for(int k=1; k<p.positions.size(); k++){
				Point pt0 = p.positions.get(k-1);
				Point pt1 = p.positions.get(k);
				g.drawLine((int)(offX+pt0.x*scale), (int)(offY+pt0.y*scale), (int)(offX+pt1.x*scale), (int)(offY+pt1.y*scale));
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			s.timestep+=0.005;
			s.updatePositions();
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		JFrame f = new JFrame("Sistema solare");
		f.setBounds(0, 0, 800, 800);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SolarGUI p = new SolarGUI();
		f.add(p);
		new Thread(p).start();
		f.setVisible(true);

	}
}


